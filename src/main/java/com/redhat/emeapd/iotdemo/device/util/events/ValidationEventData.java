/**
 * 
 */
package com.redhat.emeapd.iotdemo.device.util.events;

/**
 * @author abattagl
 *
 */
public class ValidationEventData {
    private String messageCorrelationId;
    private String messagePayload;
    /**
     * @return the messageCorrelationId
     */
    public String getMessageCorrelationId() {
        return messageCorrelationId;
    }
    /**
     * @param messageCorrelationId the messageCorrelationId to set
     */
    public void setMessageCorrelationId(String messageCorrelationId) {
        this.messageCorrelationId = messageCorrelationId;
    }
    /**
     * @return the messagePayload
     */
    public String getMessagePayload() {
        return messagePayload;
    }
    /**
     * @param messagePayload the messagePayload to set
     */
    public void setMessagePayload(String message) {
        this.messagePayload = message;
    }
    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((messageCorrelationId == null) ? 0 : messageCorrelationId.hashCode());
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
	ValidationEventData other = (ValidationEventData) obj;
	if (messageCorrelationId == null) {
	    if (other.messageCorrelationId != null)
		return false;
	} else if (!messageCorrelationId.equals(other.messageCorrelationId))
	    return false;
	return true;
    }
    @Override
    public String toString() {
	return "ValidationEventData [messageCorrelationId=" + messageCorrelationId + ", messagePayload=" + messagePayload + "]";
    }
    
    
}
