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
  @ConfigurationProperties(prefix = "messaging.retry.default.back-off-policy")
  public DefaultBackOffPolicyProperties messageDefaultBackOffPolicyProperties() {

    return new DefaultBackOffPolicyProperties();
  }

  /**
   * Creates bean for the {@link DefaultRetryPolicyProperties}
   *
   * @return the {@link DefaultRetryPolicy}
   */
  @Bean
  @ConfigurationProperties(prefix = "messaging.retry.default.retry-policy")
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
   * @return the {@link DefaultRetryPolicy}
   */
  @Bean
  public DefaultRetryPolicy messageRetryPolicy() {

    return new DefaultRetryPolicy(messageDefaultRetryPolicyProperties());
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
   * @param messageDefaultRetryPolicy the {@link DefaultRetryPolicy}
   * @param messageDefaultBackOffPolicy the {@link DefaultBackOffPolicy}
   * @param messageSender the {@link MessageSender}
   * @return the {@link MessageRetryTemplate}
   */
  @Bean
  public MessageRetryTemplate<?, ?> messageDefaultRetryTemplate(DefaultRetryPolicy messageDefaultRetryPolicy,
      DefaultBackOffPolicy messageDefaultBackOffPolicy, MessageSender<Object, Object> messageSender) {

    MessageRetryTemplate<Object, Object> bean = new MessageRetryTemplate<>(messageDefaultRetryPolicy,
        messageDefaultBackOffPolicy);
    bean.setMessageSender(messageSender);
    bean.setKafkaRecordSupport(messageKafkaRecordSupport());
    return bean;
  }

  /**
   * Creates bean for the {@link DefaultKafkaRecordSupport}.
   *
   * @return the {@link DefaultKafkaRecordSupport}.
   */
  @Bean
  public DefaultKafkaRecordSupport<Object, Object> messageKafkaRecordSupport() {

    return new DefaultKafkaRecordSupport<>();
  }

}
