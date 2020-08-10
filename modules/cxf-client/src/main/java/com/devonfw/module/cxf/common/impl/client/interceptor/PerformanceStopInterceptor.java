package com.devonfw.module.cxf.common.impl.client.interceptor;

import java.lang.reflect.Method;

import org.apache.cxf.endpoint.Endpoint;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.message.Exchange;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;
import org.apache.cxf.service.model.EndpointInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.devonfw.module.service.common.base.client.ServiceClientPerformanceLogger;

/**
 * Implementation of {@link AbstractPhaseInterceptor} that logs the duration time of a service client invocation.
 *
 * @since 3.0.0
 */
public class PerformanceStopInterceptor extends AbstractPhaseInterceptor<Message> {

  private static final Logger LOG = LoggerFactory.getLogger(PerformanceStopInterceptor.class);

  private boolean errorLogged;

  /**
   * The constructor.
   */
  public PerformanceStopInterceptor() {

    super(Phase.POST_INVOKE);
  }

  @Override
  public void handleMessage(Message message) throws Fault {

    Exchange exchange = message.getExchange();
    SystemNanoTime start = exchange.get(SystemNanoTime.class);
    if (start == null) {
      if (!this.errorLogged) {
        LOG.warn("Invalid setup - no SystemNanoTime present!");
        this.errorLogged = true;
      }
      return;
    }
    Endpoint endpoint = exchange.getEndpoint();
    Throwable exception = message.getContent(Exception.class);
    EndpointInfo endpointInfo = endpoint.getEndpointInfo();
    String url = endpointInfo.getAddress();
    Method method = exchange.get(Method.class);
    String service;
    if (method != null) {
      service = method.getDeclaringClass().getName() + "#" + method.getName();
    } else {
      service = endpointInfo.getName().toString();
    }
    service = service + "[" + url + "]";
    Integer statusCode = (Integer) exchange.get(Message.RESPONSE_CODE);
    ServiceClientPerformanceLogger.log(start.getNanoTime(), service, statusCode.intValue(), exception);
  }

}
