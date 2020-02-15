package com.devonfw.module.kafka.common.messaging.health.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.common.PartitionInfo;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.kafka.core.ConsumerFactory;

import com.devonfw.module.kafka.common.messaging.health.api.config.KafkaHealthIndicatorProperties;

/**
 * @author ravicm
 *
 */
public class KafkaHealthIndicator implements HealthIndicator {

  private final ConsumerFactory<?, ?> consumerFactory;

  private KafkaHealthIndicatorProperties properties;

  private Consumer<?, ?> metadataConsumer;

  /**
   * The constructor.
   *
   * @param consumerFactory
   * @param properties
   */
  public KafkaHealthIndicator(ConsumerFactory<?, ?> consumerFactory, KafkaHealthIndicatorProperties properties) {

    this.consumerFactory = consumerFactory;
    this.properties = properties;
  }

  @Override
  public Health health() {

    ExecutorService executor = Executors.newSingleThreadExecutor();
    Future<Health> future = executor.submit(this::doCheckKafkaHealth);

    try {
      return future.get(this.properties.getTimeout(), TimeUnit.SECONDS);
    } catch (InterruptedException e) {

      Thread.currentThread().interrupt();

      return Health.down().withDetail("Interruption of the health check within the waiting time.",
          "waiting period: " + this.properties.getTimeout() + " seconds").build();

    } catch (ExecutionException e) {

      return Health.down(e).build();

    } catch (TimeoutException e) {

      return Health.down().withDetail("Health check could not be carried out within the waiting time.",
          "waiting period: " + this.properties.getTimeout() + " seconds").build();
    } finally {
      executor.shutdownNow();
    }
  }

  /**
   * @return
   */
  protected Health doCheckKafkaHealth() {

    try {

      Optional.ofNullable(this.metadataConsumer)
          .ifPresent(metaConsumer -> this.metadataConsumer = this.consumerFactory.createConsumer());

      if (this.properties == null || this.properties.getTopicsToCheck() == null
          || this.properties.getTopicsToCheck().isEmpty()) {

        this.metadataConsumer.listTopics();
        return Health.up().build();
      }
      // else {

      List<TopicHealthInfo> topicHealthInfos = checkPartitionLeaders(this.properties.getTopicsToCheck());
      return checkAllTopicsAndReturnStatus(topicHealthInfos);
      // }
    } catch (Exception e) {

      return Health.down(e).build();
    }
  }

  /**
   * @param topicHealthInfos
   * @return
   */
  private Health checkAllTopicsAndReturnStatus(List<TopicHealthInfo> topicHealthInfos) {

    if (isAllUp(topicHealthInfos)) {
      return Health.up().withDetail("topics", topicHealthInfos).build();
    }

    return Health.down().withDetail("topics", topicHealthInfos).build();
  }

  /**
   * @param topics
   * @return
   */
  protected List<TopicHealthInfo> checkPartitionLeaders(Set<String> topics) {

    List<TopicHealthInfo> result = new ArrayList<>();

    topics.forEach(topic -> checkPartitionLeaders(result, topic));

    return result;
  }

  /**
   * @param result
   * @param topic
   */
  private void checkPartitionLeaders(List<TopicHealthInfo> result, String topic) {

    TopicHealthInfo healthInfo = new TopicHealthInfo();
    healthInfo.setTopic(topic);
    healthInfo.setStatus("UP");
    result.add(healthInfo);

    List<PartitionInfo> partitionInfos = this.metadataConsumer.partitionsFor(topic);

    Optional.ofNullable(partitionInfos).ifPresentOrElse(
        partitionInfo -> checkPartionLeaderAndSetHealthStatus(healthInfo, partitionInfos),
        () -> setHealthStatusDownwhenPartionInfosAreEmpty(healthInfo));
  }

  /**
   * @param healthInfo
   * @param partitionInfos
   */
  private void checkPartionLeaderAndSetHealthStatus(TopicHealthInfo healthInfo, List<PartitionInfo> partitionInfos) {

    for (PartitionInfo partitionInfo : partitionInfos) {
      healthInfo.getPartitionsChecked().add(partitionInfo.partition());

      if (partitionInfo.leader().id() == -1) {

        healthInfo.setStatus("DOWN");
        healthInfo.setDetails("Partitions without Leader.");
        healthInfo.getPartitionsWithoutLeader().add(partitionInfo.partition());
      }
    }
  }

  /**
   *
   */
  private void setHealthStatusDownwhenPartionInfosAreEmpty(TopicHealthInfo healthInfo) {

    healthInfo.setStatus("DOWN");
    healthInfo.setDetails("Topic does not exist.");
  }

  /**
   * @param healthInfos
   * @return
   */
  protected boolean isAllUp(List<TopicHealthInfo> healthInfos) {

    for (TopicHealthInfo healthInfo : healthInfos) {
      if ("DOWN".equals(healthInfo.getStatus())) {
        return false;
      }
    }
    return true;
  }
}
