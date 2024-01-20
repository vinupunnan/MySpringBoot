package com.kailas.mm.repository;

import com.kailas.mm.entity.sql.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item,Integer> {

    Item findByItemCode(String itemCode);
    Item findByItemId(Integer itemId);
    List<Item> findAll();

   Item  save(Item item);

}
