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

import com.redhat.emeapd.iotdemo.device.domain.CoolingBean;
import com.redhat.emeapd.iotdemo.device.domain.ProductionBean;

/**
 * @author abattagl
 *
 */
@Path("/v1")
@RegisterRestClient(configKey="factory-api")
public interface ApiClientService {

    @GET
    @Path("/production/{data}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    boolean sendProductionData(@PathParam ProductionBean data) throws Exception;

    @GET
    @Path("/cooling/{data}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    boolean sendCoolingData(@PathParam CoolingBean data) throws Exception;
}
