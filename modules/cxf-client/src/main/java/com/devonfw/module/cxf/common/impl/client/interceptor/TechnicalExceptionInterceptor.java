package com.devonfw.module.cxf.common.impl.client.interceptor;

import net.sf.mmm.util.exception.api.ServiceInvocationFailedException;

import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;;

/**
 * Implementation of {@link AbstractPhaseInterceptor} to handle technical errors like {@link java.net.ConnectException}
 * or {@link java.net.SocketTimeoutException}.
 *
 * @since 3.0.0
 */
public class TechnicalExceptionInterceptor extends AbstractPhaseInterceptor<Message> {

  private final String service;

  /**
   * The constructor.
   *
   * @param service the name (e.g. {@link Class#getName() qualified name}) of the
   *        {@link com.devonfw.module.service.common.api.Service} that failed.
   */
  public TechnicalExceptionInterceptor(String service) {

    super(Phase.PRE_PROTOCOL);
    this.service = service;
  }

  @Override
  public void handleMessage(Message message) throws Fault {

    Throwable exception = message.getContent(Exception.class);
    if (exception != null) {
      message.getExchange().put("wrap.in.processing.exception", Boolean.FALSE);
      throw new ServiceInvocationFailedException(exception, exception.toString(), "ServiceInvoke", null, this.service);
    }
  }

}
