package com.devonfw.module.jpa.dataaccess.api.data;

import com.devonfw.module.jpa.dataaccess.api.PersistenceEntity;

/**
 * {@link GenericRevisionedRepository} with defaults applied for simple usage.
 *
 * @param <E> generic type of the managed {@link PersistenceEntity}.
 * @since 3.0.0
 */
public interface DefaultRevisionedRepository<E extends PersistenceEntity<Long>>
    extends GenericRevisionedRepository<E, Long> {

}
