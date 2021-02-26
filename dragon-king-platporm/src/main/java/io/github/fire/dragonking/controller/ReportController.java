package io.github.fire.dragonking.controller;

import io.github.fire.dragonking.AppInterfaceInfo;
import io.github.fire.dragonking.service.ReportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("report")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @PostMapping(value = "{appName}/appInterfaceInfos", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void serviceInterfaceInfos(@PathVariable("appName") String appName, @RequestBody List<AppInterfaceInfo> serviceInterfaceInfos) {

        log.info("serviceInterfaceInfos  received {}  info {}",  appName, serviceInterfaceInfos);

        reportService.reportServiceInterfaceInfos(appName, serviceInterfaceInfos);

    }

}
