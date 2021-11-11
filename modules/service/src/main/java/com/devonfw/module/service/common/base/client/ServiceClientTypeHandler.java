package com.devonfw.module.service.common.base.client;

import com.devonfw.module.service.common.api.client.context.ServiceContext;
import com.devonfw.module.service.common.api.constants.ServiceConstants;

/**
 * Interface for detection and handling of service types such as REST or SOAP.
 *
 * @see com.devonfw.module.service.common.api.client.async.AsyncServiceClientFactory
 * @see com.devonfw.module.service.common.api.client.sync.SyncServiceClientFactory
 * @since 2021.04.003
 */
public interface ServiceClientTypeHandler {

  /**
   * @param context the {@link ServiceContext}.
   * @return the {@link ServiceContext#getUrl() URL} with additional post-processing such as resolving variables.
   */
  default String getUrl(ServiceContext<?> context) {

    String url = context.getUrl();
    url = url.replace(ServiceConstants.VARIABLE_TYPE, getServiceTypeFolderName());
    return url;
  }

  /**
   * @return the resolved value for {@link ServiceConstants#VARIABLE_TYPE} such as e.g.
   *         {@link ServiceConstants#URL_FOLDER_REST}.
   */
  String getServiceTypeFolderName();

  /**
   * @param context the {@link ServiceContext}.
   * @return {@code true} if this implementation is responsibe for creating a service client corresponding to the given
   *         {@link ServiceContext}, {@code false} otherwise.
   */
  boolean isResponsibleForService(ServiceContext<?> context);

}
