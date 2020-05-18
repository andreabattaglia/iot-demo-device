/**
 * 
 */
package com.redhat.emeapd.iotdemo.device.service.cooling;

import java.util.PrimitiveIterator;
import java.util.Random;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.slf4j.Logger;

import com.redhat.emeapd.iotdemo.device.domain.CoolingBean;
import com.redhat.emeapd.iotdemo.device.util.random.RandomIntGeneratorProducer;

/**
 * @author abattagl
 *
 */
@ApplicationScoped
class CoolingServiceImpl implements CoolingService {
    @Inject
    RandomIntGeneratorProducer randomIntGeneratorProducer;

    private PrimitiveIterator.OfInt randomCoolingIterator;

    /**
     * Logger for this class
     */
    @Inject
    Logger LOGGER;

    @Override
    public void setProductLineParams(int coolingAvg, int coolingDelta) {
	if (LOGGER.isInfoEnabled()) {
	    LOGGER.info(
		    "\nSetting cooling parameters:\n\tcoolingAvg = {}\n\tcoolingDelta = {}",
		    coolingAvg, coolingDelta);
	}
	randomCoolingIterator = randomIntGeneratorProducer.intRandomNumberGenerator(coolingAvg, coolingDelta);
    }

    @Override
    public CoolingBean cool() {
	CoolingBean coolingBean = new CoolingBean();
	coolingBean.setCooling(randomCoolingIterator.nextInt());
	return coolingBean;
    }

}
