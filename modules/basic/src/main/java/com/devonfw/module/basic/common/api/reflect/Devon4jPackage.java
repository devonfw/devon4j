package com.devonfw.module.basic.common.api.reflect;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * This class represents a {@link Package} following the
 * <a href="https://github.com/devonfw/devon4j/wiki/coding-conventions#packages">devonfw coding convetion</a>. <br>
 * After parsing a {@link Package} as {@link Devon4jPackage} you can get individual parts/segments such as
 * {@link #getComponent() compoent}, {@link #getLayer() layer}, {@link #getScope() scope}, etc.<br>
 * This is useful for advanced features and tools such as service clients and exception facades, code-generators,
 * static-code-analyzers (SonarQube plugin), etc.
 *
 * @see #of(String)
 * @see #of(Package)
 * @see #of(Class)
 *
 * @author hohwille
 */
public final class Devon4jPackage {

  /**
   * The <a href="https://github.com/devonfw/devon4j/wiki/coding-conventions#packages">common "layer"</a> for cross-cutting
   * code.
   */
  public static final String LAYER_COMMON = "common";

  /** The <a href="https://github.com/devonfw/devon4j/wiki/guide-dataaccess-layer">data-access layer</a>. */
  public static final String LAYER_DATA_ACCESS = "dataaccess";

  /** The <a href="https://github.com/devonfw/devon4j/wiki/guide-logic-layer">logic layer</a>. */
  public static final String LAYER_LOGIC = "logic";

  /** The <a href="https://github.com/devonfw/devon4j/wiki/guide-service-layer">service layer</a>. */
  public static final String LAYER_SERVICE = "service";

  /** The <a href="https://github.com/devonfw/devon4j/wiki/guide-batch-layer">batch layer</a>. */
  public static final String LAYER_BATCH = "batch";

  /**
   * The <a href="https://github.com/devonfw/devon4j/wiki/guide-client-layer">client layer</a>. Please note that devonfw does
   * not recommend to implement the client layer in Java.
   */
  public static final String LAYER_CLIENT = "client";

  /** The <a href="https://github.com/devonfw/devon4j/wiki/coding-conventions#packages">scope</a> for APIs. */
  public static final String SCOPE_API = "api";

  /**
   * The <a href="https://github.com/devonfw/devon4j/wiki/coding-conventions#packages">scope</a> for reusable base
   * implementations.
   */
  public static final String SCOPE_BASE = "base";

  /** The <a href="https://github.com/devonfw/devon4j/wiki/coding-conventions#packages">scope</a> for implementations. */
  public static final String SCOPE_IMPL = "impl";

  private static final Set<String> LAYERS = new HashSet<>(
      Arrays.asList(LAYER_BATCH, LAYER_CLIENT, LAYER_COMMON, LAYER_DATA_ACCESS, LAYER_LOGIC, LAYER_SERVICE));

  private static final Set<String> SCOPES = new HashSet<>(Arrays.asList(SCOPE_API, SCOPE_BASE, SCOPE_IMPL));

  private static final String REGEX_PKG_SEPARATOR = "\\.";

  private final String[] segments;

  private final int scopeIndex;

  private Boolean valid;

  private transient String root;

  private transient String detail;

  private transient String pkg;

  /**
   * Der Konstruktor.
   *
   * @param segments - see {@link #getSegment(int)}.
   * @param scope - see {@link #getScope()}.
   */
  private Devon4jPackage(String pkg, String[] segments, String root, String detail, int scope) {
    super();
    Objects.requireNonNull(segments, "segments");
    this.pkg = pkg;
    for (int i = 0; i < segments.length; i++) {
      if (!isValidSegment(segments[i])) {
        throw new IllegalArgumentException("segments[" + i + "] = " + segments[i]);
      }
    }
    this.root = root;
    this.detail = detail;
    this.segments = segments;
    this.scopeIndex = scope;
  }

  private static boolean isValidSegment(String segment) {

    if (segment == null) {
      return false;
    }
    if (segment.isEmpty()) {
      return false;
    }
    if (segment.indexOf('.') >= 0) {
      return false;
    }
    return true;
  }

  /**
   * @return the number of {@link #getSegment(int) package segments}.
   */
  public int getSegmentCount() {

    return this.segments.length;
  }

  /**
   * @param index the position of the requested segment. A valid index is in the range from {@code 0} to <code>
   *        {@link #getSegmentCount()}-1</code>.
   * @return the {@link Package} segment at the given index or {@code null} if the given index is invalid.
   */
  public String getSegment(int index) {

    if ((index >= 0) && (index < this.segments.length)) {
      return this.segments[index];
    }
    return null;
  }

  /**
   * @return {@code true} if this {@link Devon4jPackage} is a valid according to devonfw
   *         <a href="https://github.com/devonfw/devon4j/wiki/coding-conventions#packages">package conventions"</a>,
   *         {@code false} otherwise.
   */
  public boolean isValid() {

    if (this.valid == null) {
      this.valid = Boolean.valueOf(isValidInternal());
    }
    return this.valid;
  }

  private boolean isValidInternal() {

    if (this.segments.length < 4) {
      return false;
    }
    if (!isValidLayer()) {
      return false;
    }
    if (!isValidScope()) {
      return false;
    }
    return true;
  }

  /**
   * @return {@code true} if the {@link #getScope() scope} is valid (one of the predefined scopes {@link #isScopeApi()
   *         api}, {@link #isScopeBase() base}, or {@link #isScopeImpl() impl}).
   */
  public boolean isValidScope() {

    return SCOPES.contains(getScope());
  }

  /**
   * @return {@code true} if the {@link #getLayer() layer} is valid (one of the predefined scopes {@link #isLayerBatch()
   *         batch}, {@link #isLayerClient() client}, {@link #isLayerCommon() common}, {@link #isLayerDataAccess()
   *         dataaccess}, {@link #isLayerLogic() logic}, or {@link #isLayerService() service}).
   */
  public boolean isValidLayer() {

    return LAYERS.contains(getLayer());
  }

  /**
   * @return the root-{@link Package} of the organization or IT project owning the code.
   */
  public String getRoot() {

    if (this.root == null) {
      if (this.scopeIndex == -1) {
        return this.pkg;
      }
      int appIndex = this.scopeIndex - 3;
      if (appIndex <= 0) {
        return null;
      }
      StringBuilder sb = new StringBuilder();
      for (int i = 0; i < appIndex; i++) {
        if (i > 0) {
          sb.append('.');
        }
        sb.append(this.segments[i]);
      }
      this.root = sb.toString();
    }
    return this.root;
  }

  /**
   * @return the technical name of the application or (micro-)service.
   */
  public String getApplication() {

    return getSegment(this.scopeIndex - 3);
  }

  /**
   * @return the business component the code belongs to.
   */
  public String getComponent() {

    return getSegment(this.scopeIndex - 2);
  }

  /**
   * @return the layer the code is assigned to.
   */
  public String getLayer() {

    return getSegment(this.scopeIndex - 1);
  }

  /**
   * @return {@code true} if {@link #getLayer() layer} is {@link #LAYER_COMMON}.
   */
  public boolean isLayerCommon() {

    return LAYER_COMMON.equals(getLayer());
  }

  /**
   * @return {@code true} if {@link #getLayer() layer} is {@link #LAYER_DATA_ACCESS}.
   */
  public boolean isLayerDataAccess() {

    return LAYER_DATA_ACCESS.equals(getLayer());
  }

  /**
   * @return {@code true} if {@link #getLayer() layer} is {@link #LAYER_LOGIC}.
   */
  public boolean isLayerLogic() {

    return LAYER_LOGIC.equals(getLayer());
  }

  /**
   * @return {@code true} if {@link #getLayer() layer} is {@link #LAYER_SERVICE}.
   */
  public boolean isLayerService() {

    return LAYER_SERVICE.equals(getLayer());
  }

  /**
   * @return {@code true} if {@link #getLayer() layer} is {@link #LAYER_BATCH}.
   */
  public boolean isLayerBatch() {

    return LAYER_BATCH.equals(getLayer());
  }

  /**
   * @return {@code true} if {@link #getLayer() layer} is {@link #LAYER_CLIENT}.
   */
  public boolean isLayerClient() {

    return LAYER_CLIENT.equals(getLayer());
  }

  /**
   * @return scope the scope the code is assigned to.
   */
  public String getScope() {

    return getSegment(this.scopeIndex);
  }

  /**
   * @return {@code true} if {@link #getScope() scope} is {@link #SCOPE_API}.
   */
  public boolean isScopeApi() {

    return SCOPE_API.equals(getScope());
  }

  /**
   * @return {@code true} if {@link #getScope() scope} is {@link #SCOPE_BASE}.
   */
  public boolean isScopeBase() {

    return SCOPE_BASE.equals(getScope());
  }

  /**
   * @return {@code true} if {@link #getScope() scope} is {@link #SCOPE_IMPL}.
   */
  public boolean isScopeImpl() {

    return SCOPE_IMPL.equals(getScope());
  }

  /**
   * @return the optional detail. Can be a single segment or multiple segments separated with dot. May be {@code null}.
   */
  public String getDetail() {

    if (this.detail == null) {
      if (this.scopeIndex < 3) {
        return null;
      }
      this.detail = joinPackage(this.scopeIndex + 1);
    }
    return this.detail;
  }

  @Override
  public int hashCode() {

    return Objects.hash(this.segments, this.scopeIndex);
  }

  @Override
  public boolean equals(Object obj) {

    if (obj == this) {
      return true;
    }
    if ((obj == null) || (obj.getClass() != Devon4jPackage.class)) {
      return false;
    }
    Devon4jPackage other = (Devon4jPackage) obj;
    if (!Arrays.deepEquals(this.segments, other.segments)) {
      return false;
    }
    if (this.scopeIndex != other.scopeIndex) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {

    if (this.pkg == null) {
      this.pkg = joinPackage(0);
    }
    return this.pkg;
  }

  private String joinPackage(int start) {

    return joinPackage(start, this.segments.length);
  }

  private String joinPackage(int start, int end) {

    if (start >= end) {
      return null;
    }
    StringBuilder sb = new StringBuilder();
    for (int i = start; i < end; i++) {
      if (i > start) {
        sb.append('.');
      }
      sb.append(this.segments[i]);
    }
    return sb.toString();
  }

  /**
   * @param root - see {@link #getRoot()}.
   * @param application - see {@link #getApplication()}.
   * @param component - see {@link #getComponent()}.
   * @param layer - see {@link #getLayer()}.
   * @param scope - see {@link #getScope()}.
   * @param detail - see {@link #getDetail()}.
   * @return the {@link Devon4jPackage} for the given parameters.
   */
  public static Devon4jPackage of(String root, String application, String component, String layer, String scope,
      String detail) {

    String[] roots;
    if (root == null) {
      roots = new String[0];
    } else {
      roots = root.split(REGEX_PKG_SEPARATOR);
    }
    String[] details;
    if (detail == null) {
      details = new String[0];
    } else {
      details = detail.split(REGEX_PKG_SEPARATOR);
    }
    String[] segments = new String[roots.length + details.length + 4];
    System.arraycopy(roots, 0, segments, 0, roots.length);
    int i = roots.length;
    segments[i++] = application;
    segments[i++] = component;
    segments[i++] = layer;
    segments[i++] = scope;
    System.arraycopy(details, 0, segments, i, details.length);
    return new Devon4jPackage(null, segments, root, detail, (i - 1));
  }

  /**
   * @param packageName the {@link Package#getName() package name} to parse.
   * @return the parsed {@link Devon4jPackage} corresponding to the given package.
   */
  public static Devon4jPackage of(String packageName) {

    String[] segments = packageName.split(REGEX_PKG_SEPARATOR);
    int scopeIndex = -1;
    for (int i = 2; i < segments.length; i++) {
      if (SCOPES.contains(segments[i])) {
        scopeIndex = i;
        break;
      }
      if (LAYERS.contains(segments[i])) {
        scopeIndex = i + 1;
      }
    }
    return new Devon4jPackage(packageName, segments, null, null, scopeIndex);
  }

  /**
   * @param javaPackage the {@link Package} to parse.
   * @return the parsed {@link Devon4jPackage} corresponding to the given package.
   */
  public static Devon4jPackage of(Package javaPackage) {

    return of(javaPackage.getName());
  }

  /**
   * @param type the {@link Class} {@link Class#getPackage() located} in the {@link Package} to parse.
   * @return the parsed {@link Devon4jPackage} corresponding to the {@link Package} {@link Class#getPackage() of} the given
   *         {@link Class}.
   */
  public static Devon4jPackage of(Class<?> type) {

    return of(type.getPackage());
  }

}
