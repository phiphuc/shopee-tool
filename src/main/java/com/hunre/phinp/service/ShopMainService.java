package com.hunre.phinp.service;

import com.google.gson.Gson;
import com.hunre.phinp.config.kafka.CompletableFutureReplyingKafkaOperations;
import com.hunre.phinp.domain.ShopMain;
import com.hunre.phinp.repository.ShopMainRepository;
import domain.shopee.request.GetInformationRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.concurrent.CompletableFuture;


@Service
@Component
public class ShopMainService {
    private final Logger log = LoggerFactory.getLogger(ShopMainService.class);

    @Value("${cloud.topic.username.request}")
    private String usernameRequest;

    @Autowired
    private CompletableFutureReplyingKafkaOperations<String, String, String> requestReplyKafkaTemplate;

    private ShopMainRepository shopMainRepository;

    public ShopMainService(ShopMainRepository shopMainRepository) {
        this.shopMainRepository = shopMainRepository;
    }

//    public ShopMain createShop(ShopMain shopMain) {
//        shopMain.setCreateDate(ZonedDateTime.of(LocalDateTime.now(), ZoneId.of("Asia/Ho_Chi_Minh")));
//        ShopMain shop = shopMainRepository.save(shopMain);
//        GetInformationRequest request = new GetInformationRequest();
//        request.setId(shop.getId());
//        request.setUsername(shop.getLinkShop());
//        this.kafkaTemplate.send(usernameRequest, request.toString());
//        return shop;
//    }
//
    public CompletableFuture<String> createShopAsyns(ShopMain shopMain) {
        shopMain.setCreateDate(ZonedDateTime.of(LocalDateTime.now(), ZoneId.of("Asia/Ho_Chi_Minh")));
        ShopMain shop = shopMainRepository.save(shopMain);
        GetInformationRequest request = new GetInformationRequest();
        request.setId(shop.getId());
        request.setUsername(shop.getLinkShop());
        return requestReplyKafkaTemplate.requestReply(usernameRequest, request.toString());

    }



}
