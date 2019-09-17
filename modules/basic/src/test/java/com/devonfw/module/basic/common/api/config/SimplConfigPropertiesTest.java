package com.devonfw.module.basic.common.api.config;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.assertj.core.data.MapEntry;
import org.junit.jupiter.api.Test;

import com.devonfw.module.basic.common.api.config.ConfigProperties;
import com.devonfw.module.basic.common.api.config.EmptyConfigProperties;
import com.devonfw.module.basic.common.api.config.SimpleConfigProperties;
import com.devonfw.module.test.common.base.ModuleTest;

/**
 * Test of {@link SimpleConfigProperties}.
 */
public class SimplConfigPropertiesTest extends ModuleTest {

  /**
   * Test of {@link SimpleConfigProperties#ofFlatMap(String, Map)}.
   */
  @Test
  public void testOfFlatMap() {

    // given
    Map<String, String> map = new HashMap<>();
    map.put("foo.bar.some", "some-value");
    map.put("foo.bar.other", "other-value");
    map.put("foo", "foo-value");
    map.put("bar", "bar-value");
    map.put("", "value");

    // when
    ConfigProperties rootConfig = SimpleConfigProperties.ofFlatMap("root", map);

    // then
    assertThat(rootConfig).isNotNull();
    assertThat(rootConfig.isEmpty()).isFalse();
    assertThat(rootConfig.getValue()).isEqualTo("value");
    assertThat(rootConfig.toString()).isEqualTo("root=value");
    assertThat(rootConfig.getChildKeys()).containsOnly("foo", "bar");
    assertThat(rootConfig.getChild("foo", "bar").getChildKeys()).containsOnly("some", "other");
    assertThat(rootConfig.getChild("undefined")).isNotNull().isInstanceOf(EmptyConfigProperties.class);
    for (Entry<String, String> entry : map.entrySet()) {
      String key = entry.getKey();
      assertThat(rootConfig.getChildValue(key)).as(key).isEqualTo(entry.getValue());
    }
    assertThat(rootConfig.toFlatMap()).isEqualTo(map);
  }

  /**
   * Test of {@link SimpleConfigProperties#ofHierarchicalMap(String, Map)}.
   */
  @Test
  public void testOfHierarchicalMap() {

    // given
    Map<String, Object> fooMap = createMap("foo-value");
    Map<String, Object> fooBarMap = new HashMap<>();
    fooMap.put("bar", fooBarMap);
    fooBarMap.put("some", createMap("some-value"));
    fooBarMap.put("other", createMap("other-value"));
    Map<String, Object> barMap = createMap("bar-value");

    Map<String, Object> map = createMap("value");
    map.put("foo", fooMap);
    map.put("bar", barMap);

    // when
    ConfigProperties rootConfig = SimpleConfigProperties.ofHierarchicalMap("root", map);

    // then
    assertThat(rootConfig).isNotNull();
    assertThat(rootConfig.isEmpty()).isFalse();
    assertThat(rootConfig.getValue()).isEqualTo("value");
    assertThat(rootConfig.toString()).isEqualTo("root=value");
    assertThat(rootConfig.getChildKeys()).containsOnly("foo", "bar");
    assertThat(rootConfig.getChild("foo", "bar").getChildKeys()).containsOnly("some", "other");
    assertThat(rootConfig.getChild("undefined")).isNotNull().isInstanceOf(EmptyConfigProperties.class);
    assertThat(rootConfig.toHierarchicalMap()).isEqualTo(map);
    for (Entry<String, String> entry : rootConfig.toFlatMap().entrySet()) {
      String key = entry.getKey();
      assertThat(rootConfig.getChildValue(key)).as(key).isEqualTo(entry.getValue());
    }
  }

  /**
   * Test of {@link SimpleConfigProperties#inherit(ConfigProperties)}.
   */
  @Test
  public void testInherit() {

    // given
    Map<String, String> map = new HashMap<>();
    map.put("app.foo.url", "http://foo.domain.com");
    map.put("app.bar.url", "http://bar.domain.com");
    map.put("app.bar.user.login", "bar-user");
    map.put("default.url", "http://api.domain.com");
    map.put("default.user.login", "api");

    // when
    ConfigProperties rootConfig = SimpleConfigProperties.ofFlatMap(map);
    ConfigProperties defaultConfig = rootConfig.getChild("default");
    ConfigProperties fooAppConfig = rootConfig.getChild("app", "foo");
    ConfigProperties fooConfig = fooAppConfig.inherit(defaultConfig);
    ConfigProperties barAppConfig = rootConfig.getChild("app", "bar");
    ConfigProperties barConfig = barAppConfig.inherit(defaultConfig);

    // then
    assertThat(fooConfig).isNotNull();
    assertThat(fooConfig.toFlatMap()).containsOnly(MapEntry.entry("url", "http://foo.domain.com"),
        MapEntry.entry("user.login", "api"));
    fooConfig = fooAppConfig.inherit(defaultConfig); // test lazy init
    assertThat(fooConfig.getChildKeys()).containsOnly("url", "user");
    fooConfig = fooAppConfig.inherit(defaultConfig); // test lazy init
    assertThat(fooConfig.getChildValue("url")).isEqualTo("http://foo.domain.com");
    assertThat(fooConfig.getChildValue("user", "login")).isEqualTo("api");
    assertThat(barConfig).isNotNull();
    assertThat(barConfig.toFlatMap()).containsOnly(MapEntry.entry("url", "http://bar.domain.com"),
        MapEntry.entry("user.login", "bar-user"));
    barConfig = barAppConfig.inherit(defaultConfig); // test lazy init
    assertThat(barConfig.getChildKeys()).containsOnly("url", "user");
    barConfig = barAppConfig.inherit(defaultConfig); // test lazy init
    assertThat(barConfig.getChildValue("url")).isEqualTo("http://bar.domain.com");
    assertThat(barConfig.getChildValue("user", "login")).isEqualTo("bar-user");
  }

  private static Map<String, Object> createMap(String value) {

    Map<String, Object> map = new HashMap<>();
    map.put("", value);
    return map;
  }

}
