package ${package}.general.common.base;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

import com.devonfw.module.test.common.base.ModuleTest;
import com.example.application.sampleapp.SpringBootApp;

/**
 * This test verifies that {@link SpringBootApp} is able to startup.
 */
public class ApplicationTest extends ApplicationComponentTest {
  @Test
  public void contextLoads() {
    // just ensure the app can startup
    SpringBootApp.main(new String[] {});
  }
}
