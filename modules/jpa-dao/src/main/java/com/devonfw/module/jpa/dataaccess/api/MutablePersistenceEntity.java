/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package com.devonfw.module.jpa.dataaccess.api;

import net.sf.mmm.util.entity.api.MutableRevisionedEntity;
import net.sf.mmm.util.entity.api.PersistenceEntity;

/**
 * This is the interface for a {@link PersistenceEntity} in Devon4j.
 *
 * @param <ID> is the type of the {@link #getId() primary key}.
 */
public interface MutablePersistenceEntity<ID> extends PersistenceEntity<ID>, MutableRevisionedEntity<ID> {

  @Override
  void setRevision(Number revision);

}
