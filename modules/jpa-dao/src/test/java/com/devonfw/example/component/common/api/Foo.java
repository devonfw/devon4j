package com.devonfw.example.component.common.api;

import com.devonfw.example.general.common.api.TestApplicationEntity;
import com.devonfw.module.basic.common.api.reference.IdRef;

public interface Foo extends TestApplicationEntity {

  String getName();

  void setName(String name);

  IdRef<Bar> getBarId();

  void setBarId(IdRef<Bar> barId);

}
