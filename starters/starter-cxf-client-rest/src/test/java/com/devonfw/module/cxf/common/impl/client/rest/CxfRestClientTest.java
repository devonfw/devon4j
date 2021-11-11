package com.devonfw.module.cxf.common.impl.client.rest;

import javax.inject.Inject;

import net.sf.mmm.util.exception.api.ServiceInvocationFailedException;
import net.sf.mmm.util.exception.api.TechnicalErrorUserException;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

import com.devonfw.module.service.common.api.client.ServiceClientFactory;
import com.devonfw.module.service.common.api.client.config.ServiceClientConfigBuilder;
import com.devonfw.module.test.common.base.ComponentTest;
import com.devonfw.test.app.TestApplication;
import com.devonfw.test.app.myexample.service.api.rest.MyExampleRestService;
import com.devonfw.test.app.myexample.service.impl.rest.MyBusinessException;

/**
 * Test of this starter and cxf-client-rest module.
 */
@SpringBootTest(classes = TestApplication.class, webEnvironment = WebEnvironment.RANDOM_PORT)
public class CxfRestClientTest extends ComponentTest {

  @Inject
  private ServiceClientFactory serviceClientFactory;

  /**
   * Test of REST service client invoking {@link MyExampleRestService#greet(String)} on the spring-boot server via HTTP
   * on random port via CXF client.
   */
  @Test
  public void testGetWithUrlParam() {

    // given
    MyExampleRestService service = this.serviceClientFactory.create(MyExampleRestService.class,
        new ServiceClientConfigBuilder().authBasic().userLogin("admin").userPassword("admin").buildMap());
    String name = "John Doe";
    // when
    String result = service.greet(name);
    // then
    assertThat(result).isEqualTo("Hi John Doe!");
  }

  /**
   * Test of REST service client invoking {@link MyExampleRestService#businessError()} on the spring-boot server via
   * HTTP on random port via CXF client to check exception mapping.
   */
  @Test
  public void testBusinessError() {

    // given
    MyExampleRestService service = this.serviceClientFactory.create(MyExampleRestService.class,
        new ServiceClientConfigBuilder().authBasic().userLogin("admin").userPassword("admin").buildMap());
    // when
    try {
      service.businessError();
      failBecauseExceptionWasNotThrown(ServiceInvocationFailedException.class);
    } catch (ServiceInvocationFailedException e) {
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
  public void testTechnicalError() {

    // given
    MyExampleRestService service = this.serviceClientFactory.create(MyExampleRestService.class,
        new ServiceClientConfigBuilder().authBasic().userLogin("admin").userPassword("admin").buildMap());
    // when
    try {
      service.technicalError();
      failBecauseExceptionWasNotThrown(ServiceInvocationFailedException.class);
    } catch (ServiceInvocationFailedException e) {
      assertThat(e.getNlsMessage().getMessage()).matches(
          "While invoking the service com\\.devonfw\\.test\\.app\\.myexample\\.service\\.api\\.rest\\.MyExampleRestService#technicalError\\[http://localhost:[0-9]+/app/services/rest/my-example/v1/technical-error\\] the following error occurred: An unexpected error has occurred! We apologize any inconvenience\\. Please try again later\\..* Probably the service is temporary unavailable\\. Please try again later\\. If the problem persists contact your system administrator\\.");
      assertThat(e.getCode()).isEqualTo(TechnicalErrorUserException.CODE);
      assertThat(e.isForUser()).isTrue();
      assertThat(e.isTechnical()).isTrue();
      assertThat(e.getUuid()).isNotNull();
    }
  }

}
