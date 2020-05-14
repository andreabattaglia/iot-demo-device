package com.redhat.emeapd.iotdemo.device.domain;

public class ProductLineBean {
    private int temperatureAvg;
    private int temperatureDelta;
    private int rpmAvg;
    private int rpmDelta;
    private int coolingAvg;
    private int coolingDelta;

    /**
     * @return the temperatureAvg
     */
    public int getTemperatureAvg() {
	return temperatureAvg;
    }

    /**
     * @param temperatureAvg the temperatureAvg to set
     */
    public void setTemperatureAvg(int temperatureAvg) {
	this.temperatureAvg = temperatureAvg;
    }

    /**
     * @return the temperatureDelta
     */
    public int getTemperatureDelta() {
	return temperatureDelta;
    }

    /**
     * @param temperatureDelta the temperatureDelta to set
     */
    public void setTemperatureDelta(int temperatureDelta) {
	this.temperatureDelta = temperatureDelta;
    }

    /**
     * @return the rpmAvg
     */
    public int getRpmAvg() {
	return rpmAvg;
    }

    /**
     * @param rpmAvg the rpmAvg to set
     */
    public void setRpmAvg(int rpmAvg) {
	this.rpmAvg = rpmAvg;
    }

    /**
     * @return the rpmDelta
     */
    public int getRpmDelta() {
	return rpmDelta;
    }

    /**
     * @param rpmDelta the rpmDelta to set
     */
    public void setRpmDelta(int rpmDelta) {
	this.rpmDelta = rpmDelta;
    }

    /**
     * @return the coolingAvg
     */
    public int getCoolingAvg() {
	return coolingAvg;
    }

    /**
     * @param coolingAvg the coolingAvg to set
     */
    public void setCoolingAvg(int coolingAvg) {
	this.coolingAvg = coolingAvg;
    }

    /**
     * @return the coolingDelta
     */
    public int getCoolingDelta() {
	return coolingDelta;
    }

    /**
     * @param coolingDelta the coolingDelta to set
     */
    public void setCoolingDelta(int coolingDelta) {
	this.coolingDelta = coolingDelta;
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + coolingAvg;
	result = prime * result + coolingDelta;
	result = prime * result + rpmAvg;
	result = prime * result + rpmDelta;
	result = prime * result + temperatureAvg;
	result = prime * result + temperatureDelta;
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
	ProductLineBean other = (ProductLineBean) obj;
	if (coolingAvg != other.coolingAvg)
	    return false;
	if (coolingDelta != other.coolingDelta)
	    return false;
	if (rpmAvg != other.rpmAvg)
	    return false;
	if (rpmDelta != other.rpmDelta)
	    return false;
	if (temperatureAvg != other.temperatureAvg)
	    return false;
	if (temperatureDelta != other.temperatureDelta)
	    return false;
	return true;
    }

    @Override
    public String toString() {
	return "ProductLineBean [temperatureAvg=" + temperatureAvg + ", temperatureDelta=" + temperatureDelta
		+ ", rpmAvg=" + rpmAvg + ", rpmDelta=" + rpmDelta + ", coolingAvg=" + coolingAvg + ", coolingDelta="
		+ coolingDelta + "]";
    }

}
