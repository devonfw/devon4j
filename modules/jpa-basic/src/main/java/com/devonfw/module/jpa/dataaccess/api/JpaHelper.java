package com.devonfw.module.jpa.dataaccess.api;

import java.util.Collection;

import net.sf.mmm.util.entity.api.GenericEntity;

import com.devonfw.module.basic.common.api.reference.GenericIdRef;
import com.devonfw.module.basic.common.api.reference.Ref;

/**
 * Helper class for generic handling of {@link net.sf.mmm.util.entity.api.PersistenceEntity persistence entities} (based
 * on {@link javax.persistence.EntityManager}). In some cases it is required to access JPA features in a static way.
 * E.g. a common case is a setter in your {@link net.sf.mmm.util.entity.api.PersistenceEntity} for a
 * {@link com.devonfw.module.basic.common.api.reference.Ref reference} from an
 * {@link com.devonfw.module.basic.common.api.to.AbstractEto ETO} that can be archieved via the following code:
 *
 * <pre>
 * &#64;Entity
 * &#64;Table("Foo")
 * public class FooEntity extends ApplicationPersistenceEntity implements Foo {
 *   &#64;OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
 *   &#64;JoinColumn(name = "bar")
 *   private BarEntity bar;
 *   ...
 *   &#64;Override
 *   public void setBarId({@link com.devonfw.module.basic.common.api.reference.IdRef}{@literal <Bar>} barId) {
 *     this.bar = {@link JpaHelper}.{@link JpaHelper#asEntity(Ref, Class) asEntity}(barId, BarEntity.class);
 *   }
 * }
 * </pre>
 */
public class JpaHelper {

  /**
   * @param <E> generic type of the {@link net.sf.mmm.util.entity.api.PersistenceEntity entity}
   * @param reference the {@link Ref} or {@code null}. Typically an
   *        {@link com.devonfw.module.basic.common.api.reference.IdRef}.
   * @param entityClass the {@link net.sf.mmm.util.entity.api.PersistenceEntity entity} {@link Class}.
   * @return the {@link net.sf.mmm.util.entity.api.PersistenceEntity entity} of the specified {@link Class} with the
   *         {@link Ref#getId() ID} from the given {@link GenericIdRef} or {@code null} if the given {@link Ref} is
   *         {@code null}.
   */
  public static <E> E asEntity(Ref<?, ? super E> reference, Class<E> entityClass) {

    if (reference == null) {
      return null;
    } else {
      return JpaEntityManagerAccess.getEntityManager().getReference(entityClass, reference.getId());
    }
  }

  /**
   * @param <E> generic type of the input {@link GenericEntity entities} (most commonly the entity interface).
   * @param <P> generic type of the output {@link net.sf.mmm.util.entity.api.PersistenceEntity persistence entities}.
   * @param input the {@link Collection} of {@link GenericEntity entities} (e.g.
   *        {@link com.devonfw.module.basic.common.api.to.AbstractEto ETOs}) to use as input.
   * @param entityClass the {@link Class} reflecting the {@link net.sf.mmm.util.entity.api.PersistenceEntity}.
   * @param output die {@link Collection} where to {@link Collection#add(Object) add} the
   *        {@link net.sf.mmm.util.entity.api.PersistenceEntity persistent entities} corresponding to the input
   *        {@link GenericEntity entities}. Most probably {@link Collection#isEmpty() empty} but may also already
   *        contain entities so this method will add additional entities.
   */
  @SuppressWarnings("unchecked")
  public static <E extends GenericEntity<?>, P extends E> void asEntities(Collection<? extends E> input,
      Class<P> entityClass, Collection<P> output) {

    for (E eto : input) {
      P entity;
      if (entityClass.isInstance(eto)) {
        entity = (P) eto;
      } else {
        entity = JpaEntityManagerAccess.getEntityManager().getReference(entityClass, eto.getId());
      }
      output.add(entity);
    }
  }

}
