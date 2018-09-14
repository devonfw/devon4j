package com.devonfw.module.cxf.common.impl.client.interceptor;

import net.sf.mmm.util.date.api.TimeMeasure;

import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;
import org.apache.cxf.transport.Conduit;
import org.apache.cxf.ws.addressing.EndpointReferenceType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    TimeMeasure timeMeasure = message.getExchange().get(TimeMeasure.class);
    if (timeMeasure == null) {
      if (!this.errorLogged) {
        LOG.warn("Invalid setup - no TimeMeasure present!");
        this.errorLogged = true;
      }
      return;
    }
    Throwable exception = message.getContent(Exception.class);
    if (exception == null) {
      timeMeasure.succeed();
    }
    Conduit conduit = message.get(Conduit.class);
    EndpointReferenceType target = conduit.getTarget();
    String url = target.getAddress().getValue();
    timeMeasure.log(LOG, url);
  }

}
