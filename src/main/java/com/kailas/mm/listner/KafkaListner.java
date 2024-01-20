package com.kailas.mm.listner;


import com.kailas.mm.model.dto.ItemDto;
//import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaListner {
//    @KafkaListener(topics = "ItemTopic",
//            groupId = "group-id")
    public void consume(ItemDto item)
    {
      System.out.println(item.getItemCode());
    }
}
