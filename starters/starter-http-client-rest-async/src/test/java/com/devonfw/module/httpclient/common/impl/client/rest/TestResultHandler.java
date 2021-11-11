package com.devonfw.module.httpclient.common.impl.client.rest;

import java.util.function.Consumer;

import com.devonfw.module.service.common.api.client.AsyncServiceClient;

/**
 * Simple container for {@link #getResponse() response} and {@link #getError() error} of an asynchronous REST client
 * response for testing.
 *
 * @param <T> type of the {@link #getResponse() response}.
 */
public class TestResultHandler<T> implements Consumer<T> {

  private final ErrorHandler errorHandler;

  private T response;

  /**
   * The constructor.
   *
   * @param serviceClient the {@link AsyncServiceClient}.
   */
  public TestResultHandler(AsyncServiceClient<?> serviceClient) {

    this(serviceClient.getErrorHandler());
  }

  /**
   * The constructor.
   *
   * @param defaultErrorHandler the default error handler to extend.
   */
  public TestResultHandler(Consumer<Throwable> defaultErrorHandler) {

    super();
    this.errorHandler = new ErrorHandler(defaultErrorHandler);
  }

  @Override
  public void accept(T t) {

    if (this.response != null) {
      throw new IllegalStateException("Response already received!");
    }
    assert (t != null);
    this.response = t;
  }

  /**
   * @return the error {@link Consumer}.
   */
  public Consumer<Throwable> getErrorHandler() {

    return this.errorHandler;
  }

  /**
   * @return the received response or {@code null} if not yet available.
   */
  public T getResponse() {

    return this.response;
  }

  /**
   * @return the received error or {@code null} if no error has been received.
   */
  public Throwable getError() {

    return this.errorHandler.error;
  }

  /**
   * @return the received response. This method will block until the response has been received.
   */
  public T getResponseOrWait() {

    wait4response();
    if (this.response == null) {
      throw new IllegalStateException("Expected valid response but received error!", this.errorHandler.error);
    }
    return this.response;
  }

  /**
   * @return the received response. This method will block until the response has been received.
   */
  public Throwable getErrorOrWait() {

    wait4response();
    if (this.errorHandler.error == null) {
      throw new IllegalStateException("Expected error but received response: " + this.response);
    }
    return this.errorHandler.error;
  }

  private void wait4response() {

    int i = 0;
    while ((this.response == null) && (this.errorHandler.error == null)) {
      try {
        Thread.sleep(100);
      } catch (InterruptedException e) {
      }
      i++;
      if (i > 1000) {
        throw new IllegalStateException("AsyncServiceClient did not answer for 100 seconds.");
      }
    }
  }

  private static class ErrorHandler implements Consumer<Throwable> {

    private final Consumer<Throwable> defaultErrorHandler;

    private Throwable error;

    /**
     * The constructor.
     *
     * @param defaultErrorHandler the default error handler to extend.
     */
    public ErrorHandler(Consumer<Throwable> defaultErrorHandler) {

      super();
      this.defaultErrorHandler = defaultErrorHandler;
    }

    @Override
    public void accept(Throwable t) {

      if (this.error != null) {
        throw new IllegalStateException("Response already received!");
      }
      assert (t != null);
      if (this.defaultErrorHandler != null) {
        this.defaultErrorHandler.accept(t);
      }
      this.error = t;

    }

  }

}
