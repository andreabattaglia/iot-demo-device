/**
 * 
 */
package com.redhat.emeapd.iotdemo.device.domain;

/**
 * @author abattagl
 *
 */
public class CoolingBean {
    private int temperature;

    /**
     * @return the temperature
     */
    public int getTemperature() {
	return temperature;
    }

    /**
     * @param temperature the temperature to set
     */
    public void setTemperature(int temperature) {
	this.temperature = temperature;
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + temperature;
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
	if (temperature != other.temperature)
	    return false;
	return true;
    }

    @Override
    public String toString() {
	return "CoolingBean [temperature=" + temperature + "]";
    }

}
