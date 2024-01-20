package com.kailas.mm.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kailas.mm.model.dto.ItemDto;
import com.kailas.mm.service.ItemService;
import com.kailas.mm.common.BaseResponse;


import com.kailas.mm.service.KafkaService;
import com.kailas.mm.service.impl.KafkaServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/items")
public class ItemController {
    @Autowired
    @Qualifier("itemServiceImpl")
    ItemService itemService;

    @Autowired
    KafkaServiceImpl kafkaService;

    @Value("${item.discount.price}")
    private String itemDiscount;





    @PostMapping("/new")
    public ResponseEntity<BaseResponse> createNewEmployee( @RequestBody ItemDto itemDto){
        System.out.println("Item Discount " +itemDiscount);
      itemService.saveItem(itemDto);
      //kafkaService.sentToKafka(itemDto);
    return null;
    }

   @GetMapping("/itemId")
    public ResponseEntity<BaseResponse> getItem(@RequestParam  Integer itemId) throws JsonProcessingException {
      // beanCreation.printGreeting();

     BaseResponse baseResponse = new BaseResponse(HttpStatus.OK.value(),"Success",itemService.getItem(itemId),null);
    return new ResponseEntity<>(baseResponse,HttpStatus.OK);

    }

   @GetMapping("/")
    public ResponseEntity<List<ItemDto>> getAllItems(){
    List<ItemDto> itemList = itemService.getAllItems();
       return new ResponseEntity<>(itemList,HttpStatus.OK);
}
    @PostMapping("/kafka")
    public void createEmployee(@RequestBody ItemDto item) {
       String ster = item.getItemCode();
        String desc = item.getItemDescription();

    }
    }


