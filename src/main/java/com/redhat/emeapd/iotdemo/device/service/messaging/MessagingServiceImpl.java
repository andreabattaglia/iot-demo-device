/**
 * 
 */
package com.redhat.emeapd.iotdemo.device.service.messaging;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
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

/**
 * @author abattagl
 * 
 * @see https://stackoverflow.com/questions/47617617/camel-in-out-messages-and-correlation-id-confusion
 *
 */
@ApplicationScoped
public class MessagingServiceImpl implements MessagingService {

    private final ObjectMapper MAPPER = new ObjectMapper();

    @ConfigProperty(name = "production.queue.name", defaultValue = "production")
    String productionQueueName;

    @ConfigProperty(name = "cooling.queue.name", defaultValue = "cooling")
    String coolingQueueName;

    @Inject
    ConnectionFactory connectionFactory;

    private JMSContext context = null;
    private JMSProducer producer = null;
    private Queue productionQueue = null;
    private Queue coolingQueue = null;
    private TemporaryQueue productionReplyQueue;
    private TemporaryQueue coolingReplyQueue;
    private JMSConsumer productionConsumer;
    private JMSConsumer coolingConsumer;
    final Map<String, TextMessage> requestMap = new HashMap<>();

    @Inject
    Logger LOGGER;

    @PostConstruct
    private void postConstruct() {
	context = connectionFactory.createContext(Session.AUTO_ACKNOWLEDGE);
	producer = context.createProducer();
	productionQueue = context.createQueue(productionQueueName);
	coolingQueue = context.createQueue(coolingQueueName);
	productionReplyQueue = context.createTemporaryQueue();
	coolingReplyQueue = context.createTemporaryQueue();
	productionConsumer = context.createConsumer(productionReplyQueue);
	coolingConsumer = context.createConsumer(coolingReplyQueue);
    }

    @PreDestroy
    private void preDestroy() {
	context.close();
    }

    @Override
    public boolean sendProductionData(ProductionBean productionBean) throws Exception {
	return sendData(productionQueue, productionReplyQueue, productionBean, productionConsumer);
    }

    @Override
    public boolean sendCoolingData(CoolingBean coolingBean) throws Exception {
	return sendData(coolingQueue, coolingReplyQueue, coolingBean, productionConsumer);

    }

    public boolean sendData(Queue queue, TemporaryQueue replyQueue, Object payload, JMSConsumer replyConsumer)
	    throws Exception {
	String messagePayload = null;

	messagePayload = MAPPER.writeValueAsString(payload);

	TextMessage message = context.createTextMessage(messagePayload);
	message.setJMSReplyTo(replyQueue);
	producer.send(queue, message);
	requestMap.put(message.getJMSMessageID(), message);
	TextMessage replyMessage = (TextMessage) replyConsumer.receive();
	return Boolean.getBoolean(replyMessage.getText());

    }

}
