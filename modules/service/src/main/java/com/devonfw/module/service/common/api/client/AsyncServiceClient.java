package com.devonfw.module.service.common.api.client;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

/**
 * An {@link AsyncServiceClient} wraps a {@link #get() service client} allowing to do {@link #call(Object, Consumer)
 * asynchronous calls}. The following code is a simple example how to do an asynchronous service invocation:
 *
 * <pre>
 * {@link ServiceClientFactory} factory = getServiceClientFactory();
 * {@link AsyncServiceClient}{@literal <MyService>} client = factory.{@link ServiceClientFactory#createAsync(Class) createAsync}(MyService.class);
 * client.{@link #call(Object, Consumer) call}(client.{@link #get()}.doSomethingOperation(args), r -> {
 *   handleAsyncResult(r);
 * });
 * </pre>
 *
 * Please note that the {@link #get() actual service client} is just a stub recording the desired service operation to
 * call asynchronously. It will only return a dummy result (typically {@code null} and for primitive types an according
 * default like {@code 0} or {@code false}). This approach allows the {@link #call(Object, Consumer) call} method to be
 * type-safe ensuring that the {@link Consumer} callback fits to the result type of the invoked operation.<br>
 * Therefore ensure that you always call exactly one service operation on the {@link #get() service client} immediatly
 * followed by a {@link #call(Object, Consumer) call} method invocation.
 *
 * @param <S> type of the {@link #get() service client}.
 * @since 2020.08.001
 * @see ServiceClientFactory#createAsync(Class)
 */
public interface AsyncServiceClient<S> {

  /**
   * @return the actual service client as dynamic proxy of the service interface provided when this
   *         {@link AsyncServiceClient} was {@link ServiceClientFactory#createAsync(Class) created}. It is just a stub
   *         that will only record the invoked service operation returning a dummy result. In order to actually trigger
   *         the service invocation you need to invoke the {@link #call(Object, Consumer) call} method immediately after
   *         calling the service operation as described in the type comment of this {@link AsyncServiceClient}.
   */
  S get();

  /**
   * @return the the {@link Consumer} callback {@link Consumer#accept(Object) accepting} a potential exception that
   *         occurred during sending the request or receiving the response.
   */
  Consumer<Throwable> getErrorHandler();

  /**
   * @param errorHandler the {@link Consumer} callback {@link Consumer#accept(Object) accepting} a potential exception
   *        that occurred during sending the request or receiving the response.
   */
  void setErrorHandler(Consumer<Throwable> errorHandler);

  /**
   * @param <R> type of the result of the service operation to call.
   * @param result the dummy result returned by the operation invoked on the actual {@link #get() service client}.
   * @param resultHandler the {@link Consumer} callback {@link Consumer#accept(Object) accepting} the actual result
   *        asynchronously when available.
   */
  <R> void call(R result, Consumer<R> resultHandler);

  /**
   * @param serviceInvoker the lambda function calling a void operation on the actual {@link #get() service client} -
   *        e.g. {@code () -> client.get().myVoidFunction()}.
   * @param resultHandler the {@link Consumer} callback {@link Consumer#accept(Object) accepting} the actual result
   *        asynchronously when available. May be {@code null} for fire and forget.
   */
  void callVoid(Runnable serviceInvoker, Consumer<Void> resultHandler);

  /**
   * @param <R> type of the result of the service operation to call.
   * @param result
   * @return CompletableFuture of type R.
   */
  <R> CompletableFuture<R> call(R result);

}
