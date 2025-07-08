package com.kailas.mm.service;

import com.kailas.mm.model.dto.ItemDto;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface ItemService {
    public ItemDto getItem(int itemId) ;
    List<ItemDto> getAllItems();

    public void saveItems(List<ItemDto> itemDtos) throws ExecutionException, InterruptedException;
}
