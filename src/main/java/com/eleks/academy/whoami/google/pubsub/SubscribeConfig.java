package com.eleks.academy.whoami.google.pubsub;

import com.eleks.academy.whoami.model.request.Message;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.gcp.pubsub.core.PubSubTemplate;
import org.springframework.cloud.gcp.pubsub.integration.AckMode;
import org.springframework.cloud.gcp.pubsub.integration.inbound.PubSubInboundChannelAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.messaging.MessageChannel;

@Configuration
public class SubscribeConfig {

    @Bean
    public PubSubInboundChannelAdapter messageChannelAdapter(
            @Qualifier("inputChannel") MessageChannel inputChannel,
            PubSubTemplate pubSubTemplate) {
        var adapter = new PubSubInboundChannelAdapter(pubSubTemplate, "notification-service");
        adapter.setOutputChannel(inputChannel);
        adapter.setAckMode(AckMode.MANUAL);
        adapter.setPayloadType(Message.class);
        return adapter;
    }
    @Bean
    public MessageChannel inputChannel() {
        return new DirectChannel();
    }
}
