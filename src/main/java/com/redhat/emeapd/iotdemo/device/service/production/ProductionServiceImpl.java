/**
 * 
 */
package com.redhat.emeapd.iotdemo.device.service.production;

import java.util.PrimitiveIterator;
import java.util.Random;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.redhat.emeapd.iotdemo.device.domain.ProductionBean;
import com.redhat.emeapd.iotdemo.device.util.random.RandomIntGeneratorProducer;

/**
 * @author abattagl
 *
 */
@ApplicationScoped
class ProductionServiceImpl implements ProductionService {
    @Inject
    RandomIntGeneratorProducer randomIntGeneratorProducer;

    private PrimitiveIterator.OfInt randomTemperatureIterator;
    private PrimitiveIterator.OfInt randomRPMIterator;

//    private int temperatureAvg;
//    private int temperatureDelta;
//    private int rpmAvg;
//    private int rpmDelta;

    /**
     * Initialize a new random number generator that generates random numbers in the
     * range [min, max]
     */
    @Override
    public void setProductLineParams(int temperatureAvg, int temperatureDelta, int rpmAvg, int rpmDelta) {
//	this.temperatureAvg = temperatureAvg;
//	this.temperatureDelta = temperatureDelta;
//	this.rpmAvg = rpmAvg;
//	this.rpmDelta = rpmDelta;

	randomTemperatureIterator = randomIntGeneratorProducer.intRandomNumberGenerator(temperatureAvg,
		temperatureDelta);
	randomRPMIterator = randomIntGeneratorProducer.intRandomNumberGenerator(rpmAvg, rpmDelta);
    }

    @Override
    public ProductionBean produce() {
	ProductionBean productionBean = new ProductionBean();
	productionBean.setTemperature(randomTemperatureIterator.nextInt());
	productionBean.setRpm(randomRPMIterator.nextInt());
	return productionBean;
    }

}
