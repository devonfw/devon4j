package com.devonfw.module.httpclient.common.impl.rest;

import java.util.function.Consumer;

/**
 * {@link Consumer} used as error-handling to immediately throw received exceptions.
 *
 * @since 2020.12.001
 */
class SyncErrorHandlerThrowImmediately implements Consumer<Throwable> {

  private static final SyncErrorHandlerThrowImmediately INSTANCE = new SyncErrorHandlerThrowImmediately();

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
  public static SyncErrorHandlerThrowImmediately get() {

    return INSTANCE;
  }

}
