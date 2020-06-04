package com.devonfw.module.kafka.common.messaging.retry.api.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.devonfw.module.kafka.common.messaging.api.client.MessageSender;
import com.devonfw.module.kafka.common.messaging.retry.api.client.MessageRetryOperations;
import com.devonfw.module.kafka.common.messaging.retry.impl.DefaultBackOffPolicy;
import com.devonfw.module.kafka.common.messaging.retry.impl.DefaultKafkaRecordSupport;
import com.devonfw.module.kafka.common.messaging.retry.impl.DefaultRetryPolicy;
import com.devonfw.module.kafka.common.messaging.retry.impl.MessageRetryContext;
import com.devonfw.module.kafka.common.messaging.retry.impl.MessageRetryTemplate;

/**
 * This configuration class is used to create configurations beans for the {@link MessageRetryOperations}.
 *
 */
@Configuration
@EnableConfigurationProperties
public class MessageDefaultRetryConfig {

  /**
   * Creates bean for the {@link DefaultBackOffPolicyProperties}.
   *
   * @return the {@link DefaultBackOffPolicyProperties}
   */
  @Bean
  @ConfigurationProperties(prefix = "messaging.retry.back-off-policy")
  public DefaultBackOffPolicyProperties messageDefaultBackOffPolicyProperties() {

    return new DefaultBackOffPolicyProperties();
  }

  /**
   * Creates bean for the {@link DefaultRetryPolicyProperties}
   *
   * @return the {@link DefaultRetryPolicy}
   */
  @Bean
  @ConfigurationProperties(prefix = "messaging.retry.retry-policy")
  public DefaultRetryPolicyProperties messageDefaultRetryPolicyProperties() {

    return new DefaultRetryPolicyProperties();
  }

  /**
   * Creates bean for the {@link DefaultBackOffPolicy}
   *
   * @return the {@link DefaultBackOffPolicy}
   */
  @Bean
  public DefaultBackOffPolicy messageBackOffPolicy() {

    return new DefaultBackOffPolicy(messageDefaultBackOffPolicyProperties());
  }

  /**
   * Creates bean for the {@link DefaultRetryPolicy}
   *
   * @param <K> the key type
   * @param <V> the value type
   *
   * @return the {@link DefaultRetryPolicy}
   */
  @Bean
  public <K, V> DefaultRetryPolicy<K, V> messageRetryPolicy() {

    return new DefaultRetryPolicy<>(messageDefaultRetryPolicyProperties());
  }

  /**
   * Creates bean for the {@link MessageRetryContext}
   *
   * @return the {@link MessageRetryContext}
   */
  @Bean
  public MessageRetryContext messageRetryContext() {

    return new MessageRetryContext();
  }

  /**
   * Creates bean for the {@link MessageRetryTemplate}
   *
   * @param <K> the key type
   * @param <V> the value type
   *
   * @param messageDefaultRetryPolicy the {@link DefaultRetryPolicy}
   * @param messageDefaultBackOffPolicy the {@link DefaultBackOffPolicy}
   * @param messageSender the {@link MessageSender}
   * @return the {@link MessageRetryTemplate}
   */
  @Bean
  public <K, V> MessageRetryTemplate<K, V> messageDefaultRetryTemplate(
      DefaultRetryPolicy<K, V> messageDefaultRetryPolicy, DefaultBackOffPolicy messageDefaultBackOffPolicy,
      MessageSender<K, V> messageSender) {

    MessageRetryTemplate<K, V> bean = new MessageRetryTemplate<>(messageDefaultRetryPolicy,
        messageDefaultBackOffPolicy);
    bean.setMessageSender(messageSender);
    bean.setKafkaRecordSupport(messageKafkaRecordSupport());
    return bean;
  }

  /**
   * Creates bean for the {@link DefaultKafkaRecordSupport}.
   *
   * @param <K> the key type
   * @param <V> the value type
   *
   * @return the {@link DefaultKafkaRecordSupport}.
   */
  @Bean
  public <K, V> DefaultKafkaRecordSupport<K, V> messageKafkaRecordSupport() {

    return new DefaultKafkaRecordSupport<>();
  }

}
