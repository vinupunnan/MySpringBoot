package com.kailas.mm.service.impl;

import com.kailas.mm.entity.sql.Item;
import com.kailas.mm.exception.ItemNotFoundException;
import com.kailas.mm.model.dto.ItemDto;

import com.kailas.mm.repository.ItemRepository;

import com.kailas.mm.service.ItemService;
import com.kailas.mm.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ItemServiceImpl implements ItemService {


       @Autowired
        ItemRepository itemRepository;

       @Autowired
       @Lazy
       TestService service ;


//    @Autowired Setter Injection
    //Non immutabale.No mandatory ,But no circular dependency when used with @Lazy Annotation
//    public void setItemRepository(ItemRepository itemRepository) {
//        this.itemRepository = itemRepository;
//    }

//    @Autowired
//    public ItemServiceImpl(ItemRepository itemRepository) {
//        this.itemRepository = itemRepository;
//    }
//
//    @Autowired
//    private Logger logger;

    @Override
    public ItemDto getItem(int itemId)  {
        Optional<Item> test=      itemRepository.findById(itemId);

        if( test.isPresent()){
            System.out.println("PRESEEEENt");
        }else{
            System.out.println("Not Present");
        }


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