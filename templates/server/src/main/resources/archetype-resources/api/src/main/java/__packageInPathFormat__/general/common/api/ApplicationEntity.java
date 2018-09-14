package ${package}.general.common.api;

import net.sf.mmm.util.entity.api.MutableGenericEntity;

/**
 * This is the abstract interface for a {@link MutableGenericEntity} of this application. We are using {@link Long} for
 * all {@link #getId() primary keys}.
 */
public abstract interface ApplicationEntity extends MutableGenericEntity<Long> {

}
