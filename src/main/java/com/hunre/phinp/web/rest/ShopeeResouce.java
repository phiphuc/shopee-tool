package com.hunre.phinp.web.rest;

import com.hunre.phinp.repository.ShopMainRepository;
import com.hunre.phinp.service.ShopMainService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ShopeeResouce {
    private final Logger log = LoggerFactory.getLogger(ShopMainResource.class);

    private static final String ENTITY_NAME = "shopMain";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ShopMainRepository shopMainRepository;
    private final ShopMainService shopMainService;

    public ShopeeResouce(ShopMainRepository shopMainRepository, ShopMainService shopMainService) {
        this.shopMainRepository = shopMainRepository;
        this.shopMainService = shopMainService;
    }


}
