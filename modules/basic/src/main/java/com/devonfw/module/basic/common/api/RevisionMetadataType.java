package com.devonfw.module.basic.common.api;

import java.util.Date;

/**
 * Implementation of {@link RevisionMetadata} as immutable type.
 */
public class RevisionMetadataType implements RevisionMetadata {

  private final Number revision;

  private final Date date;

  private final String userLogin;

  /**
   * The constructor.
   *
   * @param revision the {@link #getRevision() revision}.
   * @param date the {@link #getDate() date}.
   * @param author the {@link #getAuthor() author}.
   */
  public RevisionMetadataType(Number revision, Date date, String userLogin) {

    this.revision = revision;
    this.date = date;
    this.userLogin = userLogin;
  }

  @Override
  public Number getRevision() {

    return this.revision;
  }

  @Override
  public Date getDate() {

    return this.date;
  }

  @Override
  public String getUserLogin() {

    return this.userLogin;
  }

  @Override
  public String toString() {

    return this.revision + "@" + this.date + " by " + this.userLogin;
  }

}
