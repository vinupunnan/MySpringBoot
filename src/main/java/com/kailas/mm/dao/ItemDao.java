package com.kailas.mm.dao;

import com.kailas.mm.entity.sql.Item;
import com.kailas.mm.model.dto.ItemDto;
import com.kailas.mm.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component

public class ItemDao {

    @Autowired
    ItemRepository itemRepository;
    public void saveItem(ItemDto itemDto){
        itemRepository.save(new Item(itemDto));
    }

}
