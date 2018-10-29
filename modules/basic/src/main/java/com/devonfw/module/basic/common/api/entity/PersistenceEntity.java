package com.devonfw.module.basic.common.api.entity;

/**
 * {@link GenericEntity} representing the actual {@link javax.persistence.Entity} to be stored in the database (unlike
 * {@link com.devonfw.module.basic.common.api.to.AbstractEto ETO}).
 *
 * @param <ID> is the type of the {@link #getId() primary key}.
 *
 * @since 3.0.0
 */
public interface PersistenceEntity<ID> extends GenericEntity<ID> {

}
