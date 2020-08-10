package com.devonfw.test.app.myexample.service.api.rest;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import com.devonfw.module.rest.common.api.RestService;

/**
 * Simple {@link RestService} as example for testing.
 */
@Path("/my-example/v1")
public interface MyExampleRestService extends RestService {

  /**
   * @param name the name to greet.
   * @return the greeting.
   */
  @GET
  @Path("/greet/{name}")
  String greet(@PathParam("name") String name);

  /**
   * @param example the posted {@link MyExampleTo}.
   * @return the updated {@link MyExampleTo}.
   */
  @POST
  @Path("/example")
  MyExampleTo saveExample(MyExampleTo example);

  /**
   * Method that will throw a business exception to test error mapping.
   */
  @GET
  @Path("/business-error")
  void businessError();

  /**
   * Method that will throw a technical exception to test error mapping.
   */
  @GET
  @Path("/technical-error")
  void technicalError();

}
