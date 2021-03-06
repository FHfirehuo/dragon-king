package io.github.fire.dragonking.job.runner;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.fire.dragonking.AppInterfaceTransferInfo;
import io.github.fire.dragonking.AppType;
import io.github.fire.dragonking.job.mapper.LogMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.stereotype.Component;

import java.util.Properties;
import java.util.concurrent.CountDownLatch;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafKaConsumeRunner {

    private final LogMapper logMapper;

    public void start() {

        final Properties props = getStreamsConfig();

        final StreamsBuilder builder = new StreamsBuilder();
        createRequestVolumeStatisticsStream(builder);
        final KafkaStreams streams = new KafkaStreams(builder.build(), props);
        final CountDownLatch latch = new CountDownLatch(1);

        // attach shutdown handler to catch control-c
        Runtime.getRuntime().addShutdownHook(new Thread("streams-requestVolumeStatistics-shutdown-hook") {
            @Override
            public void run() {
                streams.close();
                latch.countDown();
            }
        });

        try {
            streams.start();
            latch.await();
        } catch (final Throwable e) {
            System.exit(1);
        }
        System.exit(0);
    }

    public void createRequestVolumeStatisticsStream(final StreamsBuilder builder) {
        final KStream<String, String> source = builder.stream(INPUT_TOPIC);
        source.foreach((appName, value) -> {
            AppInterfaceTransferInfo appInterfaceInfo = null;
            try {
                appInterfaceInfo = new ObjectMapper().readValue(value, AppInterfaceTransferInfo.class);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                return;
            }
            String remoteAddr = appInterfaceInfo.getRemoteAddr();
            long entryMethodTime = appInterfaceInfo.getEntryMethodTime();
            long outMethodTime = appInterfaceInfo.getOutMethodTime();
            appInterfaceInfo.getRequestStatus();
            String method = appInterfaceInfo.getClassName() + "#" + appInterfaceInfo.getMethod();
            String uri = appInterfaceInfo.getUri();
            long consumingTime = outMethodTime - entryMethodTime; // 耗时
            appInterfaceInfo.setEntryMethodTime(entryMethodTime/1000);
            log.info("{} 在 {} 请求了 {} 的 {} , 耗时 {} ms, 状态为 {}", remoteAddr, consumingTime, appName, appInterfaceInfo.getAppType().equals(AppType.WEB) ? uri : method, consumingTime, appInterfaceInfo.getRequestStatus());
            logMapper.insert(appName, appInterfaceInfo, consumingTime);
        });

    }

    public static final String INPUT_TOPIC = "appInterfaceTransferInfo";
    public static final String GROUP_ID = "dragonKingJob";

    static Properties getStreamsConfig() {
        final Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, GROUP_ID);
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(StreamsConfig.CACHE_MAX_BYTES_BUFFERING_CONFIG, 0);
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());

//        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        return props;
    }
}
