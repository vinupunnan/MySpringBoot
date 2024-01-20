package com.kailas.mm.service.impl;

import com.kailas.mm.service.ItemService;
import com.kailas.mm.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
public class TestServiceImpl implements TestService {

    @Autowired
    @Lazy
    ItemService itemService;
    @Override
    public void addTest() {

    }
}
