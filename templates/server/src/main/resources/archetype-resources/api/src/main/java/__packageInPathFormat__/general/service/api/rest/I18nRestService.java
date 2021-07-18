package ${package}.general.service.api.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

import com.devonfw.module.rest.common.api.RestService;

/**
 * The Internationlization REST service provides access to i18n module.
 */
@Path("/i18n/v1")
public interface I18nRestService extends RestService {
  /**
   * @param locale for locale
   * @param filter
   * @return JSON string
   * @throws Exception
   */
  @GET
  @Path("/locales/{locale}/")
  public String getResourcesForLocale(@PathParam("locale") String locale, @QueryParam("filter") String filter)
      throws Exception;

}
