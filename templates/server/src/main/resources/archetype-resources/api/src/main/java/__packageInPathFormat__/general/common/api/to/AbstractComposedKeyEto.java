package ${package}.general.common.api.to;

import java.io.Serializable;

import com.devonfw.module.basic.common.api.entity.GenericEntity;
import com.devonfw.module.basic.common.api.to.AbstractEto;
import com.devonfw.module.basic.common.api.to.AbstractTo;

public abstract class AbstractComposedKeyEto<ID extends Serializable> extends AbstractTo implements GenericEntity<ID> {

	private static final long serialVersionUID = 1L;

	private ID id;

	private int modificationCounter;

	private transient GenericEntity<ID> persistentEntity;

	/**
	 * The constructor.
	 */
	public AbstractComposedKeyEto() {

		super();
	}

	@Override
	public ID getId() {

		return this.id;
	}

	@Override
	public void setId(ID id) {

		this.id = id;
	}

	@Override
	public int getModificationCounter() {

		if (this.persistentEntity != null) {
			// JPA implementations will update modification counter only after the
			// transaction has been committed.
			// Conversion will typically happen before and would result in the wrong (old)
			// modification counter.
			// Therefore we update the modification counter here (that has to be called
			// before serialization takes
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
	 * @param buffer is the {@link StringBuilder} where to
	 *               {@link StringBuilder#append(Object) append} the string
	 *               representation.
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
	 * Inner class to grant access to internal persistent {@link GenericEntity
	 * entity} reference of an {@link AbstractEto}. Shall only be used internally
	 * and never be external users.
	 */
	public static class PersistentEntityAccess {

		/**
		 * Sets the internal persistent {@link GenericEntity entity} reference of the
		 * given {@link AbstractEto}.
		 *
		 * @param <ID>             is the generic type of the
		 *                         {@link GenericEntity#getId() ID}.
		 * @param eto              is the {@link AbstractEto ETO}.
		 * @param persistentEntity is the persistent {@link GenericEntity entity}.
		 */
		protected <ID extends Serializable> void setPersistentEntity(AbstractComposedKeyEto<ID> eto,
				GenericEntity<ID> persistentEntity) {

			assert ((eto.persistentEntity == null) || (persistentEntity == null));
			eto.persistentEntity = persistentEntity;
		}
	}
}