package io.github.fire.dragonking.service;

import io.github.fire.dragonking.AppInterfaceInfo;
import io.github.fire.dragonking.domain.App;
import io.github.fire.dragonking.mapper.AppMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final ConcurrentHashMap<String, Integer> serverMap = new ConcurrentHashMap<>();
    private final AppMapper serverMapper;

    public void initServerMap(){
        List<App>  servers = serverMapper.getAllServer();
    }

    public void reportServiceInterfaceInfos(String serviceName, List<AppInterfaceInfo> serviceInterfaceInfos) {
        int id = 0;
        if(serverMap.contains(serviceName)){
            id =  serverMap.get(serviceName);
        }else{
            //添加
            App server = new App();
            server.setName(serviceName);
            serverMapper.add(server);
            id = server.getId();
        }

        for (AppInterfaceInfo serviceInterfaceInfo : serviceInterfaceInfos){

        }


    }
}
