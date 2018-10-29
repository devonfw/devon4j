package ${package}.general.logic.impl;

import java.sql.Blob;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.inject.Named;

import ${package}.general.dataaccess.api.BinaryObjectEntity;
import ${package}.general.dataaccess.api.dao.BinaryObjectRepository;
import ${package}.general.common.api.security.ApplicationAccessControlConfig;
import ${package}.general.logic.api.UcManageBinaryObject;
import ${package}.general.logic.api.to.BinaryObjectEto;
import ${package}.general.logic.base.AbstractUc;

/**
 * Implementation of {@link UcManageBinaryObject}.
 */
@Named
public class UcManageBinaryObjectImpl extends AbstractUc implements UcManageBinaryObject {

  @Inject
  private BinaryObjectRepository binaryObjectRepository;

  @Override
  @RolesAllowed(ApplicationAccessControlConfig.PERMISSION_SAVE_BINARY_OBJECT)
  public BinaryObjectEto saveBinaryObject(Blob data, BinaryObjectEto binaryObjectEto) {

    BinaryObjectEntity binaryObjectEntity = getBeanMapper().map(binaryObjectEto, BinaryObjectEntity.class);
    binaryObjectEntity.setData(data);
    this.binaryObjectRepository.save(binaryObjectEntity);
    return getBeanMapper().map(binaryObjectEntity, BinaryObjectEto.class);
  }

  @Override
  @RolesAllowed(ApplicationAccessControlConfig.PERMISSION_DELETE_BINARY_OBJECT)
  public void deleteBinaryObject(Long binaryObjectId) {

    this.binaryObjectRepository.deleteById(binaryObjectId);
  }

  @Override
  @RolesAllowed(ApplicationAccessControlConfig.PERMISSION_FIND_BINARY_OBJECT)
  public BinaryObjectEto findBinaryObject(Long binaryObjectId) {

    return getBeanMapper().map(this.binaryObjectRepository.find(binaryObjectId), BinaryObjectEto.class);
  }

  @Override
  @RolesAllowed(ApplicationAccessControlConfig.PERMISSION_FIND_BINARY_OBJECT)
  public Blob getBinaryObjectBlob(Long binaryObjectId) {

    return this.binaryObjectRepository.find(binaryObjectId).getData();
  }

}
