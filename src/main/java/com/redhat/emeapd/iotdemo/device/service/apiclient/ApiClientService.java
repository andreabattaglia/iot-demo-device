/**
 * 
 */
package com.redhat.emeapd.iotdemo.device.service.apiclient;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.jboss.resteasy.annotations.jaxrs.PathParam;

/**
 * @author abattagl
 *
 */
@Path("/v1")
@RegisterRestClient(configKey="factory-api")
public interface ApiClientService {

    @GET
    @Path("/production/{data}")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.TEXT_PLAIN)
    String sendProductionData(@PathParam String data) throws Exception;

    @GET
    @Path("/cooling/{data}")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.TEXT_PLAIN)
    String sendCoolingData(@PathParam String data) throws Exception;
}
