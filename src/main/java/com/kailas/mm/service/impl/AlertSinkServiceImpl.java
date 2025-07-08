package com.kailas.mm.service.impl;

import com.kailas.mm.exception.ItemNotFoundException;
import com.kailas.mm.model.dto.AlertDto;
import com.kailas.mm.model.entity.sql.Item;
import com.kailas.mm.repository.ItemRepository;
import com.kailas.mm.service.AlertSinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class AlertSinkServiceImpl  implements AlertSinkService {

@Autowired
    ItemRepository itemRepository;

    public void saveAlerts(AlertDto alertDto) {
        List<Map<String,String>> dataList = alertDto.getData();
        for(Map<String,String> m:dataList){
           String itemId = m.get("itemId");
         Optional<Item> optItem =  itemRepository.findById(Integer.parseInt(itemId));
            if(optItem.get() == null){
                throw new ItemNotFoundException("The item is not found in the dab");
            }
        }

    }
}
