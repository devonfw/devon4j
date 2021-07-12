package com.devonfw.module.service.common.base.client;

import javax.ws.rs.Path;

import com.devonfw.module.service.common.api.client.context.ServiceContext;
import com.devonfw.module.service.common.api.constants.ServiceConstants;

/**
 * Implementation of {@link ServiceClientTypeHandler} for REST using JAX-RS.
 *
 * @since 2021.04.003
 */
public class ServiceClientTypeHandlerRest implements ServiceClientTypeHandler {

  private static final ServiceClientTypeHandlerRest INSTANCE = new ServiceClientTypeHandlerRest();

  /**
   * @return the resolved value for {@link ServiceConstants#VARIABLE_TYPE} such as e.g.
   *         {@link ServiceConstants#URL_FOLDER_REST}.
   */
  @Override
  public String getServiceTypeFolderName() {

    return ServiceConstants.URL_FOLDER_REST;
  }

  @Override
  public boolean isResponsibleForService(ServiceContext<?> context) {

    return context.getApi().isAnnotationPresent(Path.class);
  }

  /**
   * @return the singleton instance of this class.
   */
  public static ServiceClientTypeHandlerRest get() {

    return INSTANCE;
  }

}
