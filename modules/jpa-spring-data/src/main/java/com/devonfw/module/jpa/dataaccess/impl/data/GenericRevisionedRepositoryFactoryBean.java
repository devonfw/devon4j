package com.devonfw.module.jpa.dataaccess.impl.data;

import java.io.Serializable;

import javax.persistence.EntityManager;

import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;

import com.devonfw.module.jpa.dataaccess.api.data.GenericRepository;
import com.devonfw.module.jpa.dataaccess.api.data.GenericRevisionedRepository;

/**
 * {@link org.springframework.data.repository.core.support.RepositoryFactoryBeanSupport} for
 * {@link GenericRepositoryImpl}. In order to use {@link GenericRepository} you need to annotate your spring-boot
 * application as following:
 *
 * <pre>
 * &#64;{@link org.springframework.data.jpa.repository.config.EnableJpaRepositories}(repositoryFactoryBeanClass = {@link GenericRevisionedRepositoryFactoryBean}.class)
 * </pre>
 *
 * @param <R> generic type of the {@link GenericRevisionedRepository} interface.
 * @param <E> generic type of the managed {@link com.devonfw.module.basic.common.api.entity.PersistenceEntity entity}.
 * @param <ID> generic type of the {@link com.devonfw.module.basic.common.api.entity.PersistenceEntity#getId() primary key}
 *        of the entity.
 *
 * @since 3.0.0
 */
public class GenericRevisionedRepositoryFactoryBean<R extends GenericRevisionedRepository<E, ID>, E, ID extends Serializable>
    extends JpaRepositoryFactoryBean<R, E, ID> {

  /**
   * Creates a new {@link JpaRepositoryFactoryBean} for the given repository interface.
   *
   * @param repositoryInterface must not be {@literal null}.
   */
  public GenericRevisionedRepositoryFactoryBean(Class<? extends R> repositoryInterface) {

    super(repositoryInterface);
  }

  @Override
  protected RepositoryFactorySupport createRepositoryFactory(EntityManager entityManager) {

    return new GenericRevisionedRepositoryFactory(entityManager);
  }

  private static class GenericRevisionedRepositoryFactory extends JpaRepositoryFactory {

    /**
     * The constructor.
     *
     * @param entityManager the {@link EntityManager}.
     */
    public GenericRevisionedRepositoryFactory(EntityManager entityManager) {

      super(entityManager);
    }

    @Override
    protected Class<?> getRepositoryBaseClass(RepositoryMetadata metadata) {

      return GenericRevisionedRepositoryImpl.class;
    }

  }

}