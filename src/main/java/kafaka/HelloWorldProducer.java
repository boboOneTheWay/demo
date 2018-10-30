package kafaka;

import java.util.Date;
import java.util.Properties;
import java.util.Random;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

public class HelloWorldProducer {
    public static void main(String[] args) {
        Random rnd = new Random();

        Properties props = new Properties();
        props.put("bootstrap.servers", "");
        props.put("acks", "all");
        props.put("retries", 0);
        props.put("batch.size", 16384);
        props.put("linger.ms", 1);
        props.put("buffer.memory", 33554432);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        //配置partitionner选择策略，可选配置
//        props.put("partitioner.class", "kafaka.SimplePartitioner");

        Producer<String, String> producer = new KafkaProducer<>(props);

        String msg = "{\"sendId\":\"20188003\",\"senderName\":\"科技有限公司\",\"userName\":\"gao\",\"token\":\"E10ADC3949BA59ABBE56E057F20F8839\",\"targetName\":\"CFAE\",\"msgType\":102,\"msgState\":0,\"transUuid\":\"E10ADC3949BA59ABBE56E057F20F883EE10ADC3949BA59ABBE56E057F20F8839\",\"msgUuid\":\"1000114300000009\"}";
        ProducerRecord<String, String> data = new ProducerRecord<>("HARVESTSCF_TO_CFAE", msg);
            producer.send(data,
                    new Callback() {
                        public void onCompletion(RecordMetadata metadata, Exception e) {
                            if(e != null) {
                                System.out.println("kafaka 异常");
                                e.printStackTrace();
                            } else {
                                System.out.println("The offset of the record we just sent is: " + metadata.offset());
                            }
                        }
                    });
//        }
        producer.close();
    }
}
