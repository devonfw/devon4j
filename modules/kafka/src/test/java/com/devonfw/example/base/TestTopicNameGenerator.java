package com.devonfw.example.base;

/**
 * This class is used to generate the topic names using {@link #generateTopicNames(int)}.
 *
 */
public class TestTopicNameGenerator {

  private static int nextTopicNumber = 1;

  private static final String TOPIC_NAME_PREFIX = "Test-Topic-";

  private TestTopicNameGenerator() {

  }

  /**
   * This method is used to create the topic names.
   *
   * @param count the no.of topics.
   * @return String[] of topics.
   */
  public static String[] generateTopicNames(int count) {

    String[] result = new String[count];
    for (int i = 0; i < count; i++) {
      result[i] = TOPIC_NAME_PREFIX + Integer.toString(nextTopicNumber + i);
    }
    return result;
  }

  /**
   * @return the latest topic name.
   */
  public static String nextTopicName() {

    return TOPIC_NAME_PREFIX + Integer.toString(nextTopicNumber++);
  }
}
