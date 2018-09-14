package com.devonfw.module.cxf.common.impl.client.interceptor;

import net.sf.mmm.util.date.api.TimeMeasure;

import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;

/**
 * Implementation of {@link AbstractPhaseInterceptor} that logs the duration time of a service client invocation.
 *
 * @since 3.0.0
 */
public class PerformanceStartInterceptor extends AbstractPhaseInterceptor<Message> {

  /**
   * The constructor.
   */
  public PerformanceStartInterceptor() {
    super(Phase.SETUP);
  }

  @Override
  public void handleMessage(Message message) throws Fault {

    message.getExchange().put(TimeMeasure.class, new TimeMeasure());
  }

}
