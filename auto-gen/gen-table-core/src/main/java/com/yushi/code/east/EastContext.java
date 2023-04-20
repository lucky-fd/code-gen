package com.yushi.code.east;

import com.yushi.code.east.config.EastConfiguration;
import com.yushi.code.east.jdbc.metadata.DbMetaData;
import lombok.Data;

/**
 * east上下文.包含配置信息,管理的entity信息.
 *
 * @since 2022.03.05
 * @author fdong
 */
@Data
public class EastContext {

  /** 默认数据源元数据,自动建表也是基于该数据 */
  private DbMetaData dbMetaData;

  /** 配置信息 */
  private EastConfiguration eastConfiguration;
}
