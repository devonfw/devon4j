package com.devonfw.example.module;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.devonfw.example.TestApplication;
import com.devonfw.module.test.common.base.ModuleTest;

/**
 * TODO
 *
 */
@ExtendWith({ SpringExtension.class })
@EmbeddedKafka(topics = { AbstractKafkaBaseTest.RETRY_TEST_TOPIC, AbstractKafkaBaseTest.TEST_TOPIC_1 })
@SpringBootTest(classes = { TestApplication.class }, webEnvironment = WebEnvironment.NONE)
@DirtiesContext
public abstract class AbstractKafkaBaseTest extends ModuleTest {

  /** Test Topic */
  public static final String TEST_TOPIC_1 = "test-topic-1";

  /** Test Topic */
  public static final String RETRY_TEST_TOPIC = "retry-test";

  /** Name of consumer group for testing */
  public static final String TEST_GROUP = "test-group";

}
