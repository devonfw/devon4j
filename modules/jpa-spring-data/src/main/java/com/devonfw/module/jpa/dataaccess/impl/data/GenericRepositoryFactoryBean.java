package com.devonfw.module.jpa.dataaccess.impl.data;

import java.io.Serializable;

import javax.persistence.EntityManager;

import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;

import com.devonfw.module.jpa.dataaccess.api.data.GenericRepository;

/**
 * {@link org.springframework.data.repository.core.support.RepositoryFactoryBeanSupport} for
 * {@link GenericRepositoryImpl}. In order to use {@link GenericRepository} you need to annotate your spring-boot
 * application as following:
 *
 * <pre>
 * &#64;{@link org.springframework.data.jpa.repository.config.EnableJpaRepositories}(repositoryFactoryBeanClass = {@link GenericRepositoryFactoryBean}.class)
 * </pre>
 *
 * @param <R> generic type of the {@link GenericRepository} interface.
 * @param <E> generic type of the managed {@link com.devonfw.module.jpa.dataaccess.api.PersistenceEntity entity}.
 * @param <ID> generic type of the {@link com.devonfw.module.jpa.dataaccess.api.PersistenceEntity#getId() primary key}
 *        of the entity.
 *
 * @since 3.0.0
 */
public class GenericRepositoryFactoryBean<R extends GenericRepository<E, ID>, E, ID extends Serializable>
    extends JpaRepositoryFactoryBean<R, E, ID> {

  /**
   * Creates a new {@link JpaRepositoryFactoryBean} for the given repository interface.
   *
   * @param repositoryInterface must not be {@literal null}.
   */
  public GenericRepositoryFactoryBean(Class<? extends R> repositoryInterface) {

    super(repositoryInterface);
  }

  @Override
  protected RepositoryFactorySupport createRepositoryFactory(EntityManager entityManager) {

    return new GenericRepositoryFactory(entityManager);
  }

  private static class GenericRepositoryFactory extends JpaRepositoryFactory {

    /**
     * The constructor.
     *
     * @param entityManager the {@link EntityManager}.
     */
    public GenericRepositoryFactory(EntityManager entityManager) {

      super(entityManager);
    }

    @Override
    protected Class<?> getRepositoryBaseClass(RepositoryMetadata metadata) {

      return GenericRepositoryImpl.class;
    }

  }

}