package ${package}.general.common.base;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

import com.devonfw.module.basic.common.api.reflect.Devon4jPackage;
import com.devonfw.module.test.common.base.ModuleTest;
import com.example.application.sampleapp.SpringBootApp;
import com.example.application.sampleapp.general.common.base.Devon4jPackageCheckTest;

/**
 * This test verifies that {@link SpringBootApp} is able to startup.
 */
public class ApplicationTest extends ApplicationComponentTest {
  @Test
  public void contextLoads() {

    Devon4jPackage pkg = Devon4jPackage.of(Devon4jPackageCheckTest.class);
    assertThat(pkg.isValid()).isTrue();
  }
}
