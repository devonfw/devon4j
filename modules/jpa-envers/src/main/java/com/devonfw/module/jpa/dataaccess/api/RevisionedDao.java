package com.devonfw.module.jpa.dataaccess.api;

import com.devonfw.module.jpa.dataaccess.api.MutablePersistenceEntity;

/**
 * This is a simplified variant of {@link GenericRevisionedDao} for the suggested and common case that you have a
 * {@link Long} as {@link MutablePersistenceEntity#getId() primary key}.
 *
 * @param <ENTITY> is the type of the managed {@link MutablePersistenceEntity entity}.
 */
public interface RevisionedDao<ENTITY extends MutablePersistenceEntity<Long>> extends
    GenericRevisionedDao<Long, ENTITY> {

}
