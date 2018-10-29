package com.devonfw.module.jpa.dataaccess.impl.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;

import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;

import com.devonfw.module.basic.common.api.entity.RevisionedEntity;
import com.devonfw.module.jpa.dataaccess.api.AdvancedRevisionEntity;
import com.devonfw.module.jpa.dataaccess.api.QueryUtil;
import com.devonfw.module.jpa.dataaccess.api.RevisionMetadata;
import com.devonfw.module.jpa.dataaccess.api.RevisionMetadataType;
import com.devonfw.module.jpa.dataaccess.api.data.GenericRevisionedRepository;
import com.devonfw.module.jpa.dataaccess.impl.LazyRevisionMetadata;
import com.querydsl.core.alias.Alias;
import com.querydsl.jpa.impl.JPAQuery;

/**
 * Implementation of {@link GenericRevisionedRepository}.
 *
 * @param <E> generic type of the managed {@link #getEntityClass() entity}.
 * @param <ID> generic type of the {@link com.devonfw.module.basic.common.api.entity.PersistenceEntity#getId() primary key}
 *        of the entity.
 *
 * @since 3.0.0
 */
public class GenericRevisionedRepositoryImpl<E, ID extends Serializable> extends GenericRepositoryImpl<E, ID>
    implements GenericRevisionedRepository<E, ID> {

  /**
   * The constructor.
   *
   * @param entityInformation the {@link JpaEntityInformation}.
   * @param entityManager the JPA {@link EntityManager}.
   */
  public GenericRevisionedRepositoryImpl(JpaEntityInformation<E, ID> entityInformation, EntityManager entityManager) {

    super(entityInformation, entityManager);
  }

  @Override
  public E find(ID id, Number revision) {

    AuditReader auditReader = AuditReaderFactory.get(this.entityManager);
    E entity = auditReader.find(this.entityInformation.getJavaType(), id, revision);
    if (entity instanceof RevisionedEntity) {
      ((RevisionedEntity<?>) entity).setRevision(revision);
    }
    return entity;
  }

  @SuppressWarnings("unchecked")
  @Override
  public List<RevisionMetadata> getRevisionHistoryMetadata(ID id, boolean lazy) {

    AuditReader auditReader = AuditReaderFactory.get(this.entityManager);
    List<Number> revisionList = auditReader.getRevisions(getEntityClass(), id);
    if (revisionList.isEmpty()) {
      return Collections.emptyList();
    }
    if (lazy) {
      List<RevisionMetadata> result = new ArrayList<>(revisionList.size());
      for (Number revision : revisionList) {
        Long revisionLong = Long.valueOf(revision.longValue());
        result.add(new LazyRevisionMetadata(this.entityManager, revisionLong));
      }
      return result;
    } else {
      AdvancedRevisionEntity rev = Alias.alias(AdvancedRevisionEntity.class);
      JPAQuery<AdvancedRevisionEntity> query = new JPAQuery<AdvancedRevisionEntity>(this.entityManager)
          .from(Alias.$(rev));
      @SuppressWarnings("rawtypes")
      List revList = revisionList;
      QueryUtil.get().whereIn(query, Alias.$(rev.getId()), (List<Long>) revList);
      query.orderBy(Alias.$(rev.getId()).asc());
      List<AdvancedRevisionEntity> resultList = query.fetch();
      return resultList.stream().map(x -> RevisionMetadataType.of(x)).collect(Collectors.toList());
    }
  }

  @Override
  public RevisionMetadata getLastRevisionHistoryMetadata(ID id) {

    AuditReader auditReader = AuditReaderFactory.get(this.entityManager);
    List<Number> revisionList = auditReader.getRevisions(getEntityClass(), id);
    if (revisionList.isEmpty()) {
      return null;
    }
    Number lastRevision = revisionList.get(revisionList.size() - 1);
    AdvancedRevisionEntity revisionEntity = this.entityManager.find(AdvancedRevisionEntity.class, lastRevision);
    if (revisionEntity == null) {
      throw new IllegalStateException("Could not find AdvancedRevisionEntity for ID '" + id + "'.");
    }
    return RevisionMetadataType.of(revisionEntity);
  }

}
