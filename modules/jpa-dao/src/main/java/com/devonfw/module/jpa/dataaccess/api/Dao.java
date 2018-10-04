package com.devonfw.module.jpa.dataaccess.api;

/**
 * This is a simplified variant of {@link GenericDao} for the suggested and common case that you have a {@link Long} as
 * {@link PersistenceEntity#getId() primary key}.
 *
 * @see GenericDao
 *
 * @param <E> is the generic type of the {@link PersistenceEntity}.
 */
public interface Dao<E extends PersistenceEntity<Long>> extends GenericDao<Long, E> {

}
