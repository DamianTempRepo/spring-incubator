package entelect.training.incubator.spring.notification.sms.client.jms;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import javax.jms.JMSException;


@Component
public class Listener {

    @JmsListener(destination = "sms_queue")
    public String receiveMessage(String message) throws JMSException {

        System.out.println("Received message " + message);
        return message;
    }

}