package poussecafe.spring.kafka;

import java.util.Objects;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.support.Acknowledgment;

public class SpringKafkaEnvelope {

    public static class Builder {

        private SpringKafkaEnvelope envelope = new SpringKafkaEnvelope();

        public Builder consumerRecord(ConsumerRecord<String, String> consumerRecord) {
            envelope.consumerRecord = consumerRecord;
            return this;
        }

        public Builder acknowledgment(Acknowledgment acknowledgment) {
            envelope.acknowledgment = acknowledgment;
            return this;
        }

        public SpringKafkaEnvelope build() {
            Objects.requireNonNull(envelope.consumerRecord);
            Objects.requireNonNull(envelope.acknowledgment);
            return envelope;
        }
    }

    private SpringKafkaEnvelope() {

    }

    private ConsumerRecord<String, String> consumerRecord;

    public ConsumerRecord<String, String> consumerRecord() {
        return consumerRecord;
    }

    private Acknowledgment acknowledgment;

    public Acknowledgment acknowledgment() {
        return acknowledgment;
    }
}
