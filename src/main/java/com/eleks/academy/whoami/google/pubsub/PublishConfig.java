package com.eleks.academy.whoami.google.pubsub;

import com.eleks.academy.whoami.model.request.Message;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.cloud.gcp.pubsub.core.PubSubTemplate;
import org.springframework.cloud.gcp.pubsub.integration.outbound.PubSubMessageHandler;
import org.springframework.cloud.gcp.pubsub.support.converter.JacksonPubSubMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Configuration
public class PublishConfig {
    private static final Log LOGGER = LogFactory.getLog(PublishConfig.class);
    private static final String TOPIC_NAME = "projects/asset-management-system-367118/topics/upload-photo";

    @Bean
    public DirectChannel pubSubOutputChannel() {
        return new DirectChannel();
    }

    @Bean
    @ServiceActivator(inputChannel = "pubSubOutputChannel")
    public MessageHandler messageSender(PubSubTemplate pubSubTemplate) {
        PubSubMessageHandler adapter = new PubSubMessageHandler(pubSubTemplate, TOPIC_NAME);
        adapter.setPublishCallback(new ListenableFutureCallback<>() {
            @Override
            public void onFailure(Throwable ex) {
                LOGGER.info("There was an error sending the message.");
            }

            @Override
            public void onSuccess(String result) {
                LOGGER.info("Message was sent successfully.");
            }
        });

        return adapter;
    }
    @Bean
    public JacksonPubSubMessageConverter jacksonPubSubMessageConverter(ObjectMapper objectMapper) {
        return new JacksonPubSubMessageConverter(objectMapper);
    }
    /**
     * an interface that allows sending a person to Pub/Sub.
     */
    @MessagingGateway(defaultRequestChannel = "pubSubOutputChannel")
    public interface PubSubPersonGateway {
        void sendMessageToPubSub(Message person);
    }
}
