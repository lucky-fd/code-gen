package com.yushi.code.tableCreator;

import com.yushi.code.tableCreator.config.Configuration;
import com.yushi.code.tableCreator.jdbc.metadata.DbMetaData;
import lombok.Data;

/**
 * wind上下文.包含配置信息,管理的entity信息.
 *
 * @since 2022.03.05
 * @author ramer
 */
@Data
public class WindContext {
  WindContext() {}

  /** 默认数据源元数据,自动建表也是基于该数据 */
  private DbMetaData dbMetaData;

  private Configuration configuration;
}
