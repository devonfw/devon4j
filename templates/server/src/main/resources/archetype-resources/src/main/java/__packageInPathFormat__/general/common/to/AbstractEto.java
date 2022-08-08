package ${package}.general.common.to;

import ${package}.general.common.entity.GenericEntity;

/**
 * This is the abstract base class for an {@link AbstractEto transfer-object} that contains all the data properties of
 * an {@link GenericEntity entity} without its relations. This is called <em>ETO</em> (entity transfer object).
 * Sometimes in other contexts people also call this DTO (data transfer object).<br>
 * Here, data properties are the properties using a datatype (immutable value type such as {@link String},
 * {@link Number}, {@link java.time.Instant}, custom-datatype, etc.). Relations of an @link GenericEntity entity} are
 * not contained except for {@link #getId() IDs} of {@link javax.persistence.OneToOne} relations. For actual relations
 * you will use {@link AbstractCto CTO}s to express what set of entities to transfer, load, save, update, etc. without
 * redundancies.<br>
 * Classes extending this class should carry the suffix <code>Eto</code>.
 */
public abstract class AbstractEto extends AbstractGenericEto<Long> {

  private Long id;

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

}
