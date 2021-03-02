package io.github.fire.dragonking.controller;

import io.github.fire.dragonking.AppInterfaceInfo;
import io.github.fire.dragonking.service.ReportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("report")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @PostMapping(value = "{appName}/{appType}/appInterfaceInfos", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void serviceInterfaceInfos(HttpServletRequest request, @PathVariable("appName") String appName, @PathVariable("appType") String appType, @RequestBody List<AppInterfaceInfo> serviceInterfaceInfos) {

        String remoteAddr = request.getRemoteAddr();
        System.out.println(remoteAddr);
        reportService.reportServiceInterfaceInfos(appName, appType, remoteAddr, serviceInterfaceInfos);

    }

}
