package com.devonfw.module.service.common.base.client;

import com.devonfw.module.service.common.api.client.context.ServiceContext;
import com.devonfw.module.service.common.api.constants.ServiceConstants;

/**
 * Abstract base implementation of a partial service client factory.
 *
 * @see com.devonfw.module.service.common.api.client.async.AsyncServiceClientFactory
 * @see com.devonfw.module.service.common.api.client.sync.SyncServiceClientFactory
 * @since 2020.08.001
 */
public abstract class PartialServiceClientFactory {

  /**
   * @param context the {@link ServiceContext}.
   * @return the {@link ServiceContext#getUrl() URL} with additional post-processing such as resolving variables.
   */
  protected String getUrl(ServiceContext<?> context) {

    String url = context.getUrl();
    url = url.replace(ServiceConstants.VARIABLE_TYPE, getServiceTypeFolderName());
    return url;
  }

  /**
   * @return the resolved value for {@link ServiceConstants#VARIABLE_TYPE} such as e.g.
   *         {@link ServiceConstants#URL_FOLDER_REST}.
   */
  protected abstract String getServiceTypeFolderName();

  /**
   * @param context the {@link ServiceContext}.
   * @return the display name of the service for exception or log messages.
   */
  protected String createServiceName(ServiceContext<?> context) {

    return context.getApi().getName();
  }

  /**
   * @param context the {@link ServiceContext}.
   * @return {@code true} if this implementation is responsibe for creating a service client corresponding to the given
   *         {@link ServiceContext}, {@code false} otherwise.
   */
  protected abstract boolean isResponsibleForService(ServiceContext<?> context);

}
