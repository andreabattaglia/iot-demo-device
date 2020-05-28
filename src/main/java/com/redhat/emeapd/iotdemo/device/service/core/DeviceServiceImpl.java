/**
 * 
 */
package com.redhat.emeapd.iotdemo.device.service.core;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;

import com.redhat.emeapd.iotdemo.device.domain.CoolingBean;
import com.redhat.emeapd.iotdemo.device.domain.ProductLineBean;
import com.redhat.emeapd.iotdemo.device.domain.ProductionBean;
import com.redhat.emeapd.iotdemo.device.service.cooling.CoolingService;
import com.redhat.emeapd.iotdemo.device.service.messaging.MessagingService;
import com.redhat.emeapd.iotdemo.device.service.production.ProductionService;
import com.redhat.emeapd.iotdemo.device.service.productline.ProductLineService;
import com.redhat.emeapd.iotdemo.device.util.events.CoolingValidationReceived;
import com.redhat.emeapd.iotdemo.device.util.events.CoolingValidationResponseReady;
import com.redhat.emeapd.iotdemo.device.util.events.ProductLineChanged;
import com.redhat.emeapd.iotdemo.device.util.events.ProductionValidationReceived;
import com.redhat.emeapd.iotdemo.device.util.events.ProductionValidationResponseReady;
import com.redhat.emeapd.iotdemo.device.util.events.ValidationEventData;
import com.redhat.emeapd.iotdemo.device.util.events.ValidationReplyReadyData;

import io.quarkus.runtime.StartupEvent;
import io.quarkus.scheduler.Scheduled;

/**
 * @author abattagl
 *
 */
@ApplicationScoped
public class DeviceServiceImpl implements DeviceService {

    @Inject
    Logger LOGGER;

    @Inject
    ProductLineService productLineService;

    @Inject
    ProductionService productionService;

    @Inject
    CoolingService coolingService;

    @Inject
    MessagingService messagingService;

    private final AtomicInteger counter = new AtomicInteger();

    private Lock productionLock;
    private Condition productionCondition;

    private final AtomicBoolean updateProductLine = new AtomicBoolean(true);

    public DeviceServiceImpl() {
	productionLock = new ReentrantLock();
	productionCondition = productionLock.newCondition();
    }

    public void onStart(@Observes StartupEvent ev) {
	LOGGER.info("The application is starting...{}");
    }

    @PostConstruct
    void postConstruct() {
	setProductLineParams();
    }

    void updateProductLine(@Observes @ProductLineChanged ProductLineBean productLine) {
	updateProductLine.set(true);
    }

    private void setProductLineParams() {
	ProductLineBean productLine = productLineService.getProductLine();
	if (LOGGER.isInfoEnabled()) {
	    LOGGER.info("setProductLineParams() - ProductLineBean productLine={}", productLine);
	}

	productionService.setProductLineParams(productLine.getTemperatureAvg(), productLine.getTemperatureDelta(),
		productLine.getRpmAvg(), productLine.getRpmDelta());
	coolingService.setProductLineParams(productLine.getCoolingAvg(), productLine.getCoolingDelta());
    }

    @Scheduled(every = "1000s")
    void run() {
	ProductionBean productionBean = null;
	boolean valid = false;
	if (updateProductLine.getAndSet(false))
	    setProductLineParams();
	productionBean = productionService.produce();

	try {
	    int iteration = counter.incrementAndGet();
	    if (LOGGER.isInfoEnabled()) {
		LOGGER.info("Sending production data #{} to the server for validation \n\t{}", iteration,
			productionBean);
	    }
	    // sends data to the server for validation
	    messagingService.sendProductionData(iteration, productionBean);
	} catch (Exception e) {
	    // TODO Auto-generated catch block
	    LOGGER.error("Device Error", e);
	}
    }

    void evaluateProductionValidationReply(
	    @Observes @ProductionValidationResponseReady ValidationReplyReadyData validationEventData)
	    throws Exception {
	CoolingBean coolingBean = null;
	if (!validationEventData.isValid()) {
	    LOGGER.info("PRODUCT #{} NOT VALIDATED", validationEventData.getIterationId());
	    return;
	}
	LOGGER.info("PRODUCT #{} VALIDATED", validationEventData.getIterationId());
	coolingBean = coolingService.cool();
	// sends data to the server for validation
	messagingService.sendCoolingData(validationEventData.getIterationId(), coolingBean);
    }

    void evaluateCoolingValidationReply(
	    @Observes @CoolingValidationResponseReady ValidationReplyReadyData validationEventData) {
	if (!validationEventData.isValid()) {
	    LOGGER.info("COOLING #{} ERROR", validationEventData.getIterationId());
	    return;
	}
	LOGGER.info("COOLING #{} OK", validationEventData.getIterationId());
    }
}
