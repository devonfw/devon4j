package com.devonfw.module.kafka.common.messaging.health.impl;

import java.util.HashSet;
import java.util.Set;

/**
 * This class is used to indicate the health.
 *
 * @deprecated The implementation of Devon4Js Kafka module will be abandoned. It is superseeded by Springs Kafka
 *             implementation.
 */
@Deprecated
public class TopicHealthInfo {

  private String topic;

  private String status;

  private String details;

  private Set<Integer> partitionsChecked = new HashSet<>();

  private Set<Integer> partitionsWithoutLeader = new HashSet<>();

  /**
   * The topic
   *
   * @return the topic
   */
  public String getTopic() {

    return this.topic;
  }

  /**
   * Set the topic for {@link #getTopic()}
   *
   * @param topic
   */
  void setTopic(String topic) {

    this.topic = topic;
  }

  /**
   * The status of the Health
   *
   * @return the status
   */
  public String getStatus() {

    return this.status;
  }

  /**
   * Set the status for {@link #getStatus()}
   *
   * @param status
   */
  void setStatus(String status) {

    this.status = status;
  }

  /**
   * The other details of the health status.
   *
   * @return the details.
   */
  public String getDetails() {

    return this.details;
  }

  /**
   * Set the details for {@link #getDetails()}.
   *
   * @param details
   */
  void setDetails(String details) {

    this.details = details;
  }

  /**
   * The partitions
   *
   * @return the Set of Integer partitions.
   */
  public Set<Integer> getPartitionsChecked() {

    return this.partitionsChecked;
  }

  /**
   * The partitions without leader.
   *
   * @return the set of integer partitions without leaders.
   */
  public Set<Integer> getPartitionsWithoutLeader() {

    return this.partitionsWithoutLeader;
  }

}
