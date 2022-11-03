package com.eleks.academy.whoami.google.pubsub;

import com.eleks.academy.whoami.model.request.Message;
import org.springframework.cloud.gcp.pubsub.support.BasicAcknowledgeablePubsubMessage;
import org.springframework.cloud.gcp.pubsub.support.GcpPubSubHeaders;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

@Service
public class Receiver {

    @ServiceActivator(inputChannel = "inputChannel")
    public void messageReceiver(Message payload,
                                @Header (GcpPubSubHeaders.ORIGINAL_MESSAGE) BasicAcknowledgeablePubsubMessage message) {
        System.out.println(payload);
        message.ack();
    }
}
