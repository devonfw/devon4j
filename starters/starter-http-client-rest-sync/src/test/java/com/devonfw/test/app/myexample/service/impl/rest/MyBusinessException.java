package com.devonfw.test.app.myexample.service.impl.rest;

import net.sf.mmm.util.exception.api.NlsRuntimeException;

/**
 * Business exception for testing.
 */
public class MyBusinessException extends NlsRuntimeException {

  /** @see #getMessage() */
  public static final String MESSAGE = "Test of business error";

  /** @see #getCode() */
  public static final String CODE = "BusinessErrorCode";

  private static final long serialVersionUID = 1L;

  /**
   * The constructor.
   */
  public MyBusinessException() {

    super(null, MESSAGE);
  }

  @Override
  public String getCode() {

    return CODE;
  }

  @Override
  public boolean isTechnical() {

    return false;
  }

}
