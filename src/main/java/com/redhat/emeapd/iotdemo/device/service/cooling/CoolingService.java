/**
 * 
 */
package com.redhat.emeapd.iotdemo.device.service.cooling;

import com.redhat.emeapd.iotdemo.device.domain.CoolingBean;

/**
 * @author abattagl
 *
 */
public interface CoolingService {
    void setProductLineParams(int coolingAvg, int coolingDelta);
    CoolingBean cool();
}
