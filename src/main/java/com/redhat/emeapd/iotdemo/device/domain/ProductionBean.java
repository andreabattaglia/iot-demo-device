/**
 * 
 */
package com.redhat.emeapd.iotdemo.device.domain;

/**
 * @author abattagl
 *
 */
public class ProductionBean {
    private int temperature;
    private int rpm;

    /**
     * @return the temperature
     */
    public int getTemperature() {
	return temperature;
    }

    /**
     * @param temperature the temperature to set
     */
    public void setTemperature(int temp) {
	this.temperature = temp;
    }

    /**
     * @return the rpm
     */
    public int getRpm() {
	return rpm;
    }

    /**
     * @param rpm the rpm to set
     */
    public void setRpm(int rpm) {
	this.rpm = rpm;
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + rpm;
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
	ProductionBean other = (ProductionBean) obj;
	if (rpm != other.rpm)
	    return false;
	if (temperature != other.temperature)
	    return false;
	return true;
    }

    @Override
    public String toString() {
	return "ProductionBean [temperature=" + temperature + ", rpm=" + rpm + "]";
    }

}
