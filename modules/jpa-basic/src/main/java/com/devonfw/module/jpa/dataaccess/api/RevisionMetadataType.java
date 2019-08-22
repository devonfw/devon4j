package com.devonfw.module.jpa.dataaccess.api;

import java.util.Date;

/**
 * @deprecated use {@link com.devonfw.module.basic.common.api.RevisionMetadataType} instead.
 *
 *             Implementation of {@link RevisionMetadata} as immutable type.
 */
@Deprecated
public class RevisionMetadataType implements RevisionMetadata {

  private final Number revision;

  private final Date date;

  private final String author;

  /**
   * The constructor.
   *
   * @param revision the {@link #getRevision() revision}.
   * @param date the {@link #getDate() date}.
   * @param author the {@link #getAuthor() author}.
   */
  public RevisionMetadataType(Number revision, Date date, String author) {

    this.revision = revision;
    this.date = date;
    this.author = author;
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
  public String getAuthor() {

    return this.author;
  }

  /**
   * @param revEntity die {@link AdvancedRevisionEntity}.
   * @return die Instanz von {@link RevisionMetadataType} bzw. {@code null} falls {@code revision} den Wert {@code null}
   *         hat.
   */
  public static RevisionMetadataType of(AdvancedRevisionEntity revEntity) {

    if (revEntity == null) {
      return null;
    }
    return new RevisionMetadataType(revEntity.getId(), revEntity.getDate(), revEntity.getUserLogin());
  }

  @Override
  public String toString() {

    return this.revision + "@" + this.date + " by " + this.author;
  }

}
