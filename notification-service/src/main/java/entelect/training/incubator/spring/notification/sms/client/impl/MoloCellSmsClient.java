package entelect.training.incubator.spring.notification.sms.client.impl;

import entelect.training.incubator.spring.notification.sms.client.SmsClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Service;

import javax.jms.*;

/**
 * A custom implementation of a fictional SMS service.
 */
@Service
public class MoloCellSmsClient implements SmsClient {

    @Autowired
    private JmsTemplate jmsTemplate;

    @Override
    public void sendSms(String phoneNumber, String message) {
        System.out.println(String.format("Sending SMS, destination='{}', '{}'", phoneNumber, message));
    }

    public void enqueueMessage(String message) {

        jmsTemplate.send("sms_queue", session -> session.createObjectMessage(message));
    }
}
