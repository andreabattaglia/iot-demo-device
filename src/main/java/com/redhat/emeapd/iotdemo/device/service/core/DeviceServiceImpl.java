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

import org.slf4j.Logger;

import com.redhat.emeapd.iotdemo.device.domain.CoolingBean;
import com.redhat.emeapd.iotdemo.device.domain.ProductLineBean;
import com.redhat.emeapd.iotdemo.device.domain.ProductionBean;
import com.redhat.emeapd.iotdemo.device.service.cooling.CoolingService;
import com.redhat.emeapd.iotdemo.device.service.messaging.MessagingService;
import com.redhat.emeapd.iotdemo.device.service.production.ProductionService;
import com.redhat.emeapd.iotdemo.device.service.productline.ProductLineService;
import com.redhat.emeapd.iotdemo.device.util.events.ProductLineChanged;

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

    void observeImportantMessage(@Observes @ProductLineChanged ProductLineBean productLine) {
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

    @Scheduled(every = "10s")
    void run() {
	ProductionBean productionBean = null;
	CoolingBean coolingBean = null;
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
	    valid = messagingService.sendProductionData(productionBean);

	    // productionBean = null;
	    if (!valid) {
		LOGGER.error("PRODUCT #{} NOT VALIDATED", iteration);
		return;
	    }
	    LOGGER.error("PRODUCT #{} VALIDATED", iteration);
	    coolingBean = coolingService.cool();
	    // sends data to the server for validation
	    valid = messagingService.sendCoolingData(coolingBean);
	    if (!valid) {
		LOGGER.error("COOLING #{} ERROR", iteration);
		return;
	    }
	    LOGGER.error("COOLING #{} OK", iteration);
	} catch (Exception e) {
	    // TODO Auto-generated catch block
	    LOGGER.error("Device Error", e);
	}
    }
}
