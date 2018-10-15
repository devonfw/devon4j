/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package com.devonfw.module.jpa.dataaccess.api;

import com.devonfw.module.basic.common.api.entity.PersistenceEntity;
import com.devonfw.module.basic.common.api.entity.RevisionedEntity;

/**
 * This is the interface for a {@link RevisionedEntity revisioned} {@link PersistenceEntity persistence entity}.
 *
 * @param <ID> is the type of the {@link #getId() primary key}.
 *
 * @since 3.0.0
 */
public interface RevisionedPersistenceEntity<ID> extends PersistenceEntity<ID>, RevisionedEntity<ID> {

}
