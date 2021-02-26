package io.github.fire.dragonking;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AppInterfaceInfo {


    private String url;
    private String className;
    private String method;
    private String requestMethod;
    private long entryMethodTime;
    private long outMethodTime;
    private int requestStatus;

}
