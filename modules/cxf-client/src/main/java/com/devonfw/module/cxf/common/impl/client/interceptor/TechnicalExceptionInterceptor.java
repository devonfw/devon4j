package com.devonfw.module.cxf.common.impl.client.interceptor;

import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;

import com.devonfw.module.service.common.api.client.ServiceClientErrorFactory;
import com.devonfw.module.service.common.api.client.context.ServiceContext;;

/**
 * Implementation of {@link AbstractPhaseInterceptor} to handle technical errors like {@link java.net.ConnectException}
 * or {@link java.net.SocketTimeoutException}.
 *
 * @since 3.0.0
 */
public class TechnicalExceptionInterceptor extends AbstractPhaseInterceptor<Message> {

  private final ServiceClientErrorFactory errorFactory;

  private final ServiceContext<?> context;

  /**
   * The constructor.
   *
   * @param errorFactory the {@link ServiceClientErrorFactory}.
   * @param context the {@link ServiceContext}.
   */
  public TechnicalExceptionInterceptor(ServiceClientErrorFactory errorFactory, ServiceContext<?> context) {

    super(Phase.PRE_PROTOCOL);
    this.errorFactory = errorFactory;
    this.context = context;
  }

  @Override
  public void handleMessage(Message message) throws Fault {

    Throwable exception = message.getContent(Exception.class);
    if (exception != null) {
      message.getExchange().put("wrap.in.processing.exception", Boolean.FALSE);
      String url = message.getExchange().getEndpoint().getEndpointInfo().getAddress();
      throw this.errorFactory.create(exception, exception.toString(), null, null,
          this.context.getServiceDescription(null, url));
    }
  }

}
