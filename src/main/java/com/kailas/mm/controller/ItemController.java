package com.kailas.mm.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kailas.mm.model.dto.ItemDto;
import com.kailas.mm.service.ItemService;
import com.kailas.mm.common.BaseResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/items")
public class ItemController {
    @Autowired
    ItemService itemService;

   @GetMapping("/itemId")
    public ResponseEntity<BaseResponse> getItem(@RequestParam @NotNull Integer itemId) throws JsonProcessingException {
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


