package com.devonfw.module.rest.common.api;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.devonfw.module.service.common.api.Service;

/**
 * This is a marker interface for a REST {@link Service}. It is recommended to extend your REST API interfaces from this
 * {@link RestService} interface to make your life easier. However, you are not forced to do so. See JavaDoc of
 * {@link Service} for further details.
 *
 * @since 3.0.0
 */
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface RestService extends Service {

}
