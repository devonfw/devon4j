package com.devonfw.module.kafka.common.messaging.logging.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.kafka.annotation.KafkaListenerAnnotationBeanPostProcessor;
import org.springframework.kafka.config.AbstractKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.util.StringUtils;

/**
 * This class is an implementation of {@link BeanPostProcessor}.
 *
 * @deprecated The implementation of Devon4Js Kafka module will be abandoned. It is superseeded by Springs Kafka
 *             implementation.
 */
@Deprecated
public class ConsumerGroupResolver implements BeanPostProcessor {

  private static final String DEFAULT_BEAN_NAME = KafkaListenerAnnotationBeanPostProcessor.DEFAULT_KAFKA_LISTENER_CONTAINER_FACTORY_BEAN_NAME;

  private static final String UNKNOWN = "Unknown";

  private Map<String, String> factoryToConsumerGroupMapping = new HashMap<>();

  @Override
  public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {

    return bean;
  }

  @Override
  public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {

    if (AbstractKafkaListenerContainerFactory.class.isAssignableFrom(bean.getClass())) {

      this.factoryToConsumerGroupMapping.put(beanName,
          getConsumerGroup((AbstractKafkaListenerContainerFactory<?, ?, ?>) bean));
    }
    return bean;
  }

  /**
   * This method is used to get Consumer group by passing {@link AbstractKafkaListenerContainerFactory}
   *
   * @param containerFactory the {@link AbstractKafkaListenerContainerFactory}
   * @return the groupName if present or else Unknown.
   */
  protected String getConsumerGroup(AbstractKafkaListenerContainerFactory<?, ?, ?> containerFactory) {

    ConsumerFactory<?, ?> consumerFactory = containerFactory.getConsumerFactory();

    if (DefaultKafkaConsumerFactory.class.isAssignableFrom(consumerFactory.getClass())) {

      DefaultKafkaConsumerFactory<?, ?> defaultKafkaConsumerFactory = (DefaultKafkaConsumerFactory<?, ?>) consumerFactory;
      Map<String, Object> properties = defaultKafkaConsumerFactory.getConfigurationProperties();

      return getGroupIfPresent(properties);
    }

    return UNKNOWN;
  }

  private String getGroupIfPresent(Map<String, Object> properties) {

    if (properties != null) {
      String groupId = (String) properties.get(ConsumerConfig.GROUP_ID_CONFIG);
      if (groupId != null) {
        return groupId;
      }
    }
    return UNKNOWN;
  }

  /**
   * This method is used to get Consumer group by passing factoryBean.
   *
   * @param factoryBeanName the {@link AbstractKafkaListenerContainerFactory}
   * @return the groupName if present or else Unknown.
   */
  public String getConsumerGroup(String factoryBeanName) {

    String result;
    if (StringUtils.isEmpty(factoryBeanName) || factoryBeanName.isEmpty()) {
      result = this.factoryToConsumerGroupMapping.get(DEFAULT_BEAN_NAME);
    } else {
      result = this.factoryToConsumerGroupMapping.get(factoryBeanName);
    }

    if (result == null) {
      return UNKNOWN;
    }
    return result;
  }
}
