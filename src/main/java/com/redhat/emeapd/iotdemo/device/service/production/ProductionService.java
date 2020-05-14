/**
 * 
 */
package com.redhat.emeapd.iotdemo.device.service.production;

import com.redhat.emeapd.iotdemo.device.domain.ProductionBean;

/**
 * @author abattagl
 *
 */
public interface ProductionService {
    void setProductLineParams(int temperatureAvg, int temperatureDelta, int rpmAvg, int rpmDelta);
    
    ProductionBean produce();
}
