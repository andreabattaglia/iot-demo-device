/**
 * 
 */
package com.redhat.emeapd.iotdemo.device.service.messaging;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.jms.ConnectionFactory;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TemporaryQueue;
import javax.jms.TextMessage;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.redhat.emeapd.iotdemo.device.domain.CoolingBean;
import com.redhat.emeapd.iotdemo.device.domain.ProductionBean;
import com.redhat.emeapd.iotdemo.device.util.events.CoolingValidationReceived;
import com.redhat.emeapd.iotdemo.device.util.events.CoolingValidationResponseReady;
import com.redhat.emeapd.iotdemo.device.util.events.ProductionValidationReceived;
import com.redhat.emeapd.iotdemo.device.util.events.ProductionValidationResponseReady;
import com.redhat.emeapd.iotdemo.device.util.events.ValidationEventData;
import com.redhat.emeapd.iotdemo.device.util.events.ValidationReplyReadyData;

/**
 * @author abattagl
 * 
 * @see https://stackoverflow.com/questions/47617617/camel-in-out-messages-and-correlation-id-confusion
 *
 */
@ApplicationScoped
public class MessagingServiceImpl implements MessagingService {

    private final ObjectMapper MAPPER = new ObjectMapper();

//    @ConfigProperty(name = "quarkus.artemis.url")
//    @ConfigProperty(name = "quarkus.qpid-jms.url")
//    String amqURL;

    @ConfigProperty(name = "production.queue.name", defaultValue = "production")
    String productionQueueName;

    @ConfigProperty(name = "cooling.queue.name", defaultValue = "cooling")
    String coolingQueueName;

    @Inject
    ConnectionFactory connectionFactory;

    @Inject
    ProductionMessageListener productionMessageListener;
    @Inject
    CoolingMessageListener coolingMessageListener;

    @Inject
    @ProductionValidationResponseReady
    Event<ValidationReplyReadyData> productionValidationResponseReadyEvent;

    @Inject
    @CoolingValidationResponseReady
    Event<ValidationReplyReadyData> coolingValidationResponseReadyEvent;

    private JMSContext context = null;
    private JMSProducer producer = null;
    private Queue productionQueue = null;
    private Queue coolingQueue = null;
    private TemporaryQueue productionReplyQueue;
    private TemporaryQueue coolingReplyQueue;
    private JMSConsumer productionConsumer;
    private JMSConsumer coolingConsumer;
    final Map<String, Integer> productionRequestMap = new HashMap<>();
    final Map<String, Integer> coolingRequestMap = new HashMap<>();

    @Inject
    Logger LOGGER;

    @PostConstruct
    void postConstruct() {
	context = connectionFactory.createContext(Session.AUTO_ACKNOWLEDGE);
	producer = context.createProducer();
	productionQueue = context.createQueue(productionQueueName);
	coolingQueue = context.createQueue(coolingQueueName);

	productionReplyQueue = context.createTemporaryQueue();
	coolingReplyQueue = context.createTemporaryQueue();

	productionConsumer = context.createConsumer(productionReplyQueue);
	productionConsumer.setMessageListener(productionMessageListener);
	coolingConsumer = context.createConsumer(coolingReplyQueue);
	coolingConsumer.setMessageListener(coolingMessageListener);
    }

    @PreDestroy
    void preDestroy() {
	context.close();
    }

    @Override
    public void sendProductionData(int iteration, ProductionBean productionBean) throws Exception {
	sendData(productionQueue, productionReplyQueue, productionBean, productionConsumer, productionRequestMap,
		iteration);
    }

    @Override
    public void sendCoolingData(int iteration, CoolingBean coolingBean) throws Exception {
	sendData(coolingQueue, coolingReplyQueue, coolingBean, coolingConsumer, coolingRequestMap, iteration);

    }

    private void sendData(Queue queue, TemporaryQueue replyQueue, Object payload, JMSConsumer replyConsumer,
	    Map<String, Integer> correlationMap, int iterationId) throws Exception {
	String messagePayload = null;

	messagePayload = MAPPER.writeValueAsString(payload);

	TextMessage message = context.createTextMessage(messagePayload);
	message.setJMSReplyTo(replyQueue);
	producer.send(queue, message);
//	TextMessage replyMessage = (TextMessage) replyConsumer.receive();
//	String replyMessagePayload = replyMessage.getText();
//	boolean isValidated = Boolean.parseBoolean(replyMessagePayload);
//	if (LOGGER.isInfoEnabled()) {
//	    LOGGER.info("Repy for message {} = {} (boolean value = {})", messagePayload, replyMessagePayload, isValidated);
//	}

	correlationMap.put(message.getJMSMessageID(), iterationId);

    }

    void evaluateProductionValidationReply(
	    @Observes @ProductionValidationReceived ValidationEventData validationEventData) {
	if (LOGGER.isInfoEnabled()) {
	    LOGGER.info("evaluateProductionValidationReply(ValidationEventData validationEventData={}) - start",
		    validationEventData);
	}

	ValidationReplyReadyData reply = null;
	int iterationId = productionRequestMap.remove(validationEventData.getMessageCorrelationId());
	reply = new ValidationReplyReadyData();
	reply.setIterationId(iterationId);
	reply.setValid(Boolean.parseBoolean(validationEventData.getMessagePayload()));
	productionValidationResponseReadyEvent.fire(reply);

	if (LOGGER.isInfoEnabled()) {
	    LOGGER.info("evaluateProductionValidationReply(ValidationEventData validationEventData={}) - end",
		    validationEventData);
	}
    }

    void evaluateCoolingValidationReply(@Observes @CoolingValidationReceived ValidationEventData validationEventData) {
	if (LOGGER.isInfoEnabled()) {
	    LOGGER.info("evaluateCoolingValidationReply(ValidationEventData validationEventData={}) - start",
		    validationEventData);
	}

	ValidationReplyReadyData reply = null;
	int iterationId = 0;

	if (coolingRequestMap.containsKey(validationEventData.getMessageCorrelationId()))
	    iterationId = coolingRequestMap.remove(validationEventData.getMessageCorrelationId());

	reply = new ValidationReplyReadyData();
	reply.setIterationId(iterationId);
	reply.setValid(Boolean.parseBoolean(validationEventData.getMessagePayload()));
	coolingValidationResponseReadyEvent.fire(reply);

	if (LOGGER.isInfoEnabled()) {
	    LOGGER.info("evaluateCoolingValidationReply(ValidationEventData validationEventData={}) - end",
		    validationEventData);
	}
    }

}