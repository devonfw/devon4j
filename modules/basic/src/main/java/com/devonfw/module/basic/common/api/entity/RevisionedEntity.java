package com.devonfw.module.basic.common.api.entity;

/**
 * This is the interface for a {@link GenericEntity} that is (potentially) revision-controlled. Frameworks like
 * {@code hibernate-envers} (look for {@code @Audited} annotation on {@link javax.persistence.Entity JPA entities}) or
 * {@code bi-temporal} can manage such entities so all changes are stored in the database as a revision history. An
 * instance of this interface represents the {@link #getRevision() revision} of the {@link GenericEntity entity}. There
 * are two cases to distinguish:
 * <ul>
 * <li><b>{@link #LATEST_REVISION latest revision}</b><br>
 * A {@link RevisionedEntity} pointing to {@link #LATEST_REVISION} ({@code null}) represents the latest state of the
 * entity and can be modified.</li>
 * <li><b>historic {@link #getRevision() revision}</b><br>
 * If the object is {@link #getRevision() revision controlled}, it has a history of modifications. A
 * {@link RevisionedEntity} can represent a historic {@link #getRevision() revision} out of this history. It therefore
 * is immutable so operations to modify the {@link RevisionedEntity} will typically fail.</li>
 * </ul>
 *
 * @param <ID> is the type of the {@link #getId() primary key}.
 *
 * @since 3.0.0
 */
public interface RevisionedEntity<ID> extends GenericEntity<ID> {

  /**
   * The latest {@link #getRevision() revision} of an {@link GenericEntity entity}.
   */
  Number LATEST_REVISION = null;

  /**
   * This method gets the revision of this entity. The {@link RevisionedEntity#LATEST_REVISION latest revision} of an
   * entity will always return {@code null}. Otherwise this object is a <em>historic entity</em> and it will be
   * read-only so modifications are NOT permitted.
   *
   * @return the revision or {@link #LATEST_REVISION} ({@code null}) if this is the latest revision.
   */
  Number getRevision();

  /**
   * This method sets the {@link #getRevision() revision} of this entity. <br>
   * <b>ATTENTION:</b><br>
   * This operation should only be used in specific cases and if you are aware of what you are doing as this attribute
   * is managed by the persistence. However, for final freedom we decided to add this method to the API (e.g. to copy
   * from transfer-object to persistent-entity and vice-versa).
   *
   * @param revision is the new value of {@link #getRevision()}.
   */
  void setRevision(Number revision);

}
