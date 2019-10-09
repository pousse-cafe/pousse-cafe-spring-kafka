package poussecafe.spring.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;
import poussecafe.messaging.MessageSender;
import poussecafe.processing.MessageBroker;

@Component
public class MessageSenderAndReceiverFactory implements InitializingBean, MessageListener<String, String> {

    @Override
    public void afterPropertiesSet()
            throws Exception {
        logger.info("Configuring Spring Kafka messaging");
        SpringKafkaMessaging.setFactory(this);

        listenerContainer.setupMessageListener(this);
    }

    private Logger logger = LoggerFactory.getLogger(getClass());

    public MessageSender buildMessageSender() {
        return new KafkaMessageSender.Builder()
                .kafkaTemplate(template)
                .topic(listenerContainer.getContainerProperties().getTopics()[0])
                .build();
    }

    @Autowired
    private KafkaMessageListenerContainer<String, String> listenerContainer;

    @Autowired
    private KafkaTemplate<String, String> template;

    void startListenerContainer() {
        listenerContainer.start();
    }

    public KafkaMessageReceiver buildMessageReceiver(MessageBroker messageBroker) {
        return new KafkaMessageReceiver.Builder()
                .messageBroker(messageBroker)
                .messageSenderAndReceiverFactory(this)
                .build();
    }

    @Override
    public void onMessage(ConsumerRecord<String, String> consumerRecord, Acknowledgment acknowledgment) {
        kafkaReceiver.consume(new SpringKafkaEnvelope.Builder()
                .consumerRecord(consumerRecord)
                .acknowledgment(acknowledgment)
                .build());
    }

    @Override
    public void onMessage(ConsumerRecord<String, String> data) {
        throw new UnsupportedOperationException("Acknowledgment is required");
    }

    synchronized void registerReceiver(KafkaMessageReceiver kafkaMessageReceiver) {
        kafkaReceiver = kafkaMessageReceiver;
    }

    private KafkaMessageReceiver kafkaReceiver;

    synchronized void deregisterReceiver(KafkaMessageReceiver kafkaMessageReceiver) {
        if(kafkaReceiver == kafkaMessageReceiver) {
            kafkaReceiver = null;
            listenerContainer.stop();
        }
    }
}
