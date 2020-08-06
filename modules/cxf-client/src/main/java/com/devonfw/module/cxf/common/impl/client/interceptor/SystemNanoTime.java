package com.devonfw.module.cxf.common.impl.client.interceptor;

/**
 * Simple container to capture {@link System#nanoTime()} for performance meassure.
 */
public class SystemNanoTime {

  private final long nanoTime;

  /**
   * The constructor.
   */
  public SystemNanoTime() {

    this(System.nanoTime());
  }

  /**
   * The constructor.
   *
   * @param nanoTime - the {@link #getNanoTime() nano time}.
   */
  public SystemNanoTime(long nanoTime) {

    super();
    this.nanoTime = nanoTime;
  }

  /**
   * @return the captured {@link System#nanoTime() system nano time}.
   */
  public long getNanoTime() {

    return this.nanoTime;
  }

}
