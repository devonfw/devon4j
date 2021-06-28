
package com.devonfw.module.service.common.base.client;

import java.lang.reflect.Method;
import java.util.Objects;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.devonfw.module.service.common.api.client.SyncServiceClient;
import com.devonfw.module.service.common.api.client.async.ServiceClientInvocation;
import com.devonfw.module.service.common.api.client.async.ServiceClientStub;
import com.devonfw.module.service.common.api.client.context.ServiceContext;

/**
 * Abstract base implementation of {@link SyncServiceClient}.
 *
 * @param <S> type of the {@link #get() service client}.
 * @since 2021.08.003
 */

public abstract class AbstractSyncServiceClient<S> implements SyncServiceClient<S> {

  private static final Logger LOG = LoggerFactory.getLogger(AbstractSyncServiceClient.class);

  private final S proxy;

  private final ServiceClientStub<S> stub;

  /** The {@link #setErrorHandler(Consumer)} */

  private Consumer<Throwable> errorHandler;

  /** The most recent invocation. */

  private ServiceClientInvocation<S> invocation;

  /**
   * The constructor.
   *
   * @param proxy the {@link #get() service client}.
   * @param stub the {@link ServiceClientStub}.
   */

  public AbstractSyncServiceClient(S proxy, ServiceClientStub<S> stub) {

    super();
    this.proxy = proxy;
    this.stub = stub;
    this.errorHandler = this::logError;
  }

  @Override
  public S get() {

    return this.proxy;
  }

  @Override
  public Consumer<Throwable> getErrorHandler() {

    return this.errorHandler;
  }

  @Override
  public void setErrorHandler(Consumer<Throwable> errorHandler) {

    Objects.requireNonNull(errorHandler);
    this.errorHandler = errorHandler;
  }

  private ServiceClientInvocation<S> getInvocation() {

    this.invocation = this.stub.getInvocation();
    Objects.requireNonNull(this.invocation, "invocation");
    ServiceContext<S> context = this.invocation.getContext();
    Objects.requireNonNull(context, "context");
    Method method = this.invocation.getMethod();
    Objects.requireNonNull(method, "method");
    Object[] parameters = this.invocation.getParameters();
    assert (method.getParameterCount() == getLength(parameters));
    return this.invocation;
  }

  private void logError(Throwable error) {

    ServiceContext<S> context = this.stub.getContext();
    String methodName = "undefined";
    if (this.invocation != null) {
      methodName = this.invocation.getMethod().getName();
    }
    LOG.error("Failed to invoke service {}#{} at {}", context.getApi().getName(), methodName, context.getUrl(), error);
  }

  private static int getLength(Object[] parameters) {

    if (parameters == null) {
      return 0;
    }
    return parameters.length;
  }

}
