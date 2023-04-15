package com.yushi.code.tableCreator.io;

import com.yushi.code.tableCreator.util.Asserts;
import com.yushi.code.tableCreator.util.ClassUtils;
import com.yushi.code.tableCreator.util.PathUtils;

import javax.annotation.Nullable;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author ramer
 * @since 2022.05.29
 */
public class DefaultResourceLoader implements ResourceLoader {
    private final ClassLoader classLoader;

    public DefaultResourceLoader() {
        this.classLoader = ClassUtils.getDefaultClassLoader();
    }

    @Nullable
    @Override
    public ClassLoader getClassLoader() {
        return (this.classLoader != null ? this.classLoader : ClassUtils.getDefaultClassLoader());
    }

    @Override
    public Resource getResource(final String location) {
        Asserts.notNull(location, "Location must not be null");

        if (location.startsWith("/")) {
            return getResourceByPath(location);
        } else if (location.startsWith(CLASSPATH_URL_PREFIX)) {
            return new ClassPathResource(
                    location.substring(CLASSPATH_URL_PREFIX.length()), getClassLoader());
        } else {
            try {
                // Try to parse the location as a URL...
                URL url = new URL(location);
                return (PathUtils.isFileURL(url) ? new FileUrlResource(url) : new UrlResource(url));
            } catch (MalformedURLException ex) {
                // No URL -> resolve as resource path.
                return getResourceByPath(location);
            }
        }
    }

    protected Resource getResourceByPath(String path) {
        return new ClassPathContextResource(path, getClassLoader());
    }

    protected static class ClassPathContextResource extends ClassPathResource
            implements ContextResource {

        public ClassPathContextResource(String path, @Nullable ClassLoader classLoader) {
            super(path, classLoader);
        }

        @Override
        public String getPathWithinContext() {
            return getPath();
        }

        @Override
        public Resource createRelative(String relativePath) {
            String pathToUse = PathUtils.applyRelativePath(getPath(), relativePath);
            return new ClassPathContextResource(pathToUse, getClassLoader());
        }
    }
}
