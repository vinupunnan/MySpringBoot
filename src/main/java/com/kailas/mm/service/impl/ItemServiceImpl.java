package com.kailas.mm.service;

import com.kailas.mm.entity.sql.Item;
import com.kailas.mm.exception.ItemNotFoundException;
import com.kailas.mm.model.dto.ItemDto;

import com.kailas.mm.repository.ItemRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    ItemRepository itemRepository;
//
//    @Autowired
//    private Logger logger;

    @Override
    public ItemDto getItem(int itemId)  {
        Item item =
             itemRepository.findByItemId(itemId);
            if (null == item) {
               // logger.error("Item not found");
                throw new ItemNotFoundException("Item with id " +itemId +" Not found");
            }else{
        ItemDto itemDto = new ItemDto(item);
        return itemDto;
}
    }

    @Override
    public List<ItemDto> getAllItems() {
     List<Item> items = itemRepository.findAll();
    return items.stream().map(item -> new ItemDto(item)).collect(Collectors.toList());

    }

    @Override
    public void saveItem(ItemDto itemDto) {
        itemRepository.save(new Item (itemDto));

    }
}