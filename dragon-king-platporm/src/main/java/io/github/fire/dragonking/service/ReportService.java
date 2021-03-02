package io.github.fire.dragonking.service;

import io.github.fire.dragonking.AppInterfaceInfo;
import io.github.fire.dragonking.domain.App;
import io.github.fire.dragonking.mapper.AppMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final static ConcurrentHashMap<String, Integer> appMap = new ConcurrentHashMap<>();

    private final AppMapper appMapper;


    public void initServerMap() {
        List<App> apps = appMapper.getAllApp();
        apps.forEach(app -> {
            appMap.put(app.getName(), app.getId());
        });
    }

    @Async
    public void reportServiceInterfaceInfos(String serviceName, String interfaceType, String remoteAddr, List<AppInterfaceInfo> serviceInterfaceInfos) {
        int appId = 0;
        if (appMap.contains(serviceName)) {
            appId = appMap.get(serviceName);
        } else {
            //添加
            App app = new App();
            app.setName(serviceName);
            appMapper.add(app);
            appId = app.getId();
        }

        for (AppInterfaceInfo serviceInterfaceInfo : serviceInterfaceInfos) {
            Long id = appMapper.haveInterface(appId, serviceInterfaceInfo);
            if (id != null) {
                //更新时间
                appMapper.update(id);
            } else {
                appMapper.addNewInterface(appId, interfaceType, serviceInterfaceInfo);
            }

        }


    }
}
