/**
 * Created by Raghavendra on 5/13/2015.
 */
import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

import java.util.Properties;

public class SimpleProducer {
    private static Producer<String, String> producer;
    private final Properties properties = new Properties();

    public SimpleProducer() {
        properties.put("metadata.broker.list", "localhost:9092");
        properties.put("serializer.class", "kafka.serializer.StringEncoder");
        properties.put("request.required.acks", "1");
        producer = new Producer<String, String>(new ProducerConfig(properties));
    }

    public static void main(String[] args) {
        new SimpleProducer();
        String topic = "TrendingTweets";
        String mod_email = "raghavendra.reddy@sjsu.edu";
        String msg = mod_email + ":009987796:";
        KeyedMessage<String, String> data = new KeyedMessage<String, String>(topic, msg);
        producer.send(data);
        producer.close();
    }
}
