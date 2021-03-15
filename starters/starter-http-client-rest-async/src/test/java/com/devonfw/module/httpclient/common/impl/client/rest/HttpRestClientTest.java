package com.devonfw.module.httpclient.common.impl.client.rest;

import java.time.LocalDate;
import java.util.concurrent.CompletableFuture;

import javax.inject.Inject;

import net.sf.mmm.util.exception.api.ServiceInvocationFailedException;
import net.sf.mmm.util.exception.api.TechnicalErrorUserException;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

import com.devonfw.module.service.common.api.client.AsyncServiceClient;
import com.devonfw.module.service.common.api.client.ServiceClientFactory;
import com.devonfw.module.service.common.api.client.config.ServiceClientConfigBuilder;
import com.devonfw.module.test.common.base.ComponentTest;
import com.devonfw.test.app.TestApplication;
import com.devonfw.test.app.myexample.service.api.rest.MyExampleRestService;
import com.devonfw.test.app.myexample.service.api.rest.MyExampleTo;
import com.devonfw.test.app.myexample.service.impl.rest.MyBusinessException;

/**
 * Test of this starter and http-client-rest module.
 */
@SpringBootTest(classes = TestApplication.class, webEnvironment = WebEnvironment.RANDOM_PORT)
public class HttpRestClientTest extends ComponentTest {

  @Inject
  private ServiceClientFactory serviceClientFactory;

  /**
   * Test of REST service client invoking {@link MyExampleRestService#greet(String)} on the spring-boot server via HTTP
   * on random port via CXF client.
   */
  @Test
  public void testGetWithUrlParam() {

    // given
    AsyncServiceClient<MyExampleRestService> serviceClient = this.serviceClientFactory.createAsync(
        MyExampleRestService.class,
        new ServiceClientConfigBuilder().authBasic().userLogin("admin").userPassword("admin").buildMap());
    String name = "John Doe & ?#";
    // when
    TestResultHandler<String> resultHandler = new TestResultHandler<>(serviceClient);
    serviceClient.setErrorHandler(resultHandler.getErrorHandler());
    serviceClient.call(serviceClient.get().greet(name), resultHandler);
    // then
    assertThat(resultHandler.getResponseOrWait()).isEqualTo("Hi John Doe & ?#!");
  }

  /**
   * Test of {@link ServiceClientFactory#createAsync(Class, java.util.Map)} with {@link AsyncServiceClient#call(Object)}
   * testing {@link CompletableFuture} using a HTTP GET operation.
   *
   * @throws Exception on error
   */
  @Test
  public void testGetWithUrlParamForCompletableFuture() throws Exception {

    // given
    AsyncServiceClient<MyExampleRestService> serviceClient = this.serviceClientFactory.createAsync(
        MyExampleRestService.class,
        new ServiceClientConfigBuilder().authBasic().userLogin("admin").userPassword("admin").buildMap());
    String name = "John Doe & ?#";

    // when
    CompletableFuture<String> future = serviceClient.call(serviceClient.get().greet(name));
    // then
    assertThat(future.get()).isEqualTo("Hi John Doe & ?#!");
  }

  /**
   * Test of REST service client invoking
   * {@link MyExampleRestService#saveExample(com.devonfw.test.app.myexample.service.api.rest.MyExampleTo)} on the
   * spring-boot server via HTTP on random port via CXF client.
   */
  @Test
  public void testPostWithTo() {

    // given
    AsyncServiceClient<MyExampleRestService> serviceClient = this.serviceClientFactory.createAsync(
        MyExampleRestService.class,
        new ServiceClientConfigBuilder().authBasic().userLogin("admin").userPassword("admin").buildMap());
    MyExampleTo data = new MyExampleTo();
    data.setName("John Doe");
    data.setBirthday(LocalDate.of(1999, 12, 31));

    // when
    TestResultHandler<MyExampleTo> resultHandler = new TestResultHandler<>(serviceClient);
    serviceClient.setErrorHandler(resultHandler.getErrorHandler());
    serviceClient.call(serviceClient.get().saveExample(data), resultHandler);
    // then
    MyExampleTo response = resultHandler.getResponseOrWait();
    assertThat(response.getName()).isEqualTo("John Doe-saved");
    assertThat(response.getBirthday()).isEqualTo(LocalDate.of(2000, 1, 1));
  }

  /**
   * Test of {@link ServiceClientFactory#createAsync(Class, java.util.Map)} with {@link AsyncServiceClient#call(Object)}
   * testing {@link CompletableFuture} using a HTTP POST operation.
   *
   * @throws Exception on error
   */
  @Test
  public void testPostWithToForCompletableFuture() throws Exception {

    // given
    AsyncServiceClient<MyExampleRestService> serviceClient = this.serviceClientFactory.createAsync(
        MyExampleRestService.class,
        new ServiceClientConfigBuilder().authBasic().userLogin("admin").userPassword("admin").buildMap());
    MyExampleTo data = new MyExampleTo();
    data.setName("John Doe");
    data.setBirthday(LocalDate.of(1999, 12, 31));

    // when
    CompletableFuture<MyExampleTo> future = serviceClient.call(serviceClient.get().saveExample(data));
    // then
    MyExampleTo response;
    response = future.get();
    assertThat(response.getName()).isEqualTo("John Doe-saved");
    assertThat(response.getBirthday()).isEqualTo(LocalDate.of(2000, 1, 1));
  }

  /**
   * Test of REST service client invoking {@link MyExampleRestService#businessError()} on the spring-boot server via
   * HTTP on random port via CXF client to check exception mapping.
   */
  @Test
  public void testBusinessError() {

    // given
    AsyncServiceClient<MyExampleRestService> serviceClient = this.serviceClientFactory.createAsync(
        MyExampleRestService.class,
        new ServiceClientConfigBuilder().authBasic().userLogin("admin").userPassword("admin").buildMap());
    // when
    TestResultHandler<Void> resultHandler = new TestResultHandler<>(serviceClient);
    serviceClient.setErrorHandler(resultHandler.getErrorHandler());
    serviceClient.callVoid(() -> {
      serviceClient.get().businessError();
    }, resultHandler);
    // then
    Throwable error = resultHandler.getErrorOrWait();
    assertThat(error).isInstanceOf(ServiceInvocationFailedException.class);
    ServiceInvocationFailedException e = (ServiceInvocationFailedException) error;
    assertThat(e.getNlsMessage().getMessage()).matches(
        "While invoking the service com\\.devonfw\\.test\\.app\\.myexample\\.service\\.api\\.rest\\.MyExampleRestService#businessError\\[http://localhost:[0-9]+/app/services/rest/my-example/v1/business-error\\] the following error occurred: Test of business error.* Probably the service is temporary unavailable\\. Please try again later\\. If the problem persists contact your system administrator\\.");
    assertThat(e.getCode()).isEqualTo(MyBusinessException.CODE);
    assertThat(e.isForUser()).isTrue();
    assertThat(e.isTechnical()).isTrue();
    assertThat(e.getUuid()).isNotNull();
  }

  /**
   * Test of REST service client invoking {@link MyExampleRestService#businessError()} on the spring-boot server via
   * HTTP on random port via CXF client to check exception mapping.
   */
  @Test
  public void testTechnicalError() {

    // given
    AsyncServiceClient<MyExampleRestService> serviceClient = this.serviceClientFactory.createAsync(
        MyExampleRestService.class,
        new ServiceClientConfigBuilder().authBasic().userLogin("admin").userPassword("admin").buildMap());
    // when
    TestResultHandler<Void> resultHandler = new TestResultHandler<>(serviceClient);
    serviceClient.setErrorHandler(resultHandler.getErrorHandler());
    serviceClient.callVoid(() -> {
      serviceClient.get().technicalError();
    }, resultHandler);
    // then
    Throwable error = resultHandler.getErrorOrWait();
    assertThat(error).isInstanceOf(ServiceInvocationFailedException.class);
    ServiceInvocationFailedException e = (ServiceInvocationFailedException) error;
    assertThat(e.getNlsMessage().getMessage()).matches(
        "While invoking the service com\\.devonfw\\.test\\.app\\.myexample\\.service\\.api\\.rest\\.MyExampleRestService#technicalError\\[http://localhost:[0-9]+/app/services/rest/my-example/v1/technical-error\\] the following error occurred: An unexpected error has occurred! We apologize any inconvenience\\. Please try again later\\..* Probably the service is temporary unavailable\\. Please try again later\\. If the problem persists contact your system administrator\\.");
    assertThat(e.getCode()).isEqualTo(TechnicalErrorUserException.CODE);
    assertThat(e.isForUser()).isTrue();
    assertThat(e.isTechnical()).isTrue();
    assertThat(e.getUuid()).isNotNull();
  }

  /**
   * Test of REST service client invoking {@link MyExampleRestService#businessError()} on the spring-boot server via
   * HTTP on random port via CXF client to check exception mapping.
   */
  @Test
  public void testBusinessErrorForCompletableFuture() {

    // given
    AsyncServiceClient<MyExampleRestService> serviceClient = this.serviceClientFactory.createAsync(
        MyExampleRestService.class,
        new ServiceClientConfigBuilder().authBasic().userLogin("admin").userPassword("admin").buildMap());
    // when
    CompletableFuture<Void> future = serviceClient.callVoid(() -> {
      serviceClient.get().businessError();
    });
    // then
    try {
      future.get();
      failBecauseExceptionWasNotThrown(ServiceInvocationFailedException.class);
    } catch (Throwable executionEx) {
      Throwable error = executionEx.getCause();
      assertThat(error).isInstanceOf(ServiceInvocationFailedException.class);
      ServiceInvocationFailedException e = (ServiceInvocationFailedException) error;
      assertThat(e.getNlsMessage().getMessage()).matches(
          "While invoking the service com\\.devonfw\\.test\\.app\\.myexample\\.service\\.api\\.rest\\.MyExampleRestService#businessError\\[http://localhost:[0-9]+/app/services/rest/my-example/v1/business-error\\] the following error occurred: Test of business error.* Probably the service is temporary unavailable\\. Please try again later\\. If the problem persists contact your system administrator\\.");
      assertThat(e.getCode()).isEqualTo(MyBusinessException.CODE);
      assertThat(e.isForUser()).isTrue();
      assertThat(e.isTechnical()).isTrue();
      assertThat(e.getUuid()).isNotNull();
    }
  }

  /**
   * Test of REST service client invoking {@link MyExampleRestService#businessError()} on the spring-boot server via
   * HTTP on random port via CXF client to check exception mapping.
   */
  @Test
  public void testTechnicalErrorForCompletableFuture() {

    // given
    AsyncServiceClient<MyExampleRestService> serviceClient = this.serviceClientFactory.createAsync(
        MyExampleRestService.class,
        new ServiceClientConfigBuilder().authBasic().userLogin("admin").userPassword("admin").buildMap());
    // when
    CompletableFuture<Void> future = serviceClient.callVoid(() -> {
      serviceClient.get().technicalError();
    });
    // then
    try {
      future.get();
      failBecauseExceptionWasNotThrown(ServiceInvocationFailedException.class);
    } catch (Throwable executionEx) {
      Throwable error = executionEx.getCause();
      assertThat(error).isInstanceOf(ServiceInvocationFailedException.class);
      ServiceInvocationFailedException e = (ServiceInvocationFailedException) error;
      assertThat(e.getNlsMessage().getMessage()).matches(
          "While invoking the service com\\.devonfw\\.test\\.app\\.myexample\\.service\\.api\\.rest\\.MyExampleRestService#technicalError\\[http://localhost:[0-9]+/app/services/rest/my-example/v1/technical-error\\] the following error occurred: An unexpected error has occurred! We apologize any inconvenience\\. Please try again later\\..* Probably the service is temporary unavailable\\. Please try again later\\. If the problem persists contact your system administrator\\.");
      assertThat(e.getCode()).isEqualTo(TechnicalErrorUserException.CODE);
      assertThat(e.isForUser()).isTrue();
      assertThat(e.isTechnical()).isTrue();
      assertThat(e.getUuid()).isNotNull();
    }
  }

  /**
   * Test of {@link ServiceClientFactory#createAsync(Class, java.util.Map)} with
   * {@link AsyncServiceClient#call(Object, java.util.function.Consumer)} testing callback usage to retrieve a primitive
   * {@code boolean} result and conversion to {@link Boolean}.
   */
  @Test
  public void testPrimitiveResult() {

    // given
    AsyncServiceClient<MyExampleRestService> serviceClient = this.serviceClientFactory.createAsync(
        MyExampleRestService.class,
        new ServiceClientConfigBuilder().authBasic().userLogin("admin").userPassword("admin").buildMap());
    // when
    TestResultHandler<Boolean> resultHandler = new TestResultHandler<>(serviceClient);
    serviceClient.setErrorHandler(resultHandler.getErrorHandler());
    serviceClient.call(serviceClient.get().primitiveResult(), resultHandler);
    // then
    assertThat(resultHandler.getResponseOrWait()).isTrue();
  }

  /**
   * Test of {@link ServiceClientFactory#createAsync(Class, java.util.Map)} with {@link AsyncServiceClient#call(Object)}
   * testing {@link CompletableFuture} with a primitive {@code boolean} result and conversion to {@link Boolean}.
   *
   * @throws Exception on error
   */
  @Test
  public void testPrimitiveResultForCompletableFuture() throws Exception {

    // given
    AsyncServiceClient<MyExampleRestService> serviceClient = this.serviceClientFactory.createAsync(
        MyExampleRestService.class,
        new ServiceClientConfigBuilder().authBasic().userLogin("admin").userPassword("admin").buildMap());
    // when
    CompletableFuture<Boolean> future = serviceClient.call(serviceClient.get().primitiveResult());
    // then
    assertThat(future.get()).isTrue();
  }
}
