package com.devonfw.module.basic.common.api.to;

import com.devonfw.module.basic.common.api.entity.GenericEntity;

/**
 * This is the abstract base class for an {@link AbstractEto transfer-object} that contains all the data properties of
 * an {@link GenericEntity entity} without its relations. This is called <em>ETO</em> (entity transfer object).
 * Sometimes in other contexts people also call this DTO (data transfer object).<br>
 * Here, data properties are the properties using a datatype (immutable value type such as {@link String},
 * {@link Number}, {@link java.time.Instant}, custom-datatype, etc.). Relations of an @link GenericEntity entity} are
 * not contained except for {@link #getId() IDs} of {@link javax.persistence.OneToOne} relations. Instead of using
 * {@link Long} we recommend to use {@link com.devonfw.module.basic.common.api.reference.IdRef} to be type-safe and more
 * expressive. For actual relations you will use {@link AbstractCto CTO}s to express what set of entities to transfer,
 * load, save, update, etc. without redundancies.<br>
 * Classes extending this class should carry the suffix <code>Eto</code>.
 *
 * @since 3.0.0
 */
public abstract class AbstractEto extends AbstractTo implements GenericEntity<Long> {

  private static final long serialVersionUID = 1L;

  private Long id;

  private int modificationCounter;

  private transient GenericEntity<Long> persistentEntity;

  /**
   * The constructor.
   */
  public AbstractEto() {

    super();
  }

  @Override
  public Long getId() {

    return this.id;
  }

  @Override
  public void setId(Long id) {

    this.id = id;
  }

  @Override
  public int getModificationCounter() {

    if (this.persistentEntity != null) {
      // JPA implementations will update modification counter only after the transaction has been committed.
      // Conversion will typically happen before and would result in the wrong (old) modification counter.
      // Therefore we update the modification counter here (that has to be called before serialization takes
      // place).
      this.modificationCounter = this.persistentEntity.getModificationCounter();
    }
    return this.modificationCounter;
  }

  @Override
  public void setModificationCounter(int version) {

    this.modificationCounter = version;
  }

  /**
   * Method to extend {@link #toString()} logic.
   *
   * @param buffer is the {@link StringBuilder} where to {@link StringBuilder#append(Object) append} the string
   *        representation.
   */
  @Override
  protected void toString(StringBuilder buffer) {

    super.toString(buffer);
    if (this.id != null) {
      buffer.append("[id=");
      buffer.append(this.id);
      buffer.append("]");
    }
  }

  /**
   * Inner class to grant access to internal persistent {@link GenericEntity entity} reference of an
   * {@link AbstractEto}. Shall only be used internally and never be external users.
   */
  public static class PersistentEntityAccess {

    /**
     * Sets the internal persistent {@link GenericEntity entity} reference of the given {@link AbstractEto}.
     *
     * @param <ID> is the generic type of the {@link GenericEntity#getId() ID}.
     * @param eto is the {@link AbstractEto ETO}.
     * @param persistentEntity is the persistent {@link GenericEntity entity}.
     */
    protected <ID> void setPersistentEntity(AbstractEto eto, GenericEntity<Long> persistentEntity) {

      assert ((eto.persistentEntity == null) || (persistentEntity == null));
      eto.persistentEntity = persistentEntity;
    }
  }
}
