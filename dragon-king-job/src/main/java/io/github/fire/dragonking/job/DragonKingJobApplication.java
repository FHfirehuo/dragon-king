package io.github.fire.dragonking.job;

import io.github.fire.dragonking.job.runner.KafKaConsumeRunner;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

@Slf4j
@SpringBootApplication
public class DragonKingJobApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = new SpringApplicationBuilder()
                .sources(DragonKingJobApplication.class)
                .run(args);

        KafKaConsumeRunner runner = applicationContext.getBean(KafKaConsumeRunner.class);
        runner.start();
    }

}
