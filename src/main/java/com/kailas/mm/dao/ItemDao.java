package com.kailas.mm.dao;

import com.kailas.mm.model.entity.sql.Item;
import com.kailas.mm.model.dto.ItemDto;
import com.kailas.mm.repository.ItemRepository;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component

public class ItemDao {

    @Autowired
    ItemRepository itemRepository;
    public void saveItem(ItemDto itemDto){

       // Session
        itemRepository.save(new Item(itemDto));
    }

}
