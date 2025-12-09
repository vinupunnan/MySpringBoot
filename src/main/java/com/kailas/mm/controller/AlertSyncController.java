package com.kailas.mm.controller;

import com.kailas.mm.common.BaseResponse;
import com.kailas.mm.model.dto.AlertDto;
import com.kailas.mm.service.AlertSinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/alerts")
public class AlertSyncController {
    @Autowired
    AlertSinkService alertSinkService;
    @PostMapping("/sync")
    public ResponseEntity<BaseResponse> postAlerts(@RequestBody AlertDto alertData){
        String dcId = alertData.getDcId();
        alertSinkService.saveAlerts(alertData);
        BaseResponse response = new BaseResponse();
        return new ResponseEntity<>(response, HttpStatus.OK);

    }

}
