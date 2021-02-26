package io.github.fire.dragonking;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.condition.RequestMethodsRequestCondition;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RequestMappingInfo {

    private final static Logger LOGGER = LoggerFactory.getLogger(RequestMappingInfo.class);

    public void report(String dragonKingServerDomain, String dragonKingKafkaServers,String applicationName, RequestMappingHandlerMapping mapping){
        DragonKingClient.getInstance(dragonKingServerDomain, dragonKingKafkaServers);
        this.report(applicationName, mapping);
    }

    public void report(String applicationName, RequestMappingHandlerMapping mapping) {

        if (!StringUtils.hasLength(applicationName)){
            LOGGER.error("没有获取到spring.application.name属性,不启用DragonKing。");
            DragonKingClient.getInstance().notEnabled();
            return;
        }

        DragonKingClient.getInstance().enabled(applicationName);

        // 获取url与类和方法的对应信息
        Map<org.springframework.web.servlet.mvc.method.RequestMappingInfo, HandlerMethod> map = mapping.getHandlerMethods();

        List<AppInterfaceInfo> list = new ArrayList<>();
        for (Map.Entry<org.springframework.web.servlet.mvc.method.RequestMappingInfo, HandlerMethod> m : map.entrySet()) {
            AppInterfaceInfo interfaceInfo = new AppInterfaceInfo();
            org.springframework.web.servlet.mvc.method.RequestMappingInfo info = m.getKey();
            HandlerMethod method = m.getValue();
            PatternsRequestCondition p = info.getPatternsCondition();
            for (String url : p.getPatterns()) {
                interfaceInfo.setUrl(url);
            }
            interfaceInfo.setClassName(method.getMethod().getDeclaringClass().getName()); // 类名
            interfaceInfo.setMethod(method.getMethod().getName());// 方法名

            RequestMethodsRequestCondition methodsCondition = info.getMethodsCondition();
            for (RequestMethod requestMethod : methodsCondition.getMethods()) {
                interfaceInfo.setRequestMethod(requestMethod.toString());
            }

            list.add(interfaceInfo);
        }


        DragonKingClient.getInstance().reportAppInterfaceMateInfo(list);
    }
}
