package com.devonfw.module.service.common.impl.header;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.devonfw.module.logging.common.api.LoggingConstants;
import com.devonfw.module.service.common.api.header.ServiceHeaderContext;
import com.devonfw.module.service.common.api.header.ServiceHeaderCustomizer;

import io.jaegertracing.Configuration;
import io.jaegertracing.Configuration.ReporterConfiguration;
import io.jaegertracing.Configuration.SamplerConfiguration;
import io.opentracing.Scope;
import io.opentracing.Span;
import io.opentracing.Tracer;
import io.opentracing.log.Fields;
import io.opentracing.tag.Tags;

/**
 *
 */
public class ServiceHeaderCustomizerTraceSpanId implements ServiceHeaderCustomizer {

  private static final Logger LOG = LoggerFactory.getLogger(ServiceHeaderCustomizerTraceSpanId.class);

  /**
   * The constructor.
   */
  public ServiceHeaderCustomizerTraceSpanId() {

    super();
  }

  /**
   * This method is used to create tracer with jaeger tracing.
   *
   * @return {@link Tracer}
   */
  public Tracer initTracer() {

    SamplerConfiguration samplerConfig = SamplerConfiguration.fromEnv().withType("const").withParam(1);
    ReporterConfiguration reporterConfig = ReporterConfiguration.fromEnv().withLogSpans(true);
    Configuration config = new Configuration("service").withSampler(samplerConfig).withReporter(reporterConfig);
    return config.getTracer();
  }

  // private final String traceIdHeaderName;
  //
  // private final String spanIdHeaderName;
  //
  // /**
  // * The constructor.
  // */
  // public ServiceHeaderCustomizerTraceSpanId() {
  //
  // this(TraceContextFilter.TRACE_ID_HEADER_NAME_DEFAULT, TraceContextFilter.SPAN_ID_HEADER_NAME_DEFAULT);
  // }
  //
  // /**
  // * The constructor.
  // *
  // * @param traceIdHeaderName
  // * @param spanIdHeaderName
  // */
  // public ServiceHeaderCustomizerTraceSpanId(String traceIdHeaderName, String spanIdHeaderName) {
  //
  // super();
  // this.traceIdHeaderName = traceIdHeaderName;
  // this.spanIdHeaderName = spanIdHeaderName;
  // }

  @Override
  public void addHeaders(ServiceHeaderContext<?> context) {

    // String traceId = MDC.get(LoggingConstants.TRACE_ID);
    // String spanId = MDC.get(LoggingConstants.SPAN_ID);
    //
    // if (!StringUtils.isEmpty(traceId)) {
    // context.setHeader(this.traceIdHeaderName, traceId);
    // }
    //
    // if (!StringUtils.isEmpty(spanId)) {
    // context.setHeader(this.spanIdHeaderName, spanId);
    // }

    Tracer tracer = initTracer();

    Span span = tracer.buildSpan(LoggingConstants.SPAN_NAME).start();
    LOG.info("new span {} has been created for the http request {}.", context.getUrl(), span.context().toSpanId());

    try (Scope scope = tracer.scopeManager().activate(span)) {

      context.setHeader(LoggingConstants.TRACE_ID, span.context().toTraceId());
      context.setHeader(LoggingConstants.SPAN_ID, span.context().toSpanId());

    } catch (Exception ex) {
      Tags.ERROR.set(span, true);
      span.log(Map.of(Fields.EVENT, "error", Fields.ERROR_OBJECT, ex, Fields.MESSAGE, ex.getMessage()));
    } finally {
      span.finish();
    }

  }

}
