package ${package}.general.service.impl.rest;

import javax.annotation.security.PermitAll;
import javax.inject.Named;
import javax.transaction.Transactional;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

import ${package}.general.service.api.rest.I18nRestService;
import com.devonfw.module.i18n.locale.impl.I18nImpl;
import com.devonfw.module.i18n.locale.impl.LocaleNlsResourceImpl;

/**
 * Implementation of {@link I18nRestService}.
 */
@Named
@Transactional
public class I18nRestServiceImpl implements I18nRestService {
  @Override
  @PermitAll
  public String getResourcesForLocale(@PathParam("locale") String locale, @QueryParam("filter") String filter)
      throws Exception {

    return new I18nImpl(new LocaleNlsResourceImpl()).getResourceObject(locale, filter);
  }

}
