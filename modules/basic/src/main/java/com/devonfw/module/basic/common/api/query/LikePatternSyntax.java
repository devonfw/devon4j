package com.devonfw.module.basic.common.api.query;

/**
 * Enum defining available syntaxes for a match pattern in a LIKE-clause. While databases typically require {@link #SQL}
 * syntax, human user expect {@link #GLOB} syntax in search forms. Therefore this enum also supports
 * {@link #convert(String, LikePatternSyntax, boolean) conversion} from one syntax to another.
 *
 * @since 3.0.0
 */
public enum LikePatternSyntax {

  /**
   * Glob syntax that is typically expected by end-users and supported by typical search forms. It uses asterisk ('*')
   * for {@link #getAny() any wildcard} and question-mark ('?') for {@link #getSingle() single wildcard}.
   */
  GLOB('*', '?'),

  /**
   * SQL syntax that is typically required by databases. It uses percent ('%') for {@link #getAny() any wildcard} and
   * underscore ('_') for {@link #getSingle() single wildcard}.
   */
  SQL('%', '_');

  /** The escape character. */
  public static final char ESCAPE = '\\';

  private final char any;

  private final char single;

  private LikePatternSyntax(char any, char single) {

    this.any = any;
    this.single = single;
  }

  /**
   * @return the wildcard character that matches any string including the {@link String#isEmpty() empty} string.
   */
  public char getAny() {

    return this.any;
  }

  /**
   * @return the wildcard character that matches exactly one single character.
   */
  public char getSingle() {

    return this.single;
  }

  /**
   * @param pattern the LIKE pattern in the given {@link LikePatternSyntax}.
   * @param syntax the {@link LikePatternSyntax} of the given {@code pattern}.
   * @return the given {@code pattern} converted to this {@link LikePatternSyntax}.
   */
  public String convert(String pattern, LikePatternSyntax syntax) {

    return convert(pattern, syntax, false);
  }

  /**
   * @param pattern the LIKE pattern in the given {@link LikePatternSyntax}.
   * @param syntax the {@link LikePatternSyntax} of the given {@code pattern}.
   * @param matchSubstring - {@code true} if the given {@code pattern} shall also match substrings, {@code false}
   *        otherwise.
   * @return the given {@code pattern} converted to this {@link LikePatternSyntax}.
   */
  public String convert(String pattern, LikePatternSyntax syntax, boolean matchSubstring) {

    if ((pattern == null) || pattern.isEmpty()) {
      if (matchSubstring) {
        return Character.toString(this.any);
      } else {
        return pattern;
      }
    }
    if (this == syntax) {
      String result = pattern;
      if (matchSubstring) {
        if (pattern.charAt(0) != this.any) {
          result = this.any + result;
        }
        int lastIndex = pattern.length() - 1;
        if ((pattern.charAt(lastIndex) != this.any) || ((lastIndex > 0) && (pattern.charAt(lastIndex - 1) == ESCAPE))) {
          result = result + this.any;
        }
      }
      return result;
    }
    int length = pattern.length();
    StringBuilder sb = new StringBuilder(length + 8);
    boolean lastWildcardAny = false;
    for (int i = 0; i < length; i++) {
      lastWildcardAny = false;
      char c = pattern.charAt(i);
      if (c == syntax.any) {
        c = this.any;
        lastWildcardAny = true;
      } else if (c == syntax.single) {
        c = this.single;
      } else if ((c == this.any) || (c == this.single) || (c == ESCAPE)) {
        if ((i == 0) && matchSubstring) {
          sb.append(this.any);
        }
        sb.append(ESCAPE);
      }
      if (matchSubstring && (i == 0) && !lastWildcardAny) {
        sb.append(this.any);
      }
      sb.append(c);
    }
    if (matchSubstring && !lastWildcardAny) {
      sb.append(this.any);
    }
    return sb.toString();
  }

  /**
   * @param pattern the string value that may be a pattern.
   * @return the {@link LikePatternSyntax} for the given {@code pattern} or {@code null} if the given {@code pattern}
   *         does not contain any wildcards.
   */
  public static LikePatternSyntax autoDetect(String pattern) {

    if ((pattern == null) || pattern.isEmpty()) {
      return null;
    }
    for (LikePatternSyntax syntax : values()) {
      if (pattern.indexOf(syntax.any) > 0) {
        return syntax;
      } else if (pattern.indexOf(syntax.single) > 0) {
        return syntax;
      }
    }
    return null;
  }

}
