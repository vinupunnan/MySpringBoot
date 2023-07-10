package com.kailas.mm.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kailas.mm.entity.Item;
import com.kailas.mm.exception.ItemNotFoundException;
import com.kailas.mm.model.dto.ItemDto;

import java.util.List;

public interface ItemService {
    public ItemDto getItem(int itemId) ;
    List<ItemDto> getAllItems();
}
