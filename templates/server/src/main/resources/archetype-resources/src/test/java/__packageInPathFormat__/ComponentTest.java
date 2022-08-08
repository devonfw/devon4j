package ${package};

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import jakarta.inject.Inject;

/**
 * Abstract base class for component tests of this application.
 */
@ExtendWith(SpringExtension.class)
@TestExecutionListeners({ TransactionalTestExecutionListener.class, DependencyInjectionTestExecutionListener.class })
@SpringBootTest(classes = { SpringBootApp.class }, webEnvironment = WebEnvironment.MOCK)
public abstract class ComponentTest extends BaseTest {

  @Inject
  @javax.inject.Named
  private Flyway flyway;

  @Override
  protected void doSetUp() {

    super.doSetUp();
    resetDb();
  }

  /**
   * Override to disable or to only run on {@link #isInitialSetup() initial setup}.
   */
  protected void resetDb() {

    this.flyway.clean();
    this.flyway.migrate();
  }

  @Override
  protected void doTearDown() {

    super.doTearDown();
    logout();
  }

  /**
   * @param login the id of the user to run the test as.
   * @param permissions the permissions for the test.
   */
  protected void login(String login, String... permissions) {

    Authentication authentication = new TestingAuthenticationToken(login, login, permissions);
    SecurityContextHolder.getContext().setAuthentication(authentication);
  }

  /**
   * Logs off any previously logged on user.
   */
  protected void logout() {

    SecurityContextHolder.getContext().setAuthentication(null);
  }

}
