package ${package}.general.common.api;

import com.devonfw.module.basic.common.api.entity.GenericEntity;

/**
 * This is the abstract interface for a {@link GenericEntity} of this
 * application. We are using {@link ID} for all {@link #getId() primary keys
 * that are composed}.
 */
public abstract interface ApplicationComposedKeyEntity<ID> extends GenericEntity<ID> {

}
