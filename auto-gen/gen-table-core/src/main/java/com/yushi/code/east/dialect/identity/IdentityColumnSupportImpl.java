package com.yushi.code.east.dialect.identity;

import com.yushi.code.east.exception.WindException;

import java.lang.reflect.Type;

/** @author Andrea Boriero */
public class IdentityColumnSupportImpl implements IdentityColumnSupport {

  @Override
  public boolean containDataTypeInIdentityColumn() {
    return false;
  }

  @Override
  public String getIdentityColumnString(Type type) throws WindException {
    throw new WindException(getClass().getName() + " does not support identity key generation");
  }
}
