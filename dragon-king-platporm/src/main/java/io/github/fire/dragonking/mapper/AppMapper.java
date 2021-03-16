package io.github.fire.dragonking.mapper;


import io.github.fire.dragonking.AppInterfaceInfo;
import io.github.fire.dragonking.domain.App;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AppMapper {

    List<App> getAllApp();

    void add(App server);

    Long getInterface(@Param("appId") int appId, @Param("info") AppInterfaceInfo info);

    void addNewInterface(@Param("appId") int appId, @Param("interfaceType") String interfaceType, @Param("info") AppInterfaceInfo serviceInterfaceInfo);

    void update(long id);

    Long getWebInterface(@Param("appId") int appId, @Param("info") AppInterfaceInfo info);

    int getIdByName(String appName);
}
