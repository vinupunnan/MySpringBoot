package com.kailas.mm.service;

import com.kailas.mm.model.dto.AlertDto;

public interface AlertSinkService {

    public void saveAlerts(AlertDto alertDto);

}
