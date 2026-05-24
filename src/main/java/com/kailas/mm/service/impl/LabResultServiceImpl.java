package com.kailas.mm.service.impl;

import com.kailas.mm.model.dto.LabResultDto;
import com.kailas.mm.service.LabResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class LabResultServiceImpl implements LabResultService {

    @Autowired
    LabResultPriorityClassifier classsifier ;
    @Autowired
    KafkaTemplate kafkaTemplate;
    public void processLabResult(LabResultDto labResultDto) {
        classsifier.calssify(labResultDto);
        kafkaTemplate.send("topicName".)
    }


}
