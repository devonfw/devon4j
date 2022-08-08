package ${package}.worldmanagement.service;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;

/**
 * Simple class to test REST Service.
 */
@ApplicationScoped
@Named
@javax.inject.Named
@Path("customermanagement/v1")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class WorldmanagementRestService {

  @GET
  @Path("world")
  public World getCustomer() {

    // To test that jackson integration works and JSON can be mapped
    World world = new World();
    world.setName("Hello world!");
    return world;
  }

  @GET
  @Path("error")
  public World getError() {

    // To test RestServiceExceptionFacade
    throw new IllegalStateException();
  }

}
