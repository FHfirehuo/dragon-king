package io.github.fire.dragonking;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

@Configuration
@ConditionalOnWebApplication
@AutoConfigureAfter(value = WebMvcAutoConfiguration.class)
@EnableConfigurationProperties(DragonKingProperties.class)
public class DragonKingAutoConfiguration implements EnvironmentAware {

    private final static Logger LOGGER = LoggerFactory.getLogger(DragonKingAutoConfiguration.class);

    private Environment environment;

    @Autowired
    private DragonKingProperties dragonKingProperties;


    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Configuration
    public class DragonKingInterceptorConfig implements WebMvcConfigurer {
        @Override
        public void addInterceptors(InterceptorRegistry registry) {
            LOGGER.debug("添加拦截器 UrlInterceptor");
            registry.addInterceptor(new UrlInterceptor()).addPathPatterns("/**");
        }
    }

    @Component
    public class StartedListener implements ApplicationListener<ApplicationStartedEvent> {

        @Override
        public void onApplicationEvent(ApplicationStartedEvent applicationStartedEvent) {
            LOGGER.debug("spring项目启动,上报所有接口");
            ApplicationContext applicationContext = applicationStartedEvent.getApplicationContext();
            RequestMappingInfo requestMappingInfo = applicationContext.getBean(RequestMappingInfo.class);
            requestMappingInfo.report(dragonKingProperties.getServerDomain(), dragonKingProperties.getKafkaServers(), environment.getProperty("spring.application.name"), applicationContext.getBean(RequestMappingHandlerMapping.class));
        }
    }

    @Bean
    public RequestMappingInfo requestMappingInfo() {
        return new RequestMappingInfo();
    }
}
