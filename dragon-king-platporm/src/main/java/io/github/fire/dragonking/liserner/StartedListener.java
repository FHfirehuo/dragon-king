package io.github.fire.dragonking.liserner;

import io.github.fire.dragonking.service.ReportService;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;

public class StartedListener implements ApplicationListener<ApplicationStartedEvent> {
    @Override
    public void onApplicationEvent(ApplicationStartedEvent applicationStartedEvent) {

        ApplicationContext applicationContext = applicationStartedEvent.getApplicationContext();
        ReportService reportService = applicationContext.getBean(ReportService.class);

    }
}
