package io.github.fire.dragonking;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AppInterfaceInfo {


    private String url = "";
    private String className;
    private String method;
    private String requestMethod = "";

    public void setRequestMethod(String requestMethod) {
        if (requestMethod != null){
            this.requestMethod = requestMethod;
        }

    }

    public void setUrl(String url) {
        if (url != null){
            this.url = url;
        }
    }
}
