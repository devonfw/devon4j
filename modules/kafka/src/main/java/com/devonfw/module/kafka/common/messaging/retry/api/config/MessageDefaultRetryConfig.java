package com.devonfw.module.kafka.common.messaging.retry.api.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.devonfw.module.kafka.common.messaging.api.client.MessageSender;
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
   * @param props
   * @return
   */
  @Bean
  public DefaultBackOffPolicy messageDefaultBackOffPolicy(
      @Qualifier("messageDefaultBackOffPolicyProperties") DefaultBackOffPolicyProperties props) {

    return new DefaultBackOffPolicy(props);
  }

  /**
   * @param props
   * @return
   */
  @Bean
  public DefaultRetryPolicy messageDefaultRetryPolicy(
      @Qualifier("messageDefaultRetryPolicyProperties") DefaultRetryPolicyProperties props) {

    return new DefaultRetryPolicy(props);
  }

  /**
   * @param retryPolicy
   * @param backOffPolicy
   * @param sender
   * @return
   */
  @Bean
  public MessageRetryTemplate xxxMessageDefaultRetryTemplate(
      @Qualifier("messageDefaultRetryPolicy") DefaultRetryPolicy retryPolicy,
      @Qualifier("messageDefaultBackOffPolicy") DefaultBackOffPolicy backOffPolicy,
      @Qualifier("messageSender") MessageSender sender) {

    MessageRetryTemplate bean = new MessageRetryTemplate(retryPolicy, backOffPolicy);
    bean.setMessageSender(sender);
    return bean;
  }

}
