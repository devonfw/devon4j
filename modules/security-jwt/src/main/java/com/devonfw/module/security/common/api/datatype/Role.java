package com.devonfw.module.security.common.api.datatype;

import java.security.Principal;

public enum Role implements Principal {

  WAITER("Waiter"), CUSTOMER("Customer");

  private final String name;

  private Role(String name) {

    this.name = name;
  }

  @Override
  public String getName() {

    return "ROLE_" + this.name;
  }
}
