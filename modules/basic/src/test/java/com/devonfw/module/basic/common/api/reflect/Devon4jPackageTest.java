package com.devonfw.module.basic.common.api.reflect;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.devonfw.module.test.common.base.ModuleTest;

/**
 * Test-case for {@link Devon4jPackage}.
 *
 * @author hohwille
 */
public class Devon4jPackageTest extends ModuleTest {

  /** Test of {@link Devon4jPackage#of(Class)} with {@link Devon4jPackage}. */
  @Test
  public void testOfClass() {

    Class<Devon4jPackage> type = Devon4jPackage.class;
    Devon4jPackage pkg = Devon4jPackage.of(type);
    assertThat(pkg.getRoot()).isEqualTo("com.devonfw");
    assertThat(pkg.getApplication()).isEqualTo("module");
    assertThat(pkg.getComponent()).isEqualTo("basic");
    assertThat(pkg.getLayer()).isEqualTo("common");
    assertThat(pkg.isLayerBatch()).isFalse();
    assertThat(pkg.isLayerClient()).isFalse();
    assertThat(pkg.isLayerCommon()).isTrue();
    assertThat(pkg.isLayerDataAccess()).isFalse();
    assertThat(pkg.isLayerLogic()).isFalse();
    assertThat(pkg.isLayerService()).isFalse();
    assertThat(pkg.getScope()).isEqualTo("api");
    assertThat(pkg.isScopeApi()).isTrue();
    assertThat(pkg.isScopeBase()).isFalse();
    assertThat(pkg.isScopeImpl()).isFalse();
    assertThat(pkg.getDetail()).isEqualTo("reflect");
    assertThat(pkg.toString()).isEqualTo(type.getPackage().getName());
    assertThat(pkg.isValid()).isTrue();
  }

  /** Test of {@link Devon4jPackage#of(String)} with {@code com.devonfw.module.rest.service.impl.json}. */
  @Test
  public void testOfStringDevon4jModule() {

    String packageName = "com.devonfw.module.rest.service.impl.json";
    Devon4jPackage pkg = Devon4jPackage.of(packageName);
    assertThat(pkg.getRoot()).isEqualTo("com.devonfw");
    assertThat(pkg.getApplication()).isEqualTo("module");
    assertThat(pkg.getComponent()).isEqualTo("rest");
    assertThat(pkg.getLayer()).isEqualTo("service");
    assertThat(pkg.isLayerBatch()).isFalse();
    assertThat(pkg.isLayerClient()).isFalse();
    assertThat(pkg.isLayerCommon()).isFalse();
    assertThat(pkg.isLayerDataAccess()).isFalse();
    assertThat(pkg.isLayerLogic()).isFalse();
    assertThat(pkg.isLayerService()).isTrue();
    assertThat(pkg.getScope()).isEqualTo("impl");
    assertThat(pkg.isScopeApi()).isFalse();
    assertThat(pkg.isScopeBase()).isFalse();
    assertThat(pkg.isScopeImpl()).isTrue();
    assertThat(pkg.getDetail()).isEqualTo("json");
    assertThat(pkg.toString()).isEqualTo(packageName);
    assertThat(pkg.isValid()).isTrue();
  }

  /**
   * Test of {@link Devon4jPackage#of(String)} with
   * {@code com.devonfw.gastronomy.restaurant.offermanagement.dataaccess.impl.dao}.
   */
  @Test
  public void testOfStringSampleApp() {

    String packageName = "com.devonfw.gastronomy.restaurant.offermanagement.dataaccess.base";
    Devon4jPackage pkg = Devon4jPackage.of(packageName);
    assertThat(pkg.getRoot()).isEqualTo("com.devonfw.gastronomy");
    assertThat(pkg.getApplication()).isEqualTo("restaurant");
    assertThat(pkg.getComponent()).isEqualTo("offermanagement");
    assertThat(pkg.getLayer()).isEqualTo("dataaccess");
    assertThat(pkg.isLayerBatch()).isFalse();
    assertThat(pkg.isLayerClient()).isFalse();
    assertThat(pkg.isLayerCommon()).isFalse();
    assertThat(pkg.isLayerDataAccess()).isTrue();
    assertThat(pkg.isLayerLogic()).isFalse();
    assertThat(pkg.isLayerService()).isFalse();
    assertThat(pkg.getScope()).isEqualTo("base");
    assertThat(pkg.isScopeApi()).isFalse();
    assertThat(pkg.isScopeBase()).isTrue();
    assertThat(pkg.isScopeImpl()).isFalse();
    assertThat(pkg.isScopeBase()).isTrue();
    assertThat(pkg.getDetail()).isNull();
    assertThat(pkg.toString()).isEqualTo(packageName);
    assertThat(pkg.isValid()).isTrue();
  }

  /**
   * Test of {@link Devon4jPackage#of(String, String, String, String, String, String)} with
   * {@code com.devonfw.gastronomy.restaurant.offermanagement.dataaccess.impl.dao}.
   */
  @Test
  public void testOfSegmentsSampleApp() {

    String root = "com.devonfw.gastronomy";
    String app = "restaurant";
    String component = "offermanagement";
    String layer = "dataaccess";
    String scope = "base";
    Devon4jPackage pkg = Devon4jPackage.of(root, app, component, layer, scope, null);
    assertThat(pkg.getRoot()).isEqualTo(root);
    assertThat(pkg.getApplication()).isEqualTo(app);
    assertThat(pkg.getComponent()).isEqualTo(component);
    assertThat(pkg.getLayer()).isEqualTo(layer);
    assertThat(pkg.isLayerBatch()).isFalse();
    assertThat(pkg.isLayerClient()).isFalse();
    assertThat(pkg.isLayerCommon()).isFalse();
    assertThat(pkg.isLayerDataAccess()).isTrue();
    assertThat(pkg.isLayerLogic()).isFalse();
    assertThat(pkg.isLayerService()).isFalse();
    assertThat(pkg.getScope()).isEqualTo(scope);
    assertThat(pkg.isScopeApi()).isFalse();
    assertThat(pkg.isScopeBase()).isTrue();
    assertThat(pkg.isScopeImpl()).isFalse();
    assertThat(pkg.getDetail()).isNull();
    assertThat(pkg.toString()).isEqualTo("com.devonfw.gastronomy.restaurant.offermanagement.dataaccess.base");
    assertThat(pkg.isValid()).isTrue();
  }

  /**
   * Test of {@link Devon4jPackage#of(String)} with a package-name not strictly following the conventions.
   */
  @Test
  public void testOfStringFallback() {

    String packageName = "com.company.sales.shop.offermanagement.data.api.dataaccess";
    Devon4jPackage pkg = Devon4jPackage.of(packageName);
    assertThat(pkg.getRoot()).isEqualTo("com.company.sales");
    assertThat(pkg.getApplication()).isEqualTo("shop");
    assertThat(pkg.getComponent()).isEqualTo("offermanagement");
    assertThat(pkg.getLayer()).isEqualTo("data");
    assertThat(pkg.isLayerDataAccess()).isFalse();
    assertThat(pkg.getScope()).isEqualTo("api");
    assertThat(pkg.isScopeApi()).isTrue();
    assertThat(pkg.getDetail()).isEqualTo("dataaccess");
    assertThat(pkg.toString()).isEqualTo(packageName);
    assertThat(pkg.isValid()).isFalse();
  }

  /**
   * Test of {@link Devon4jPackage#of(String)} with an invalid package.
   */
  @Test
  public void testOfStringInvalid() {

    String packageName = "java.nio.channels.spi";
    Devon4jPackage pkg = Devon4jPackage.of(packageName);
    assertThat(pkg.getRoot()).isEqualTo(packageName);
    assertThat(pkg.getApplication()).isNull();
    assertThat(pkg.getComponent()).isNull();
    assertThat(pkg.getLayer()).isNull();
    assertThat(pkg.getScope()).isNull();
    assertThat(pkg.isScopeApi()).isFalse();
    assertThat(pkg.getDetail()).isNull();
    assertThat(pkg.toString()).isEqualTo(packageName);
    assertThat(pkg.isValid()).isFalse();
  }

  /**
   * Test of {@link Devon4jPackage#of(String)} with an illegal package.
   */
  @Test
  public void testOfStringIllegal() {

    Assertions.assertThrows(IllegalArgumentException.class, () -> {
      String packageName = "...batch.api.impl";
      Devon4jPackage.of(packageName);
    });

  }

  /**
   * Test of {@link Devon4jPackage#of(Package)} with the {@link Package} of {@link Devon4jPackage} itself.
   */
  @Test
  public void testOfPackage() {

    Package javaPackage = Devon4jPackage.class.getPackage();
    Devon4jPackage pkg = Devon4jPackage.of(javaPackage);
    assertThat(pkg.getRoot()).isEqualTo("com.devonfw");
    assertThat(pkg.getApplication()).isEqualTo("module");
    assertThat(pkg.getComponent()).isEqualTo("basic");
    assertThat(pkg.getLayer()).isEqualTo("common");
    assertThat(pkg.isLayerCommon()).isTrue();
    assertThat(pkg.getScope()).isEqualTo("api");
    assertThat(pkg.isScopeApi()).isTrue();
    assertThat(pkg.getDetail()).isEqualTo("reflect");
    assertThat(pkg.toString()).isEqualTo(javaPackage.getName());
    assertThat(pkg.isValid()).isTrue();
  }

}
