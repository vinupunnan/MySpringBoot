package com.kailas.mm.service.impl;


import com.kailas.mm.configuration.ThresholdProperties;
import com.kailas.mm.model.dto.LabResultDto;
import com.kailas.mm.model.dto.Priority;
import com.kailas.mm.model.properties.ThresholdValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class LabResultPriorityClassifier {
     @Autowired
    ThresholdProperties thresholdProperties;


    public void calssify(LabResultDto labResultDto){

        ThresholdValue thresholdValue = thresholdProperties.getThresholds().get(labResultDto.getTestCode());
        double labReuslt = labResultDto.getValue();
        if (labReuslt>thresholdValue.getCriticalHigh() || labReuslt <thresholdValue.getCriticalLow()){
            labResultDto.setPriority(Priority.CRITICAL);
        }

    }

}
