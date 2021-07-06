package com.devonfw.module.cxf.common.impl.client.ws;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jws.WebService;
import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Service;

import org.apache.cxf.endpoint.Client;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.message.Message;
import org.apache.cxf.transport.Conduit;
import org.apache.cxf.transport.http.HTTPConduit;

import com.devonfw.module.cxf.common.impl.client.SyncServiceClientFactoryCxf;
import com.devonfw.module.service.common.api.client.context.ServiceContext;
import com.devonfw.module.service.common.api.client.sync.SyncServiceClientFactory;
import com.devonfw.module.service.common.api.config.ServiceConfig;
import com.devonfw.module.service.common.base.client.ServiceClientTypeHandler;
import com.devonfw.module.service.common.base.client.ServiceClientTypeHandlerWs;

/**
 * Implementation of {@link SyncServiceClientFactory} for JAX-WS SOAP service clients using Apache CXF.
 *
 * @since 3.0.0
 */
public class SyncServiceClientFactoryCxfWs extends SyncServiceClientFactoryCxf {

  @Override
  protected ServiceClientTypeHandler getTypeHandler() {

    return ServiceClientTypeHandlerWs.get();
  }

  @Override
  protected <S> S createService(ServiceContext<S> context, String url) {

    Class<S> api = context.getApi();
    WebService webService = api.getAnnotation(WebService.class);
    QName qname = new QName(getNamespace(api, webService), getLocalName(api, webService));
    boolean downloadWsdl = context.getConfig().getChild(ServiceConfig.KEY_SEGMENT_WSDL)
        .getChild(ServiceConfig.KEY_SEGMENT_DISABLE_DOWNLOAD).getValueAsBoolean();
    URL wsdlUrl = null;
    if (downloadWsdl) {
      try {
        wsdlUrl = new URL(url);
      } catch (MalformedURLException e) {
        throw new IllegalArgumentException("Illegal URL: " + url, e);
      }
    }
    S serviceClient = Service.create(wsdlUrl, qname).getPort(api);
    if (!downloadWsdl) {
      BindingProvider bindingProvider = (BindingProvider) serviceClient;
      bindingProvider.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, url);
    }
    applyAspects(context, serviceClient);
    return serviceClient;
  }

  @Override
  protected <S> void applyAspects(ServiceContext<S> context, S serviceClient) {

    Client cxfClient = ClientProxy.getClient(serviceClient);
    applyInterceptors(context, cxfClient);
    Conduit conduit = cxfClient.getConduit();
    if (conduit instanceof HTTPConduit) {
      HTTPConduit httpConduit = (HTTPConduit) conduit;
      applyClientPolicy(context, httpConduit);
    }
    applyHeaders(context, cxfClient);
  }

  private String getLocalName(Class<?> api, WebService webService) {

    String portName = webService.portName();
    if (portName.isEmpty()) {
      return api.getSimpleName();
    }
    return portName;
  }

  private String getNamespace(Class<?> api, WebService webService) {

    String targetNamespace = webService.targetNamespace();
    if (targetNamespace.isEmpty()) {
      return api.getPackage().getName();
    }
    return targetNamespace;
  }

  @Override
  protected void applyHeaders(ServiceContext<?> context, Object client) {

    Collection<String> headerNames = context.getHeaderNames();
    if (!headerNames.isEmpty()) {
      Map<String, List<String>> headers = new HashMap<>();
      for (String headerName : headerNames) {
        headers.put(headerName, Arrays.asList(context.getHeader(headerName)));
      }
      ((Client) client).getRequestContext().put(Message.PROTOCOL_HEADERS, headers);
    }
  }

}
