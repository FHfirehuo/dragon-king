package io.github.fire.dragonking.mapper;


import io.github.fire.dragonking.domain.App;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AppMapper {

    List<App> getAllServer();

    void add(App server);
}
