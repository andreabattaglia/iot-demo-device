/**
 * 
 */
package com.redhat.emeapd.iotdemo.device.util.events;

/**
 * @author abattagl
 *
 */
public class ValidationReplyReadyData {
    private int iterationId;
    private boolean valid;
    /**
     * @return the iterationId
     */
    public int getIterationId() {
        return iterationId;
    }
    /**
     * @param iterationId the iterationId to set
     */
    public void setIterationId(int iterationId) {
        this.iterationId = iterationId;
    }
    /**
     * @return the valid
     */
    public boolean isValid() {
        return valid;
    }
    /**
     * @param valid the valid to set
     */
    public void setValid(boolean valid) {
        this.valid = valid;
    }
    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + iterationId;
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
	ValidationReplyReadyData other = (ValidationReplyReadyData) obj;
	if (iterationId != other.iterationId)
	    return false;
	return true;
    }
    @Override
    public String toString() {
	return "ValidationReplyReadyData [iterationId=" + iterationId + ", valid=" + valid + "]";
    }
    
    
    
}
