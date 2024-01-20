package com.kailas.mm.service;

import com.kailas.mm.model.dto.ItemDto;

import java.util.List;

public interface ItemService {
    public ItemDto getItem(int itemId) ;
    List<ItemDto> getAllItems();

    public void saveItem(ItemDto itemDto);
}
