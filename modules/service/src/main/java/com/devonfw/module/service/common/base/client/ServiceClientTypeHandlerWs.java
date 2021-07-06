package com.devonfw.module.service.common.base.client;

import javax.ws.rs.Path;

import com.devonfw.module.service.common.api.client.context.ServiceContext;
import com.devonfw.module.service.common.api.constants.ServiceConstants;

/**
 * Implementation of {@link ServiceClientTypeHandler} for REST using JAX-RS.
 *
 * @since 2021.04.003
 */
public class ServiceClientTypeHandlerWs implements ServiceClientTypeHandler {

  private static final ServiceClientTypeHandlerWs INSTANCE = new ServiceClientTypeHandlerWs();

  @Override
  public String getUrl(ServiceContext<?> context) {

    String url = ServiceClientTypeHandler.super.getUrl(context);
    if (!url.endsWith(ServiceConstants.WSDL_SUFFIX)) {
      String serviceName = context.getApi().getSimpleName();
      if (!url.endsWith(serviceName)) {
        if (!url.endsWith("/")) {
          url = url + "/";
        }
        url = url + serviceName;
      }
      url = url + ServiceConstants.WSDL_SUFFIX;
    }
    return url;
  }

  /**
   * @return the resolved value for {@link ServiceConstants#VARIABLE_TYPE} such as e.g.
   *         {@link ServiceConstants#URL_FOLDER_REST}.
   */
  @Override
  public String getServiceTypeFolderName() {

    return ServiceConstants.URL_FOLDER_WEB_SERVICE;
  }

  @Override
  public boolean isResponsibleForService(ServiceContext<?> context) {

    return context.getApi().isAnnotationPresent(Path.class);
  }

  /**
   * @return the singleton instance of this class.
   */
  public static ServiceClientTypeHandlerWs get() {

    return INSTANCE;
  }

}
