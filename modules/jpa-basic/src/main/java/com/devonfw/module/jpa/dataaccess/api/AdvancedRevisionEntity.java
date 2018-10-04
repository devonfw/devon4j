/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package com.devonfw.module.jpa.dataaccess.api;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.envers.RevisionEntity;
import org.hibernate.envers.RevisionNumber;
import org.hibernate.envers.RevisionTimestamp;

/**
 * This is a custom {@link org.hibernate.envers.DefaultRevisionEntity revision entity} also containing the actual user.
 *
 * @see org.hibernate.envers.DefaultRevisionEntity
 */
@Entity
@RevisionEntity(AdvancedRevisionListener.class)
@Table(name = "RevInfo")
public class AdvancedRevisionEntity implements PersistenceEntity<Long> {

  /** UID for serialization. */
  private static final long serialVersionUID = 1L;

  /** @see #getId() */
  @Id
  @GeneratedValue
  @RevisionNumber
  private Long id;

  /** @see #getTimestamp() */
  @RevisionTimestamp
  @Column(name = "\"timestamp\"")
  private long timestamp;

  /** @see #getDate() */
  private transient Date date;

  /** @see #getUserLogin() */

  private String userLogin;

  /**
   * The constructor.
   */
  public AdvancedRevisionEntity() {

    super();
  }

  @Override
  public Long getId() {

    return this.id;
  }

  /**
   * @param id is the new value of {@link #getId()}.
   */
  public void setId(Long id) {

    this.id = id;
  }

  /**
   * @return the timestamp when this revision has been created.
   */
  public long getTimestamp() {

    return this.timestamp;
  }

  /**
   * @return the {@link #getTimestamp() timestamp} as {@link Date}.
   */
  public Date getDate() {

    if (this.date == null) {
      this.date = new Date(this.timestamp);
    }
    return this.date;
  }

  /**
   * @param timestamp is the new value of {@link #getTimestamp()}.
   */
  public void setTimestamp(long timestamp) {

    this.timestamp = timestamp;
  }

  /**
   * @return the login or id of the user that has created this revision.
   */

  public String getUserLogin() {

    return this.userLogin;
  }

  /**
   * @param userLogin is the new value of {@link #getUserLogin()}.
   */
  public void setUserLogin(String userLogin) {

    this.userLogin = userLogin;
  }

  @Override
  public int getModificationCounter() {

    return 0;
  }

  @Override
  public void setModificationCounter(int modificationCounter) {

  }
}
