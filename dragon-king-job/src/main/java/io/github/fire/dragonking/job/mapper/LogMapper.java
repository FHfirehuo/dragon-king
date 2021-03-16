package io.github.fire.dragonking.job.mapper;

import io.github.fire.dragonking.AppInterfaceTransferInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface LogMapper {

    void insert(@Param("appName")String appName, @Param("appInterfaceInfo") AppInterfaceTransferInfo appInterfaceInfo, @Param("consumingTime")long consumingTime);
}
