/**
 * 
 */
package com.redhat.emeapd.iotdemo.device.service.cooling;

import java.util.PrimitiveIterator;
import java.util.Random;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

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

    private int coolingAvg;
    private int coolingDelta;


    @Override
    public void setProductLineParams(int coolingAvg, int coolingDelta) {
	this.coolingAvg = coolingAvg;
	this.coolingDelta = coolingDelta;
	randomCoolingIterator = randomIntGeneratorProducer.intRandomNumberGenerator(coolingAvg,coolingDelta);
    }

    @Override
    public CoolingBean cool() {
	CoolingBean coolingBean = new CoolingBean();
	coolingBean.setTemperature(randomCoolingIterator.nextInt());
	return coolingBean;
    }

}
