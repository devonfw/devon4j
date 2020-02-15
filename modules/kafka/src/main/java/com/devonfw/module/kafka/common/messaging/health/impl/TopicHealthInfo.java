package com.devonfw.module.kafka.common.messaging.health.impl;

import java.util.HashSet;
import java.util.Set;

public class TopicHealthInfo {

  private String topic;

  private String status;

  private String details;

  private Set<Integer> partitionsChecked = new HashSet<>();

  private Set<Integer> partitionsWithoutLeader = new HashSet<>();

  public String getTopic() {

    return this.topic;
  }

  void setTopic(String topic) {

    this.topic = topic;
  }

  public String getStatus() {

    return this.status;
  }

  void setStatus(String status) {

    this.status = status;
  }

  public String getDetails() {

    return this.details;
  }

  void setDetails(String details) {

    this.details = details;
  }

  public Set<Integer> getPartitionsChecked() {

    return this.partitionsChecked;
  }

  public Set<Integer> getPartitionsWithoutLeader() {

    return this.partitionsWithoutLeader;
  }

}
