package io.github.fire.dragonking;

import io.github.fire.dragonking.liserner.StartedListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@Slf4j
@SpringBootApplication
public class DragonKingApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder()
                .sources(DragonKingApplication.class)
                .listeners(new StartedListener())
                .run(args);

    }

}
