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
 * This is an implementation for {@link HealthIndicator}.
 *
 * @param <K> the key type
 * @param <V> the value type
 *
 */
public class KafkaHealthIndicator<K, V> implements HealthIndicator {

  private final ConsumerFactory<K, V> consumerFactory;

  private KafkaHealthIndicatorProperties properties;

  private Consumer<K, V> metadataConsumer;

  /**
   * The constructor.
   *
   * @param consumerFactory the {@link ConsumerFactory}
   * @param properties the {@link KafkaHealthIndicatorProperties}
   */
  public KafkaHealthIndicator(ConsumerFactory<K, V> consumerFactory, KafkaHealthIndicatorProperties properties) {

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

  private Health doCheckKafkaHealth() {

    try {

      Optional.ofNullable(this.metadataConsumer).orElse(this.metadataConsumer = this.consumerFactory.createConsumer());

      if (this.properties == null || this.properties.getTopicsToCheck() == null
          || this.properties.getTopicsToCheck().isEmpty()) {

        this.metadataConsumer.listTopics();
        return Health.up().build();
      }

      List<TopicHealthInfo> topicHealthInfos = checkPartitionLeaders(this.properties.getTopicsToCheck());
      return checkAllTopicsAndReturnStatus(topicHealthInfos);

    } catch (Exception e) {

      return Health.down(e).build();
    }
  }

  private Health checkAllTopicsAndReturnStatus(List<TopicHealthInfo> topicHealthInfos) {

    if (isAllUp(topicHealthInfos)) {
      return Health.up().withDetail("topics", topicHealthInfos).build();
    }

    return Health.down().withDetail("topics", topicHealthInfos).build();
  }

  private List<TopicHealthInfo> checkPartitionLeaders(Set<String> topics) {

    List<TopicHealthInfo> result = new ArrayList<>();

    topics.forEach(topic -> checkPartitionLeaders(result, topic));

    return result;
  }

  private void checkPartitionLeaders(List<TopicHealthInfo> result, String topic) {

    TopicHealthInfo healthInfo = new TopicHealthInfo();
    healthInfo.setTopic(topic);
    healthInfo.setStatus(HealthStatus.UP.toString());
    result.add(healthInfo);

    List<PartitionInfo> partitionInfos = this.metadataConsumer.partitionsFor(topic);

    if (partitionInfos != null) {
      checkPartionLeaderAndSetHealthStatus(healthInfo, partitionInfos);
    } else {
      setHealthStatusDownwhenPartionInfosAreEmpty(healthInfo);
    }
  }

  private void checkPartionLeaderAndSetHealthStatus(TopicHealthInfo healthInfo, List<PartitionInfo> partitionInfos) {

    for (PartitionInfo partitionInfo : partitionInfos) {
      healthInfo.getPartitionsChecked().add(partitionInfo.partition());

      if (partitionInfo.leader().id() == -1) {

        healthInfo.setStatus(HealthStatus.DOWN.toString());
        healthInfo.setDetails("Partitions without Leader.");
        healthInfo.getPartitionsWithoutLeader().add(partitionInfo.partition());
      }
    }
  }

  private void setHealthStatusDownwhenPartionInfosAreEmpty(TopicHealthInfo healthInfo) {

    healthInfo.setStatus(HealthStatus.DOWN.toString());
    healthInfo.setDetails("Topic does not exist.");
  }

  private boolean isAllUp(List<TopicHealthInfo> healthInfos) {

    for (TopicHealthInfo healthInfo : healthInfos) {
      if (HealthStatus.DOWN.toString().equals(healthInfo.getStatus())) {
        return false;
      }
    }
    return true;
  }
}
