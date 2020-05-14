package com.redhat.emeapd.iotdemo.device.rest;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;

import com.redhat.emeapd.iotdemo.device.domain.ProductLineBean;
import com.redhat.emeapd.iotdemo.device.service.productline.ProductLineService;

@Path("/productline")
public class ProductionLineResource {

    @Inject
    Logger LOGGER;

    @Inject
    ProductLineService productLineService;

    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    public void productLine(@FormParam(value = "") int temperatureAvg, //
	    @FormParam(value = "temperatureDelta") int temperatureDelta, //
	    @FormParam(value = "rpmAvg") int rpmAvg, //
	    @FormParam(value = "rpmDelta") int rpmDelta, //
	    @FormParam(value = "coolingAvg") int coolingAvg, //
	    @FormParam(value = "coolingDelta") int coolingDelta) {
	productLineService.newProductLine(temperatureAvg, temperatureDelta, rpmAvg, rpmDelta, coolingAvg, coolingDelta);
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public void productLine(ProductLineBean productLine) {
	productLineService.newProductLine(productLine);
    }
}