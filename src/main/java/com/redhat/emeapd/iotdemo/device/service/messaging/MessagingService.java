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
    boolean sendProductionData(final ProductionBean productionBean) throws Exception;

    boolean sendCoolingData(final CoolingBean coolingBean) throws Exception;
}
