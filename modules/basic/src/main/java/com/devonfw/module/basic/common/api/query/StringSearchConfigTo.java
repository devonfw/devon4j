package com.devonfw.module.basic.common.api.query;

import com.devonfw.module.basic.common.api.to.AbstractTo;

/**
 * {@link AbstractTo TO} for the options to search for a string value.
 *
 * @since 3.0.0
 */
public class StringSearchConfigTo extends AbstractTo {

  private static final long serialVersionUID = 1L;

  private boolean ignoreCase;

  private boolean matchSubstring;

  private LikePatternSyntax likeSyntax;

  private StringSearchOperator operator;

  /**
   * @return {@code true} to ignore the case, {@code false} otherwise (to search case-sensitive).
   */
  public boolean isIgnoreCase() {

    return this.ignoreCase;
  }

  /**
   * @param ignoreCase new value of {@link #isIgnoreCase()}.
   */
  public void setIgnoreCase(boolean ignoreCase) {

    this.ignoreCase = ignoreCase;
  }

  /**
   * @return matchSubstring {@code true} if search string shall also match substrings of the string values to search on.
   */
  public boolean isMatchSubstring() {

    return this.matchSubstring;
  }

  /**
   * @param matchSubstring new value of {@link #isMatchSubstring()}.
   */
  public void setMatchSubstring(boolean matchSubstring) {

    this.matchSubstring = matchSubstring;
  }

  /**
   * @return the {@link LikePatternSyntax} of the search string used to do a LIKE-search, {@code null} for no
   *         LIKE-search. Shall be {@code null} if {@link #getOperator() operator} is neither {@code null} nor
   *         {@link StringSearchOperator#LIKE}.
   */
  public LikePatternSyntax getLikeSyntax() {

    return this.likeSyntax;
  }

  /**
   * @param likeSyntax new value of {@link #getLikeSyntax()}.
   */
  public void setLikeSyntax(LikePatternSyntax likeSyntax) {

    this.likeSyntax = likeSyntax;
  }

  /**
   * @return operator the {@link StringSearchOperator} used to search. If {@code null} a "magic auto mode" is used where
   *         {@link StringSearchOperator#LIKE} is used in case the search string contains wildcards and
   *         {@link StringSearchOperator#EQ} is used otherwise.
   */
  public StringSearchOperator getOperator() {

    return this.operator;
  }

  /**
   * @param operator new value of {@link #getOperator()}.
   */
  public void setOperator(StringSearchOperator operator) {

    this.operator = operator;
  }

  /**
   * @param operator the {@link StringSearchOperator}.
   * @return a new {@link StringSearchConfigTo} with the given config.
   */
  public static StringSearchConfigTo of(StringSearchOperator operator) {

    StringSearchConfigTo result = new StringSearchConfigTo();
    result.setOperator(operator);
    return result;
  }

  /**
   * @param syntax the {@link LikePatternSyntax}.
   * @return a new {@link StringSearchConfigTo} with the given config.
   */
  public static StringSearchConfigTo of(LikePatternSyntax syntax) {

    StringSearchConfigTo result = new StringSearchConfigTo();
    result.setOperator(StringSearchOperator.LIKE);
    result.setLikeSyntax(syntax);
    return result;
  }

}
