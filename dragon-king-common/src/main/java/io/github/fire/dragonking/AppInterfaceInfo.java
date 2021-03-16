package io.github.fire.dragonking;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AppInterfaceInfo {


    private String uri = "";
    private String className;
    private String method;
    private String requestMethod = "";

    public void setRequestMethod(String requestMethod) {
        if (requestMethod != null) {
            this.requestMethod = requestMethod;
        }

    }

    public void setUri(String uri) {
        if (uri != null) {
            this.uri = uri;
        }
    }
}
