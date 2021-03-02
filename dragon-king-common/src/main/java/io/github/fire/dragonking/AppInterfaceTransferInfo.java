package io.github.fire.dragonking;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
public class AppInterfaceTransferInfo extends AppInterfaceInfo{

    private AppType appType;
    private long entryMethodTime;
    private long outMethodTime;
    private int requestStatus;
    //    private String applicationName;
    private String remoteHost;
}
