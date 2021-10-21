package com.kailas.mm.repository;

import com.kailas.mm.entity.Item;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends CrudRepository<Item,Integer> {
    Item findByItemCode(String itemCode);
    Item findByItemId(Integer itemId);
}
