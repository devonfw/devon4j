package com.devonfw.example.base;

/**
 * @author ravicm
 *
 */
public class TestTopicNameGenerator {

  private static int nextTopicNumber = 1;

  private static final String TOPIC_NAME_PREFIX = "Test-Topic-";

  private TestTopicNameGenerator() {

  }

  /**
   * @param count
   * @return
   */
  public static String[] generateTopicNames(int count) {

    String[] result = new String[count];
    for (int i = 0; i < count; i++) {
      result[i] = TOPIC_NAME_PREFIX + Integer.toString(nextTopicNumber + i);
    }
    return result;
  }

  /**
   * @return
   */
  public static String nextTopicName() {

    return TOPIC_NAME_PREFIX + Integer.toString(nextTopicNumber++);
  }
}
