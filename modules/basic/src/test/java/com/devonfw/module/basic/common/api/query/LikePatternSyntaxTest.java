package com.devonfw.module.basic.common.api.query;

import org.junit.Test;

import com.devonfw.module.basic.common.api.query.LikePatternSyntax;
import com.devonfw.module.test.common.base.ModuleTest;

/**
 * Test of {@link LikePatternSyntax}.
 */
public class LikePatternSyntaxTest extends ModuleTest {

  /** Basic test of {@link LikePatternSyntax#GLOB}. */
  @Test
  public void testGlob() {

    LikePatternSyntax syntax = LikePatternSyntax.GLOB;
    assertThat(syntax.getAny()).isEqualTo('*');
    assertThat(syntax.getSingle()).isEqualTo('?');
  }

  /** Basic test of {@link LikePatternSyntax#SQL}. */
  @Test
  public void testSql() {

    LikePatternSyntax syntax = LikePatternSyntax.SQL;
    assertThat(syntax.getAny()).isEqualTo('%');
    assertThat(syntax.getSingle()).isEqualTo('_');
  }

  /**
   * Test of {@link LikePatternSyntax#convert(String, LikePatternSyntax, boolean)} from {@link LikePatternSyntax#GLOB}
   * to {@link LikePatternSyntax#SQL}.
   */
  @Test
  public void testGlob2Sql() {

    LikePatternSyntax syntax = LikePatternSyntax.GLOB;
    assertThat(LikePatternSyntax.SQL.convert("", syntax)).isEqualTo("");
    assertThat(LikePatternSyntax.SQL.convert("*", syntax)).isEqualTo("%");
    assertThat(LikePatternSyntax.SQL.convert("?", syntax)).isEqualTo("_");
    assertThat(LikePatternSyntax.SQL.convert("a*b?c", syntax)).isEqualTo("a%b_c");
    assertThat(LikePatternSyntax.SQL.convert("*10% key_loss*", syntax)).isEqualTo("%10\\% key\\_loss%");

    assertThat(LikePatternSyntax.SQL.convert("a", syntax, true)).isEqualTo("%a%");
    assertThat(LikePatternSyntax.SQL.convert("*", syntax, true)).isEqualTo("%");
    assertThat(LikePatternSyntax.SQL.convert("a*b?c", syntax, true)).isEqualTo("%a%b_c%");
  }

  /**
   * Test of {@link LikePatternSyntax#convert(String, LikePatternSyntax, boolean)} from {@link LikePatternSyntax#SQL} to
   * {@link LikePatternSyntax#GLOB}.
   */
  @Test
  public void testSql2Glob() {

    LikePatternSyntax syntax = LikePatternSyntax.SQL;
    assertThat(LikePatternSyntax.GLOB.convert("", syntax)).isEqualTo("");
    assertThat(LikePatternSyntax.GLOB.convert("%", syntax)).isEqualTo("*");
    assertThat(LikePatternSyntax.GLOB.convert("_", syntax)).isEqualTo("?");
    assertThat(LikePatternSyntax.GLOB.convert("a%b_c", syntax)).isEqualTo("a*b?c");
    assertThat(LikePatternSyntax.GLOB.convert("%10* key?loss%", syntax)).isEqualTo("*10\\* key\\?loss*");

    assertThat(LikePatternSyntax.GLOB.convert("a", syntax, true)).isEqualTo("*a*");
    assertThat(LikePatternSyntax.GLOB.convert("%", syntax, true)).isEqualTo("*");
    assertThat(LikePatternSyntax.GLOB.convert("a%b_c", syntax, true)).isEqualTo("*a*b?c*");
  }

  /** Test of {@link LikePatternSyntax#autoDetect(String)}. */
  @Test
  public void testAutoDetect() {

    assertThat(LikePatternSyntax.autoDetect(null)).isEqualTo(null);
    assertThat(LikePatternSyntax.autoDetect("")).isEqualTo(null);
    assertThat(LikePatternSyntax.autoDetect("a")).isEqualTo(null);
    assertThat(LikePatternSyntax.autoDetect("aBc")).isEqualTo(null);
    assertThat(LikePatternSyntax.autoDetect("a*b")).isEqualTo(LikePatternSyntax.GLOB);
    assertThat(LikePatternSyntax.autoDetect("a?b")).isEqualTo(LikePatternSyntax.GLOB);
    assertThat(LikePatternSyntax.autoDetect("a%b")).isEqualTo(LikePatternSyntax.SQL);
    assertThat(LikePatternSyntax.autoDetect("a_b")).isEqualTo(LikePatternSyntax.SQL);
  }

}
