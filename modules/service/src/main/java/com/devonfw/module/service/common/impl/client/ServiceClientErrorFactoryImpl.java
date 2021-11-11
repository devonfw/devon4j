package com.devonfw.module.service.common.impl.client;

import java.util.UUID;

import net.sf.mmm.util.exception.api.ServiceInvocationFailedException;

import com.devonfw.module.service.common.api.client.ServiceClientErrorFactory;
import com.devonfw.module.service.common.base.client.AbstractServiceClientErrorFactory;

/**
 * An Implementation of {@link ServiceClientErrorFactory} that converts a REST failure response compliant with
 * <a href="https://github.com/devonfw/devon4j/blob/develop/documentation/guide-rest.asciidoc#error-results">devonfw
 * REST error specification</a> to a {@link ServiceInvocationFailedException}.
 *
 * @since 2020.08.001
 */
public class ServiceClientErrorFactoryImpl extends AbstractServiceClientErrorFactory {

  /**
   * The constructor.
   */
  public ServiceClientErrorFactoryImpl() {

    super();
  }

  @Override
  public RuntimeException create(Throwable cause, String message, String code, UUID uuid, String service) {

    return new ServiceInvocationFailedException(cause, message, code, uuid, service);
  }

}
