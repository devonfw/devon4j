// package com.devonfw.module.logging.common.impl;
//
// import static org.mockito.Mockito.when;
//
// import javax.servlet.FilterConfig;
//
// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.extension.ExtendWith;
// import org.mockito.Mock;
// import org.mockito.junit.jupiter.MockitoExtension;
// import org.springframework.test.util.ReflectionTestUtils;
//
// import com.devonfw.module.test.common.base.ModuleTest;
//
/// **
// *
// */
// @ExtendWith(MockitoExtension.class)
// public class TraceContextFilterTest extends ModuleTest {
//
// private static final String TRACE_ID_HEADER_NAME_PARAM = "traceIdHttpHeaderName";
//
// private static final String TRACE_ID_HEADER_NAME_PARAM_FIELD_NAME = "TRACE_ID_HEADER_NAME_PARAM";
//
// private static final String SPAN_ID_HEADER_NAME_PARAM = "spanIdHttpHeaderName";
//
// private static final String SPAN_ID_HEADER_NAME_PARAM_FIELD_NAME = "SPAN_ID_HEADER_NAME_PARAM";
//
// @Mock
// private FilterConfig config;
//
// /**
// *
// */
// @Test
// public void testTraceAndSpanIdHttpHeaderNameAfterConstructor() {
//
// // setup
// TraceContextFilter filter = new TraceContextFilter();
//
// // exercise
// String traceIdHttpHeaderName = (String) ReflectionTestUtils.getField(filter, TRACE_ID_HEADER_NAME_PARAM);
// String spanIdHttpHeaderName = (String) ReflectionTestUtils.getField(filter, SPAN_ID_HEADER_NAME_PARAM);
//
// // verify
// assertThat(traceIdHttpHeaderName).isNotNull();
// assertThat(spanIdHttpHeaderName).isNotNull();
// }
//
// /**
// * @throws Exception
// */
// @Test
// public void testInitWithNullInitParameter() throws Exception {
//
// // setup
// TraceContextFilter filter = new TraceContextFilter();
// String traceIdField = (String) ReflectionTestUtils.getField(TraceContextFilter.class,
// TRACE_ID_HEADER_NAME_PARAM_FIELD_NAME);
// String spanIdField = (String) ReflectionTestUtils.getField(TraceContextFilter.class,
// SPAN_ID_HEADER_NAME_PARAM_FIELD_NAME);
//
// assertThat(traceIdField).isNotNull();
// assertThat(spanIdField).isNotNull();
//
// when(this.config.getInitParameter(traceIdField)).thenReturn(null);
// when(this.config.getInitParameter(spanIdField)).thenReturn(null);
//
// // exercise
// filter.init(this.config);
//
// // verify
// String traceIdHttpHeaderName = (String) ReflectionTestUtils.getField(filter, TRACE_ID_HEADER_NAME_PARAM);
// assertThat(traceIdHttpHeaderName).isNotNull().isEqualTo(TraceContextFilter.TRACE_ID_HEADER_NAME_DEFAULT);
//
// String spanIdHttpHeaderName = (String) ReflectionTestUtils.getField(filter, SPAN_ID_HEADER_NAME_PARAM);
// assertThat(spanIdHttpHeaderName).isNotNull().isEqualTo(TraceContextFilter.SPAN_ID_HEADER_NAME_DEFAULT);
// }
//
// /**
// * @throws Exception
// */
// @Test
// public void testInitWithNonDefaultParameter() throws Exception {
//
// // setup
// TraceContextFilter filter = new TraceContextFilter();
// String traceIdField = (String) ReflectionTestUtils.getField(TraceContextFilter.class,
// TRACE_ID_HEADER_NAME_PARAM_FIELD_NAME);
// String spanIdField = (String) ReflectionTestUtils.getField(TraceContextFilter.class,
// SPAN_ID_HEADER_NAME_PARAM_FIELD_NAME);
//
// assertThat(traceIdField).isNotNull();
// assertThat(spanIdField).isNotNull();
//
// String nonDefaultParameter = "test";
//
// when(this.config.getInitParameter(traceIdField)).thenReturn(nonDefaultParameter);
// when(this.config.getInitParameter(spanIdField)).thenReturn(nonDefaultParameter);
//
// // exercise
// filter.init(this.config);
//
// // verify
// String traceIdHttpHeaderName = (String) ReflectionTestUtils.getField(filter, TRACE_ID_HEADER_NAME_PARAM);
// assertThat(traceIdHttpHeaderName).isEqualTo(nonDefaultParameter);
//
// String spanIdHttpHeaderName = (String) ReflectionTestUtils.getField(filter, SPAN_ID_HEADER_NAME_PARAM);
// assertThat(spanIdHttpHeaderName).isEqualTo(nonDefaultParameter);
// }
//
// }
