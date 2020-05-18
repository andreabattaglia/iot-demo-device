package com.redhat.emeapd.iotdemo.device.service.messaging;

import org.slf4j.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import com.redhat.emeapd.iotdemo.device.util.events.CoolingValidationReceived;
import com.redhat.emeapd.iotdemo.device.util.events.ProductionValidationReceived;
import com.redhat.emeapd.iotdemo.device.util.events.ValidationEventData;

@ApplicationScoped
class ProductionMessageListener implements MessageListener {
    /**
     * Logger for this class
     */
    @Inject
    Logger LOGGER;

    @Inject
    @ProductionValidationReceived
    Event<ValidationEventData> productionValidationReceivedEvent;

    @Override
    public void onMessage(Message message) {
	if (LOGGER.isDebugEnabled()) {
	    LOGGER.debug("onMessage(Message message={}) - start", message);
	}

	ValidationEventData eventData = null;
	String messageCorrelationId = null;
	TextMessage replyMessage = null;
	String replyMessagePayload = null;

	try {
	    messageCorrelationId = message.getJMSCorrelationID();
	    replyMessage = (TextMessage) message;
	    replyMessagePayload = replyMessage.getText();
	    eventData = new ValidationEventData();
	    eventData.setMessageCorrelationId(messageCorrelationId);
	    eventData.setMessagePayload(replyMessagePayload);
	    LOGGER.info("Received the following message:\n"
	    	+ "\t type = PRODUCTION\n"
	    	+ "\tcorrelation ID = {}\n"
	    	+ "\tpayload = {}",messageCorrelationId, replyMessagePayload);
	    productionValidationReceivedEvent.fire(eventData);
	} catch (JMSException e) {
	    LOGGER.error("onMessage(Message)", e);

	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

	if (LOGGER.isDebugEnabled()) {
	    LOGGER.debug("onMessage(Message message={}) - end", message);
	}
    }

}