package com.hunre.phinp.service;

import com.hunre.phinp.domain.ShopMain;
import com.hunre.phinp.repository.ShopMainRepository;
import domain.shopee.request.GetInformationRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.concurrent.CompletableFuture;


@Service
@Transactional
public class ShopMainService {
    private final Logger log = LoggerFactory.getLogger(ShopMainService.class);

    @Value("${cloud.topic.username.request}")
    private String usernameRequest;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

//    @Autowired
//    private CompletableFutureReplyingKafkaOperations<String, String, String> requestReplyKafkaTemplate;

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
//    public CompletableFuture<ShopMain> createShopAsyns(ShopMain shopMain) {
//        shopMain.setCreateDate(ZonedDateTime.of(LocalDateTime.now(), ZoneId.of("Asia/Ho_Chi_Minh")));
//        ShopMain shop = shopMainRepository.save(shopMain);
//        GetInformationRequest request = new GetInformationRequest();
//        request.setId(shop.getId());
//        request.setUsername(shop.getLinkShop());
//        CompletableFuture<String> x=  requestReplyKafkaTemplate.requestReply(usernameRequest, request.toString());
//        return null;
//    }



}
