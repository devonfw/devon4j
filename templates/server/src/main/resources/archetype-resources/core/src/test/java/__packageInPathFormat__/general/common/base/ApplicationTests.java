package ${package}.general.common.base;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

import com.devonfw.module.test.common.base.ModuleTest;

/**
 * This test verifies that the entire code of SpringBootApp.
 *
 */
@SpringBootTest(classes = SpringBootApp.class, webEnvironment = WebEnvironment.MOCK)
public class ApplicationTests extends ModuleTest {
  @Test
  public void contextLoads() {

  }
}
