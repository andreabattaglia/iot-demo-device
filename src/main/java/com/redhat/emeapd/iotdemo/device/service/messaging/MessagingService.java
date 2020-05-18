/**
 * 
 */
package com.redhat.emeapd.iotdemo.device.service.messaging;

import com.redhat.emeapd.iotdemo.device.domain.CoolingBean;
import com.redhat.emeapd.iotdemo.device.domain.ProductionBean;

/**
 * @author abattagl
 *
 */
public interface MessagingService {
    void sendProductionData(final int iteration, final ProductionBean productionBean) throws Exception;

    void sendCoolingData(final int iteration, final CoolingBean coolingBean) throws Exception;
}
