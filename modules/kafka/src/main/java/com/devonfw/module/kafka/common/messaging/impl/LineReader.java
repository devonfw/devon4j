package com.devonfw.module.kafka.common.messaging.impl;

/**
 * @author ravicm
 *
 */
public class LineReader {

  private int currentIndex = 0;

  private String input;

  /**
   * The constructor.
   *
   * @param input
   */
  public LineReader(String input) {

    this.input = input;
  }

  /**
   * @return
   */
  public String readLine() {

    if (this.input == null || this.currentIndex > this.input.length()) {
      return null;
    }

    String result = null;
    int endIndex = this.input.indexOf('\n', this.currentIndex);

    if (endIndex < 0) {
      return getRemaining();
    }

    result = this.input.substring(this.currentIndex, endIndex);
    this.currentIndex = endIndex + 1;

    return result;
  }

  /**
   * @return
   */
  public String getRemaining() {

    String result = this.input.substring(this.currentIndex);
    this.currentIndex = this.input.length();
    return result;
  }

  /**
   * @return
   */
  public boolean isReadComplete() {

    return this.currentIndex >= this.input.length();
  }

  /**
   *
   */
  public void reset() {

    this.currentIndex = 0;
  }

  /**
   * @return
   */
  public int getCurrentIndex() {

    return this.currentIndex;
  }
}
