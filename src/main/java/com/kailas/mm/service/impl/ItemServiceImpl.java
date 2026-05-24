package com.kailas.mm.service.impl;

import com.kailas.mm.kafkaop.KafkaPublisher;
import com.kailas.mm.model.entity.sql.Item;
import com.kailas.mm.exception.ItemNotFoundException;
import com.kailas.mm.model.dto.ItemDto;

import com.kailas.mm.repository.ItemRepository;

import com.kailas.mm.service.ItemService;
import com.kailas.mm.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    private KafkaPublisher kafkaPublisher;

    // @Autowired //Setter Injection
    // // Non immutabale.No mandatory ,But no circular dependency when used with
    // @Lazy Annotation
    // public void setItemRepository(ItemRepository itemRepository) {
    // this.itemRepository = itemRepository;
    // }

    // @Autowired
    // public ItemServiceImpl(ItemRepository itemRepository) {
    // this.itemRepository = itemRepository;
    // }

    @Autowired
    @Lazy
    TestService service;

    @Override
    public ItemDto getItem(int itemId) {
        Optional<Item> test = itemRepository.findById(itemId);
        Pageable pageable = PageRequest.of(1, 1);
        Page<Item> items = itemRepository.findAll(pageable);
        if (test.isPresent()) {
            System.out.println("PRESEEEENt");
        } else {
            System.out.println("Not Present");
        }
        List<String> errorIds = new ArrayList<>();

        Item item = itemRepository.findByItemId(itemId);
        if (null == item) {

            errorIds.add(String.valueOf(itemId));
            throw new ItemNotFoundException("Item Not found", errorIds);
        } else {

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
    public void saveItems(List<ItemDto> itemDtos) throws ExecutionException, InterruptedException {
        List<Item> itemList = itemDtos.stream().map(itemDto -> new Item(itemDto)).collect(Collectors.toList());
        itemRepository.saveAll(itemList);
        // for (ItemDto dto : itemDtos) {
        // kafkaPublisher.publishToTopic("ItemTopic", dto.getItemCode(), dto);
        // }
    }
}