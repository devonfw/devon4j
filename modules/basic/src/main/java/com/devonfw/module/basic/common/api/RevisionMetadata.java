package com.devonfw.module.basic.common.api;

import java.util.Date;

/**
 * This is the interface for the metadata associated with a
 * {@link com.devonfw.module.basic.common.api.entity.RevisionedEntity#getRevision() historic revision} of an
 * {@link com.devonfw.module.basic.common.api.entity.RevisionedEntity revisioned entity}.
 *
 * @since 3.0.0
 */
public interface RevisionMetadata {

  /**
   * This method gets the {@link com.devonfw.module.basic.common.api.entity.RevisionedEntity#getRevision() revision
   * number}.
   *
   * @return the revision number.
   */
  Number getRevision();

  /**
   * This method gets the date when this revision was created (closed).
   *
   * @return the date of completion or {@code null} if the according entity is the latest revision.
   */
  Date getDate();

  /**
   * This method gets the identifier (login) of the author who created this revision.
   *
   * @return the author. May be {@code null} (if committed outside user scope).
   */
  String getAuthor();

}
