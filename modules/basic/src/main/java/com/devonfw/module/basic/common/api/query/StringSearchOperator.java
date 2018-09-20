package com.devonfw.module.basic.common.api.query;

/**
 * Enum defining available operators for a string search or string comparison.
 *
 * @since 3.0.0
 */
public enum StringSearchOperator {

  /** Matches if strings are {@link String#equals(Object) equal}. */
  EQ("=="),

  /** Matches if strings are NOT {@link String#equals(Object) equal}. */
  NE("!="),

  /** Matches if search value is less than search hit(s) in {@link String#compareTo(String) lexicographical order}. */
  LT("<"),

  /**
   * Matches if search value is less or equal to search hit(s) in {@link String#compareTo(String) lexicographical
   * order}.
   */
  LE("<="),

  /**
   * Matches if search value is greater than search hit(s) in {@link String#compareTo(String) lexicographical order}.
   */
  GT(">"),

  /**
   * Matches if search value is greater or equal to search hit(s) in {@link String#compareTo(String) lexicographical
   * order}.
   */
  GE(">="),

  /**
   * Matches if search value as pattern matches search hit(s) in <em>LIKE</em> search.
   *
   * @see LikePatternSyntax
   */
  LIKE("LIKE"),

  /**
   * Matches if search value as pattern does not match search hit(s) in <em>LIKE</em> search.
   *
   * @see LikePatternSyntax
   */
  NOT_LIKE("NOT LIKE");

  private final String operator;

  private StringSearchOperator(String operator) {

    this.operator = operator;
  }

  @Override
  public String toString() {

    return this.operator;
  }

}
