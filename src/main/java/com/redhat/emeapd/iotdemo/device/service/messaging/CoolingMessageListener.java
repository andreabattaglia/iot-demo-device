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
import com.redhat.emeapd.iotdemo.device.util.events.ValidationEventData;

@ApplicationScoped
class CoolingMessageListener implements MessageListener {
    /**
     * Logger for this class
     */
    @Inject
    Logger LOGGER;;

    @Inject
    @CoolingValidationReceived
    Event<ValidationEventData> coolingValidationReceivedEvent;

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
	    LOGGER.info("Received the following message:\n" + "\t type = COOLING\n" + "\tcorrelation ID = {}\n"
		    + "\tpayload = {}", messageCorrelationId, replyMessagePayload);

	    coolingValidationReceivedEvent.fire(eventData);
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
