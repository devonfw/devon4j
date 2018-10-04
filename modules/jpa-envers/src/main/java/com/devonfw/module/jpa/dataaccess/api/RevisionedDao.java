package com.devonfw.module.jpa.dataaccess.api;

import com.devonfw.module.jpa.dataaccess.api.RevisionedPersistenceEntity;

/**
 * This is a simplified variant of {@link GenericRevisionedDao} for the suggested and common case that you have a
 * {@link Long} as {@link RevisionedPersistenceEntity#getId() primary key}.
 *
 * @param <ENTITY> is the type of the managed {@link RevisionedPersistenceEntity entity}.
 */
public interface RevisionedDao<ENTITY extends RevisionedPersistenceEntity<Long>> extends
    GenericRevisionedDao<Long, ENTITY> {

}
