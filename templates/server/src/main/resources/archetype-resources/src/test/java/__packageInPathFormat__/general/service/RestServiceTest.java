package ${package}.general.service;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;

import ${package}.ComponentTest;
import ${package}.SpringBootApp;

/**
 * {@link ComponentTest} for testing REST services. It runs the tests within a local server on a free randomized port.
 */
@SpringBootTest(classes = { SpringBootApp.class }, webEnvironment = WebEnvironment.RANDOM_PORT)
public abstract class RestServiceTest extends ComponentTest {

  /**
   * The port of the web server during the test.
   */
  @LocalServerPort
  protected int port;

}
