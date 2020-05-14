/**
 * 
 */
package com.redhat.emeapd.iotdemo.device.service.productline;

import com.redhat.emeapd.iotdemo.device.domain.ProductLineBean;

/**
 * @author abattagl
 *
 */
public interface ProductLineService {

    ProductLineBean getProductLine();

    void newProductLine(int temperatureAvg, int temperatureDelta, int rpmAvg, int rpmDelta, int coolingAvg,
	    int coolingDelta);

    void newProductLine(ProductLineBean productLine);

}
