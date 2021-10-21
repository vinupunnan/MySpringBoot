package com.kailas.mm.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.kailas.mm.client.InstructionSetResponse;
import com.kailas.mm.entity.Item;
import com.kailas.mm.model.dto.ItemDto;

import com.kailas.mm.repository.ItemRepository;
import com.kailas.mm.service.ItemService;


//import inrs.hc.hearth.cleaner.entities.AddressCleaner;
//import inrs.hc.hearth.cleaner.entities.StatCleaner;
//import inrs.hc.hearth.cleaner.entities.ZipCleaner;
//import inrs.hc.hearth.cleaner.factory.CleanerFactrory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    ItemRepository itemRepository;



    @Override
    public ItemDto getItem(int itemId) throws JsonProcessingException {
        Item item = itemRepository.findByItemId(itemId);
        ItemDto itemDto = new ItemDto(item);
      // String str =   clientTest.clientResponseByPayerName("Humana");
//str = str = str.substring(1, str.length() - 1);
//      InstructionSetResponse test = new ObjectMapper().readValue(str, InstructionSetResponse.class);
//

      //  System.out.println(test.toString());
//        try {
////            ObjectMapper mapper = new ObjectMapper();
////            String json = mapper.writeValueAsString(itemDto);
////            System.out.println("ResultingJSONstring = " + json);
////            CleanerFactrory fact = new CleanerFactrory();
////            StatCleaner cleaner = (StatCleaner) fact.getPlan("State");
////            String str =  cleaner.perform();
////            ZipCleaner zipcl = (ZipCleaner) fact.getPlan("Zip");
////            String test2 =  zipcl.perform();
////            AddressCleaner add = (AddressCleaner) fact.getPlan("Address");
////            String test3 =  add.perform();
////            System.out.println(test3);
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//        }
//
   // }
        return itemDto;
}}