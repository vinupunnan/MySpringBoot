package com.kailas.mm.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kailas.mm.service.ItemService;
import com.kailas.mm.common.BaseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/items")
public class ItemController {
    @Autowired
    ItemService itemService;

   @GetMapping("{itemId}")
    public ResponseEntity<BaseResponse> getItem(@PathVariable Integer itemId) throws JsonProcessingException {


BaseResponse baseResponse = new BaseResponse(HttpStatus.OK.value(),"Success",itemService.getItem(itemId),null);
    return new ResponseEntity<>(baseResponse,HttpStatus.OK);
    }


    }


