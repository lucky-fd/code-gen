/*
 * Copyright 2002-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.yushi.code.east.io;

import com.yushi.code.east.util.PathUtils;
import org.springframework.core.io.UrlResource;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.NoSuchFileException;
import java.nio.file.StandardOpenOption;

/**
 * Abstract base class for resources which resolve URLs into File references, such as {@link
 * UrlResource} or {@link ClassPathResource}.
 *
 * <p>Detects the "file" protocol as well as the JBoss "vfs" protocol in URLs, resolving file system
 * references accordingly.
 *
 * @author Juergen Hoeller
 * @since 3.0
 */
public abstract class AbstractFileResolvingResource extends AbstractResource {

  @Override
  public boolean exists() {
    try {
      URL url = getURL();
      if (PathUtils.isFileURL(url)) {
        // Proceed with file system resolution
        return getFile().exists();
      } else {
        // Try a URL connection content-length header
        URLConnection con = url.openConnection();
        customizeConnection(con);
        HttpURLConnection httpCon =
            (con instanceof HttpURLConnection ? (HttpURLConnection) con : null);
        if (httpCon != null) {
          int code = httpCon.getResponseCode();
          if (code == HttpURLConnection.HTTP_OK) {
            return true;
          } else if (code == HttpURLConnection.HTTP_NOT_FOUND) {
            return false;
          }
        }
        if (con.getContentLengthLong() >= 0) {
          return true;
        }
        if (httpCon != null) {
          // No HTTP OK status, and no content-length header: give up
          httpCon.disconnect();
          return false;
        } else {
          // Fall back to stream existence: can we open the stream?
          getInputStream().close();
          return true;
        }
      }
    } catch (IOException ex) {
      return false;
    }
  }

  @Override
  public boolean isReadable() {
    try {
      URL url = getURL();
      if (PathUtils.isFileURL(url)) {
        // Proceed with file system resolution
        File file = getFile();
        return (file.canRead() && !file.isDirectory());
      } else {
        return true;
      }
    } catch (IOException ex) {
      return false;
    }
  }

  @Override
  public boolean isFile() {
    try {
      URL url = getURL();
      if (url.getProtocol().startsWith(ResourceResolver.URL_PROTOCOL_VFS)) {
        return VfsResourceDelegate.getResource(url).isFile();
      }
      return ResourceResolver.URL_PROTOCOL_FILE.equals(url.getProtocol());
    } catch (IOException ex) {
      return false;
    }
  }

  /**
   * This implementation returns a File reference for the underlying class path resource, provided
   * that it refers to a file in the file system.
   *
   * @see ResourceResolver#getFile(URL, String)
   */
  @Override
  public File getFile() throws IOException {
    URL url = getURL();
    if (url.getProtocol().startsWith(ResourceResolver.URL_PROTOCOL_VFS)) {
      return VfsResourceDelegate.getResource(url).getFile();
    }
    return ResourceResolver.getFile(url, getDescription());
  }

  /**
   * This implementation determines the underlying File (or jar file, in case of a resource in a
   * jar/zip).
   */
  @Override
  protected File getFileForLastModifiedCheck() throws IOException {
    URL url = getURL();
    if (ResourceResolver.isJarURL(url)) {
      URL actualUrl = ResourceResolver.extractArchiveURL(url);
      if (actualUrl.getProtocol().startsWith(ResourceResolver.URL_PROTOCOL_VFS)) {
        return VfsResourceDelegate.getResource(actualUrl).getFile();
      }
      return ResourceResolver.getFile(actualUrl, "Jar URL");
    } else {
      return getFile();
    }
  }

  /**
   * This implementation returns a File reference for the given URI-identified resource, provided
   * that it refers to a file in the file system.
   *
   * @since 5.0
   * @see #getFile(URI)
   */
  protected boolean isFile(URI uri) {
    try {
      if (uri.getScheme().startsWith(ResourceResolver.URL_PROTOCOL_VFS)) {
        return VfsResourceDelegate.getResource(uri).isFile();
      }
      return ResourceResolver.URL_PROTOCOL_FILE.equals(uri.getScheme());
    } catch (IOException ex) {
      return false;
    }
  }

  /**
   * This implementation returns a File reference for the given URI-identified resource, provided
   * that it refers to a file in the file system.
   *
   * @see ResourceResolver#getFile(URI, String)
   */
  protected File getFile(URI uri) throws IOException {
    if (uri.getScheme().startsWith(ResourceResolver.URL_PROTOCOL_VFS)) {
      return VfsResourceDelegate.getResource(uri).getFile();
    }
    return ResourceResolver.getFile(uri, getDescription());
  }

  /**
   * This implementation returns a FileChannel for the given URI-identified resource, provided that
   * it refers to a file in the file system.
   *
   * @since 5.0
   * @see #getFile()
   */
  @Override
  public ReadableByteChannel readableChannel() throws IOException {
    try {
      // Try file system channel
      return FileChannel.open(getFile().toPath(), StandardOpenOption.READ);
    } catch (FileNotFoundException | NoSuchFileException ex) {
      // Fall back to InputStream adaptation in superclass
      return super.readableChannel();
    }
  }

  @Override
  public long contentLength() throws IOException {
    URL url = getURL();
    if (PathUtils.isFileURL(url)) {
      // Proceed with file system resolution
      return getFile().length();
    } else {
      // Try a URL connection content-length header
      URLConnection con = url.openConnection();
      customizeConnection(con);
      return con.getContentLengthLong();
    }
  }

  @Override
  public long lastModified() throws IOException {
    URL url = getURL();
    if (PathUtils.isFileURL(url) || ResourceResolver.isJarURL(url)) {
      // Proceed with file system resolution
      try {
        return super.lastModified();
      } catch (FileNotFoundException ex) {
        // Defensively fall back to URL connection check instead
      }
    }
    // Try a URL connection last-modified header
    URLConnection con = url.openConnection();
    customizeConnection(con);
    return con.getLastModified();
  }

  /**
   * Customize the given {@link URLConnection}, obtained in the course of an {@link #exists()},
   * {@link #contentLength()} or {@link #lastModified()} call.
   *
   * <p>Calls {@link ResourceResolver#useCachesIfNecessary(URLConnection)} and delegates to {@link
   * #customizeConnection(HttpURLConnection)} if possible. Can be overridden in subclasses.
   *
   * @param con the URLConnection to customize
   * @throws IOException if thrown from URLConnection methods
   */
  protected void customizeConnection(URLConnection con) throws IOException {
    ResourceResolver.useCachesIfNecessary(con);
    if (con instanceof HttpURLConnection) {
      customizeConnection((HttpURLConnection) con);
    }
  }

  /**
   * Customize the given {@link HttpURLConnection}, obtained in the course of an {@link #exists()},
   * {@link #contentLength()} or {@link #lastModified()} call.
   *
   * <p>Sets request method "HEAD" by default. Can be overridden in subclasses.
   *
   * @param con the HttpURLConnection to customize
   * @throws IOException if thrown from HttpURLConnection methods
   */
  protected void customizeConnection(HttpURLConnection con) throws IOException {
    con.setRequestMethod("HEAD");
  }

  /** Inner delegate class, avoiding a hard JBoss VFS API dependency at runtime. */
  private static class VfsResourceDelegate {

    public static Resource getResource(URL url) throws IOException {
      return new VfsResource(VfsUtils.getRoot(url));
    }

    public static Resource getResource(URI uri) throws IOException {
      return new VfsResource(VfsUtils.getRoot(uri));
    }
  }
}
