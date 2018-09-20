package com.devonfw.example.component.common.api;

import com.devonfw.example.general.common.api.TestApplicationEntity;

public interface Bar extends TestApplicationEntity {

  String getMessage();

  void setMessage(String message);

}
