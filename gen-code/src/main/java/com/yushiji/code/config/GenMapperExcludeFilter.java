package com.yushiji.code.config;

import com.yushiji.code.common.core.enums.DatabaseType;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.TypeFilter;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 自定义过滤器排除特定的mapper接口
 *
 * @author lucky_fd
 * @since 2024.3.20
 */
@Deprecated
public class GenMapperExcludeFilter implements TypeFilter {

    @Resource
    private GenConfig genConfig;
    // 排除mysql mapper接口
    private final Set<String> excludeMysqlClass = new HashSet<>(Arrays.asList(
            "com.yushiji.code.mapper.mysql.GenTableColumnMapper",
            "com.yushiji.code.mapper.mysql.GenTableMapper"));
    // 排除postgresql mapper接口
    private final Set<String> excludePostgresqlClass = new HashSet<>(Arrays.asList(
            "com.yushiji.code.mapper.postgre.GenTableColumnMapper",
            "com.yushiji.code.mapper.postgre.GenTableMapper"));

    @Override
    public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory) throws IOException {
        String className = metadataReader.getClassMetadata().getClassName();
        if (genConfig.getDatabaseType().equals(DatabaseType.MYSQL)) {
            return excludePostgresqlClass.contains(className);
        } else if (genConfig.getDatabaseType().equals(DatabaseType.POSTGRESQL)) {
            return excludeMysqlClass.contains(className);
        }
        return false;
    }
}
