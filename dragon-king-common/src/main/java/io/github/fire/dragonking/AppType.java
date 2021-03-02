package io.github.fire.dragonking;

public enum AppType {


    WEB("web"), RPC("rpc");

    private String value;

    private AppType(String value){
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
