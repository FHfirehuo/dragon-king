package io.github.fire.dragonking;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class UrlInterceptor implements HandlerInterceptor {

    private final String ENTRY_METHOD_TIME = "entryMethodTime";

    private static String localHost;

    static {
        try {
            localHost = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        request.setAttribute(ENTRY_METHOD_TIME, System.currentTimeMillis());
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

        if (!(handler instanceof HandlerMethod)) {
            return;
        }

        //ResourceHttpRequestHandler
        HandlerMethod handlerMethod = (HandlerMethod) handler;

        if (!handlerMethod.hasMethodAnnotation(RequestMapping.class)) {
            return;
        }

        RequestMapping methodAnnotation = handlerMethod.getMethodAnnotation(RequestMapping.class);
        String methodAnnotationFirstValues = "";
        String[] methodAnnotationValues = methodAnnotation.value();
        if (methodAnnotationValues.length > 0) {
            methodAnnotationFirstValues = methodAnnotationValues[0];
        }

        Class clazz = handlerMethod.getBeanType();
        RequestMapping classAnnotation = (RequestMapping) clazz.getAnnotation(RequestMapping.class);
        String classAnnotationFirstPath = "";
        if (classAnnotation != null) {
            String[] classAnnotationPath = classAnnotation.value();
            classAnnotationFirstPath = classAnnotationPath[0];
        }

        String uri = "/" + classAnnotationFirstPath + "/" + methodAnnotationFirstValues;

        AppInterfaceTransferInfo interfaceInfo = new AppInterfaceTransferInfo();
        interfaceInfo.setUri(uri.replaceAll("//", "/"));
        interfaceInfo.setClassName(handlerMethod.getBeanType().getName());
        interfaceInfo.setMethod(handlerMethod.getMethod().getName());
        interfaceInfo.setRequestMethod(request.getMethod());

        interfaceInfo.setEntryMethodTime((Long) request.getAttribute(ENTRY_METHOD_TIME));
        interfaceInfo.setOutMethodTime(System.currentTimeMillis());
        interfaceInfo.setRequestStatus(response.getStatus());
        interfaceInfo.setRemoteAddr(request.getRemoteAddr());
        interfaceInfo.setAppType(AppType.WEB);
        interfaceInfo.setReceiveHost(localHost);

        DragonKingClient.getInstance().reportAppInterfaceTransferInfo(interfaceInfo);

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
