package io.github.fire.dragonking.service;

import io.github.fire.dragonking.AppInterfaceInfo;
import io.github.fire.dragonking.AppType;
import io.github.fire.dragonking.domain.App;
import io.github.fire.dragonking.mapper.AppMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final AppMapper appMapper;

    @Async
    public void reportServiceInterfaceInfos(String appName, String interfaceType, String remoteAddr, List<AppInterfaceInfo> serviceInterfaceInfos) {
        Integer appId = appMapper.getIdByName(appName);
        if (appId == null) {
            //添加
            App app = new App();
            app.setName(appName);
            appMapper.add(app);
            appId = app.getId();
        }

        for (AppInterfaceInfo serviceInterfaceInfo : serviceInterfaceInfos) {
            Long interfaceId = 0L;
            if (Objects.equals(interfaceType, AppType.WEB)) {
                interfaceId = appMapper.getWebInterface(appId, serviceInterfaceInfo);
            } else {
                interfaceId = appMapper.getInterface(appId, serviceInterfaceInfo);
            }

            if (interfaceId != null) {
                //更新时间
                appMapper.update(interfaceId);
            } else {
                appMapper.addNewInterface(appId, interfaceType, serviceInterfaceInfo);
            }

        }


    }
}
