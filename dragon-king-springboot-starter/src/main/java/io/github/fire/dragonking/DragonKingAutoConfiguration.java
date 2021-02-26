package io.github.fire.dragonking;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@ConditionalOnWebApplication
@AutoConfigureAfter(value = WebMvcAutoConfiguration.class)
public class DragonKingAutoConfiguration implements EnvironmentAware {

    private Environment environment;

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Configuration
    public class DragonKingInterceptorConfig implements WebMvcConfigurer {
        @Override
        public void addInterceptors(InterceptorRegistry registry) {
            registry.addInterceptor(new UrlInterceptor()).addPathPatterns("/**");
        }
    }

    public class StartedListener implements ApplicationListener<ApplicationStartedEvent> {

        @Override
        public void onApplicationEvent(ApplicationStartedEvent applicationStartedEvent) {
            ApplicationContext applicationContext = applicationStartedEvent.getApplicationContext();
            RequestMappingInfo requestMappingInfo = applicationContext.getBean(RequestMappingInfo.class);
            environment.getProperty("");
//            requestMappingInfo.report();
        }
    }

    @Bean
    public RequestMappingInfo requestMappingInfo(){
        return new RequestMappingInfo();
    }
}
