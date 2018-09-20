/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package com.devonfw.module.jpa.dataaccess.api;

import java.util.Date;

/**
 * This is the interface for the metadata associated with a
 * {@link net.sf.mmm.util.entity.api.RevisionedEntity#getRevision() historic revision} of an
 * {@link net.sf.mmm.util.entity.api.RevisionedEntity entity}.
 *
 */
public interface RevisionMetadata {

  /**
   * This method gets the {@link net.sf.mmm.util.entity.api.RevisionedEntity#getRevision() revision number}.
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
