package com.yushi.code.tableCreator.support;

import java.util.UUID;

/**
 * {@link UUID#randomUUID()}.
 *
 * @author fdong
 * @since 2022.01.03
 */
public class UUIDGenerator implements IdGenerator {

  @Override
  public Object nextId(final Object obj) {
    return UUID.randomUUID().toString().replace("-", "");
  }
}
