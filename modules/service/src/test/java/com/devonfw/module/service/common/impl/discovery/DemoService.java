package com.devonfw.module.service.common.impl.discovery;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

/**
 * Dummy REST service for testing.
 */
@Path("/demo/v1/")
public interface DemoService {

  /**
   * @return dummy for testing.
   */
  @GET
  @Path("hello")
  public String hello();

}
