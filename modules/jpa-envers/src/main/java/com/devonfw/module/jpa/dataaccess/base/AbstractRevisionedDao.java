package com.devonfw.module.jpa.dataaccess.base;

import com.devonfw.module.jpa.dataaccess.api.RevisionedDao;
import com.devonfw.module.jpa.dataaccess.api.RevisionedPersistenceEntity;

/**
 * Abstract base implementation of {@link RevisionedDao} interface.
 *
 * @param <ENTITY> is the type of the managed {@link RevisionedPersistenceEntity entity}.
 *
 * @since 3.0.0
 */
public abstract class AbstractRevisionedDao<ENTITY extends RevisionedPersistenceEntity<Long>>
    extends AbstractGenericRevisionedDao<Long, ENTITY> implements RevisionedDao<ENTITY> {

}
