package com.devonfw.module.kafka.common.messaging.retry.api.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.devonfw.module.kafka.common.messaging.api.client.MessageSender;
import com.devonfw.module.kafka.common.messaging.retry.api.client.MessageBackOffPolicy;
import com.devonfw.module.kafka.common.messaging.retry.api.client.MessageRetryPolicy;
import com.devonfw.module.kafka.common.messaging.retry.impl.DefaultBackOffPolicy;
import com.devonfw.module.kafka.common.messaging.retry.impl.DefaultRetryPolicy;
import com.devonfw.module.kafka.common.messaging.retry.impl.MessageRetryTemplate;

/**
 * @author ravicm
 *
 */
@Configuration
@EnableConfigurationProperties
public class MessageDefaultRetryConfig {

  /**
   * @return
   */
  @Bean
  @ConfigurationProperties(prefix = "messaging.retry.default.back-off-policy")
  public DefaultBackOffPolicyProperties messageDefaultBackOffPolicyProperties() {

    return new DefaultBackOffPolicyProperties();
  }

  /**
   * @return
   */
  @Bean
  @ConfigurationProperties(prefix = "messaging.retry.default.retry-policy")
  public DefaultRetryPolicyProperties messageDefaultRetryPolicyProperties() {

    return new DefaultRetryPolicyProperties();
  }

  /**
   * @return
   */
  @Bean
  public MessageBackOffPolicy messageBackOffPolicy() {

    return new DefaultBackOffPolicy(messageDefaultBackOffPolicyProperties());
  }

  /**
   * @return
   */
  @Bean
  public MessageRetryPolicy messageRetryPolicy() {

    return new DefaultRetryPolicy(messageDefaultRetryPolicyProperties());
  }

  /**
   * @param messageDefaultRetryPolicy
   * @param messageDefaultBackOffPolicy
   * @param messageSender
   * @return
   */
  @Bean
  public MessageRetryTemplate messageDefaultRetryTemplate(DefaultRetryPolicy messageDefaultRetryPolicy,
      DefaultBackOffPolicy messageDefaultBackOffPolicy, MessageSender messageSender) {

    MessageRetryTemplate bean = new MessageRetryTemplate(messageDefaultRetryPolicy, messageDefaultBackOffPolicy);
    bean.setMessageSender(messageSender);
    return bean;
  }

}
