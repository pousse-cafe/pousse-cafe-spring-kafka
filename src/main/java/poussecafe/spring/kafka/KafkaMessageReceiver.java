package poussecafe.spring.kafka;

import java.util.Objects;
import poussecafe.jackson.JacksonMessageAdapter;
import poussecafe.messaging.Message;
import poussecafe.messaging.MessageReceiver;
import poussecafe.processing.MessageBroker;

public class KafkaMessageReceiver extends MessageReceiver<SpringKafkaEnvelope> {

    public static class Builder {

        public Builder messageBroker(MessageBroker messageBroker) {
            this.messageBroker = messageBroker;
            return this;
        }

        private MessageBroker messageBroker;

        public Builder messageSenderAndReceiverFactory(MessageSenderAndReceiverFactory messageSenderAndReceiverFactory) {
            this.messageSenderAndReceiverFactory = messageSenderAndReceiverFactory;
            return this;
        }

        private MessageSenderAndReceiverFactory messageSenderAndReceiverFactory;

        public KafkaMessageReceiver build() {
            Objects.requireNonNull(messageBroker);
            Objects.requireNonNull(messageSenderAndReceiverFactory);

            KafkaMessageReceiver receiver = new KafkaMessageReceiver(messageBroker);
            receiver.messageSenderAndReceiverFactory = messageSenderAndReceiverFactory;
            return receiver;
        }
    }

    private KafkaMessageReceiver(MessageBroker messageBroker) {
        super(messageBroker);
    }

    private MessageSenderAndReceiverFactory messageSenderAndReceiverFactory;

    @Override
    protected void actuallyStartReceiving() {
        messageSenderAndReceiverFactory.registerReceiver(this);
        messageSenderAndReceiverFactory.startListenerContainer();
    }

    @Override
    protected void actuallyStopReceiving() {
        messageSenderAndReceiverFactory.deregisterReceiver(this);
    }

    void consume(SpringKafkaEnvelope envelope) {
        onMessage(envelope);
    }

    @Override
    protected Object extractPayload(SpringKafkaEnvelope envelope) {
        return envelope.consumerRecord().value();
    }

    @Override
    protected Message deserialize(Object payload) {
        return messageAdapter.adaptSerializedMessage(payload);
    }

    private JacksonMessageAdapter messageAdapter = new JacksonMessageAdapter();

    @Override
    protected Runnable buildAcker(SpringKafkaEnvelope envelope) {
        return () -> envelope.acknowledgment().acknowledge();
    }
}
