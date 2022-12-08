package ${package}.general.common.entity;

/**
 * This is the interface for an entity, which is an object that is potentially stored in a persistent store (typically a
 * database via JPA). Every non-abstract implementation of this interface is simply called an <em>entity</em>. It is
 * supposed to be a simple java bean. <br>
 * This interface makes the following assumptions:
 * <ul>
 * <li>A {@link GenericEntity} is identified by a {@link #getId() primary key}.</li>
 * <li>A {@link GenericEntity} has a {@link #getModificationCounter() modification counter} for optimistic locking (even
 * though not strictly required - you could statically return 0).</li>
 * </ul>
 * <b>ATTENTION:</b><br>
 * An instance of this interface is typically one of the following:
 * <ul>
 * <li>a <b>JPA {@link javax.persistence.Entity}</b><br>
 * <li>a <b>{@link ${package}.general.common.to.AbstractEto entity transfer-object}</b><br>
 * </ul>
 * In order to distinguish the above cases an application has an architecture that organizes the code in technical
 * layers (see <a href="http://en.wikipedia.org/wiki/Multilayered_architecture">multilayered architecture</a>) and
 * business oriented slices (business components). Therefore within the persistence layer instances should always be
 * {@link javax.persistence.Entity persistence entities}. On the other hand the higher layers always need to use
 * {@link ${package}.general.common.to.AbstractTo transfer-objects}. Our recommendation is to map between these two in the
 * logic-layer using the {@code BeanMapper} component from the {@code devon4j-beanmapping} module.
 *
 * @see javax.persistence.Entity
 *
 * @param <ID> is the type of the {@link #getId() primary key}.
 *
 * @since 3.0.0
 */
public interface GenericEntity<ID> {

  /**
   * @return the primary key (unique identifier) of this entity. May be {@code null} if this entity is transient (not
   *         yet {@link javax.persistence.EntityManager#persist(Object) saved} in the database). While this method is
   *         initially defined in a generic way, it is strongly recommended to use {@link Long} as datatype for IDs.
   *         <br>
   *         Even if you want to have a {@link String}-based business-oriented identifier it is best practice to use a
   *         {@link Long} as primary key and add the business identifier as additional field (with a unique constraint).
   *         This way references to the entity will be a lot more compact and improve your performance in JOINs or the
   *         like. However, there may be reasons to use other datatypes for the ID. In any case the unique ID should be
   *         an immutable java-object that can be rebuild from its {@link Object#toString() string-representation}. <br>
   *         Please note that if your ID has a specific syntax, semantic, formatting rules, etc. you should create a
   *         custom datatype for it. If it can easily be mapped to a {@link Long} value you can still use {@link Long}
   *         here and provide a transient getter method that returns the your custom datatype from the {@link Long}.
   * @see javax.persistence.Id
   */
  ID getId();

  /**
   * @param id the new {@link #getId() primary key}. This method shall typically only be used by technical frameworks
   *        such as hibernate.
   */
  void setId(ID id);

  /**
   * This method gets the current modification-counter of this entity. Whenever the object gets modified and
   * {@link javax.persistence.EntityManager#persist(Object) persisted}, this counter will be increased (typically after
   * the transaction is closed). The initial value after construction is {@code 0}. <br>
   * This property is often simply called {@link javax.persistence.Version version}. However, as this sometimes causes
   * confusion or may conflict with a business property "version", we use the more technical and self-explanatory name
   * {@code modificationCounter}.<br>
   * If this feature is NOT supported for some reason, this method should always return {@code 0}.
   *
   * @see javax.persistence.Version
   *
   * @return the current modification-counter.
   * @see javax.persistence.Version
   */
  int getModificationCounter();

  /**
   * @param modificationCounter the new {@link #getModificationCounter() modification counter}. This method shall
   *        typically only be used by technical frameworks such as hibernate.
   */
  void setModificationCounter(int modificationCounter);

}
