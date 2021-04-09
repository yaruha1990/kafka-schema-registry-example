package com.example;

import io.confluent.kafka.serializers.KafkaAvroSerializer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

public class Producer {

    private static final String TOPIC = "test";

    public static void main(String[] args) {
        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", "localhost:9092");
        properties.setProperty("acks", "1");
        properties.setProperty("retries", "10");
        properties.setProperty("key.serializer", StringSerializer.class.getName());
        properties.setProperty("value.serializer", KafkaAvroSerializer.class.getName());
        properties.setProperty("schema.registry.url", "http://localhost:8081");

        KafkaProducer<String, User> producer = new KafkaProducer<>(properties);

        User user = User.newBuilder().setName("Yarik").setEmail("yaruha@ukr.net").setAge(30).build();

        ProducerRecord<String, User> producerRecord = new ProducerRecord<>(TOPIC, user);

        producer.send(producerRecord, (recordMetadata, exception) -> {
            if (exception == null) {
                System.out.println(recordMetadata.toString());
            } else {
                exception.printStackTrace();
            }
        });

        producer.flush();
        producer.close();
    }
}
