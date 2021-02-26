package io.github.fire.dragonking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.Consts;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;

public class DragonKingClient {


    private static volatile DragonKingClient client;
    private final AtomicBoolean enable = new AtomicBoolean(false);
    private String applicationName;
    private static String serverUrl;
    private static String kafkaServers;
    private static String defaultServerDomain = "localhost:8080";
    private static String defaultKafkaServers = "localhost:9092";

    private DragonKingClient() {

    }

    public static DragonKingClient getInstance() {

        return getInstance(defaultServerDomain, defaultKafkaServers);
    }

    public static DragonKingClient getInstance(String dragonKingServerDomain, String dragonKingKafkaServers) {

        if (client == null) {
            synchronized (DragonKingClient.class) {
                if (client == null) {

                    defaultServerDomain = dragonKingServerDomain;
                    defaultKafkaServers = dragonKingKafkaServers;

                    serverUrl = "http://" + dragonKingServerDomain;
                    kafkaServers = dragonKingKafkaServers;
                    client = new DragonKingClient();
                }
            }
        }
        return client;
    }


    public void reportAppInterfaceMateInfo(List<AppInterfaceInfo> appInterfaceInfos) {

        CloseableHttpResponse response = null;
        try {
            CloseableHttpClient httpclient = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost(serverUrl + "/report/" + applicationName + "/appInterfaceInfos/");
            ObjectMapper objectMapper = new ObjectMapper();
            httpPost.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
            httpPost.setHeader(HttpHeaders.CONTENT_ENCODING, Consts.UTF_8.toString());
            httpPost.setEntity(new StringEntity(objectMapper.writeValueAsString(appInterfaceInfos)));
            response = httpclient.execute(httpPost);
            System.out.println("response: " + response.getStatusLine());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                Objects.requireNonNull(response).close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void reportAppInterfaceTransferInfo(AppInterfaceInfo appInterfaceInfo) {
        if (enable.get()) {
            Kafka.PRODUCER.send(new ProducerRecord<String, String>("reportAppInterfaceTransferInfo", Integer.toString(1), Integer.toString(1)));
        }
    }

    public void notEnabled() {
        enable.set(false);
    }

    public void enabled(String applicationName) {
        this.enable.set(true);
        this.applicationName = applicationName;
    }

    private static class Kafka {


        private final static Producer<String, String> PRODUCER;

        static {
            Properties props = new Properties();
            props.put("bootstrap.servers", kafkaServers);
            props.put("acks", "all");
            props.put("retries", 0);
            props.put("linger.ms", 1);
            props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
            props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
            PRODUCER = new KafkaProducer<>(props);
        }
    }

}
