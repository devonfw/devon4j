package com.devonfw.test.app.myexample.service.impl.rest;

import javax.inject.Named;

import com.devonfw.test.app.myexample.service.api.rest.MyExampleRestService;

/**
 * Implementation of {@link MyExampleRestService}.
 */
@Named
public class MyExampleRestServiceImpl implements MyExampleRestService {

  @Override
  public String greet(String name) {

    return "Hi " + name + "!";
  }

  @Override
  public void businessError() {

    throw new MyBusinessException();
  }

  @Override
  public void technicalError() {

    throw new IllegalStateException("Secret information");
  }

}
