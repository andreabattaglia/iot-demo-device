/**
 * 
 */
package com.redhat.emeapd.iotdemo.device.domain;

import io.quarkus.runtime.annotations.RegisterForReflection;

/**
 * @author abattagl
 *
 */
@RegisterForReflection
public class CoolingBean {
    private int cooling;

    /**
     * @return the cooling
     */
    public int getCooling() {
	return cooling;
    }

    /**
     * @param cooling the cooling to set
     */
    public void setCooling(int cooling) {
	this.cooling = cooling;
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + cooling;
	return result;
    }

    @Override
    public boolean equals(Object obj) {
	if (this == obj)
	    return true;
	if (obj == null)
	    return false;
	if (getClass() != obj.getClass())
	    return false;
	CoolingBean other = (CoolingBean) obj;
	if (cooling != other.cooling)
	    return false;
	return true;
    }

    @Override
    public String toString() {
	return "CoolingBean [cooling=" + cooling + "]";
    }

}
