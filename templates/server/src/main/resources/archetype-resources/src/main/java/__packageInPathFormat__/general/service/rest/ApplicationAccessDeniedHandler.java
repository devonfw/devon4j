package ${package}.general.service.rest;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Response;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

/**
 * Implementation of {@link AccessDeniedHandler}.
 */
@ApplicationScoped
@Named("ApplicationAccessDeniedHandler")
@javax.inject.Named
public class ApplicationAccessDeniedHandler implements AccessDeniedHandler {

  @Inject
  private RestServiceExceptionFacade exceptionFacade;

  @Override
  public void handle(HttpServletRequest request, HttpServletResponse response,
      AccessDeniedException accessDeniedException) throws IOException, ServletException {

    Response restResponse = this.exceptionFacade.toResponse(accessDeniedException);
    Object entity = restResponse.getEntity();
    response.setStatus(restResponse.getStatus());
    if (entity != null) {
      response.getWriter().write(entity.toString());
    }
  }

}
