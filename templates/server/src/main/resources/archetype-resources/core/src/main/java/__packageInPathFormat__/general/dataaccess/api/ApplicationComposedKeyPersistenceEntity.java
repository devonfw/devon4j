package ${package}.general.dataaccess.api;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

import ${package}.general.common.api.ApplicationComposedKeyEntity;
import com.devonfw.module.basic.common.api.entity.PersistenceEntity;

/**
 * Abstract Entity for all Entities with an id and a version field.
 */
@MappedSuperclass
public abstract class ApplicationComposedKeyPersistenceEntity<ID extends Serializable>
		implements ApplicationComposedKeyEntity<ID>, PersistenceEntity<ID> {

	private static final long serialVersionUID = 1L;

	/** @see #getId() */
	private ID id;

	/** @see #getModificationCounter() */
	private int modificationCounter;

	/** @see #getRevision() */
	private Number revision;

	/**
	 * The constructor.
	 */
	public ApplicationComposedKeyPersistenceEntity() {

		super();
	}

	@Override
	@EmbeddedId
	public ID getId() {

		return this.id;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setId(ID id) {

		this.id = id;
	}

	@Override
	@Version
	public int getModificationCounter() {

		return this.modificationCounter;
	}

	@Override
	public void setModificationCounter(int version) {

		this.modificationCounter = version;
	}

	@Override
	public String toString() {

		StringBuilder buffer = new StringBuilder();
		toString(buffer);
		return buffer.toString();
	}

	/**
	 * Method to extend {@link #toString()} logic.
	 *
	 * @param buffer is the {@link StringBuilder} where to
	 *               {@link StringBuilder#append(Object) append} the string
	 *               representation.
	 */
	protected void toString(StringBuilder buffer) {

		buffer.append(getClass().getSimpleName());
		if (this.id != null) {
			buffer.append("[id=");
			buffer.append(this.id);
			buffer.append("]");
		}
	}

}
