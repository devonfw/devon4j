package com.devonfw.module.httpclient.common.impl;

import java.util.function.Consumer;

/**
 * {@link Consumer} used as error-handling to immediately throw received exceptions.
 *
 * @since 2020.12.001
 */
class ErrorHandlerThrowImmediately implements Consumer<Throwable> {

  private static final ErrorHandlerThrowImmediately INSTANCE = new ErrorHandlerThrowImmediately();

  @Override
  public void accept(Throwable t) {

    if (t instanceof RuntimeException) {
      throw (RuntimeException) t;
    } else {
      throw new IllegalStateException(t);
    }
  }

  /**
   * @return the singleton instance.
   */
  public static ErrorHandlerThrowImmediately get() {

    return INSTANCE;
  }

}
