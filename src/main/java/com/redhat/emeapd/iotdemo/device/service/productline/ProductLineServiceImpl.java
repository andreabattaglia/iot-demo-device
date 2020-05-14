/**
 * 
 */
package com.redhat.emeapd.iotdemo.device.service.productline;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;

import com.redhat.emeapd.iotdemo.device.domain.ProductLineBean;
import com.redhat.emeapd.iotdemo.device.util.events.ProductLineChanged;

import io.quarkus.runtime.StartupEvent;

/**
 * @author abattagl
 *
 */
@ApplicationScoped
class ProductLineServiceImpl implements ProductLineService {

    @ConfigProperty(name = "production.temperature.avg.default")
    private int temperatureAvg;
    @ConfigProperty(name = "production.temperature.delta.default")
    private int temperatureDelta;
    @ConfigProperty(name = "production.rpm.avg.default")
    private int rpmAvg;
    @ConfigProperty(name = "production.rpm.delta.default")
    private int rpmDelta;
    @ConfigProperty(name = "cooling.decrement.avg.default")
    private int coolingAvg;
    @ConfigProperty(name = "cooling.decrement.delta.default")
    private int coolingDelta;

    private ProductLineBean productLine;

    @Inject
    @ProductLineChanged
    Event<Void> productLineEvent;

    @Inject
    Logger LOGGER;

    void onStart(@Observes StartupEvent ev) {
	LOGGER.info("The application is starting...{}");
	productLine = buildProductLineBean(temperatureAvg, temperatureDelta, rpmAvg, rpmDelta, coolingAvg,
		coolingDelta);
    }

    private ProductLineBean buildProductLineBean(int temperatureAvg, int temperatureDelta, int rpmAvg, int rpmDelta,
	    int coolingAvg, int coolingDelta) {
	ProductLineBean productLine = new ProductLineBean();
	productLine.setTemperatureAvg(temperatureAvg);
	productLine.setTemperatureDelta(temperatureDelta);
	productLine.setRpmAvg(rpmAvg);
	productLine.setRpmDelta(rpmDelta);
	productLine.setCoolingAvg(coolingAvg);
	productLine.setCoolingDelta(coolingDelta);
	return productLine;
    }

    @Override
    public void newProductLine(int temperatureAvg, int temperatureDelta, int rpmAvg, int rpmDelta, int coolingAvg,
	    int coolingDelta) {
	productLine = buildProductLineBean(temperatureAvg, temperatureDelta, rpmAvg, rpmDelta, coolingAvg,
		coolingDelta);
	productLineEvent.fire(null);
    }

    @Override
    public void newProductLine(ProductLineBean productLine) {
	this.productLine = productLine;
	productLineEvent.fire(null);
    }

    @Override
    public ProductLineBean getProductLine() {
	return productLine;
    }

}
