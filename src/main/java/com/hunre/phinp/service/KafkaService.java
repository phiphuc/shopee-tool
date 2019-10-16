package com.hunre.phinp.service;


import com.google.gson.Gson;
import com.hunre.phinp.domain.ShopMain;
import com.hunre.phinp.repository.ShopMainRepository;
import domain.shopee.response.GetIdByUsernameKafkaResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;


@Service
public class KafkaService {

    private final Logger log = LoggerFactory.getLogger(KafkaService.class);

    @Autowired
    private ShopMainRepository shopMainRepository;


    public void updateInformationShop(String msg,
                                       @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition,
                                       @Header(KafkaHeaders.OFFSET) int offsets,
                                       Acknowledgment acknowledgment) {
        acknowledgment.acknowledge();
        String msgTemp = String.valueOf(msg);
        Gson requestGson = new Gson();
        GetIdByUsernameKafkaResponse response = requestGson.fromJson(msgTemp, GetIdByUsernameKafkaResponse.class);
        Long id = response.getId();
        Optional<ShopMain> shopDb = shopMainRepository.findById(id);
        if(!shopDb.isPresent()){
            return;
        }
        ShopMain shop = shopDb.get();
        ShopMain shopMain = new ShopMain();
        shopMain.setId(response.getId());
        shopMain.setAddress(response.getAddress());
        shopMain.setError(response.getError());
        shopMain.setFollow(response.getFollow());
        shopMain.setFollowing(response.getFollowing());
        shopMain.setErrorMsg(response.getError_msg());
        shopMain.setName(response.getName());
        shopMain.setProduct(response.getProduct());
        shopMain.setRate(response.getRate());
        shopMain.setVersion(response.getVersion());
        shopMain.setUserId(response.getShopId());
        shopMain.setShopId(response.getShopId());
        shopMain.setUpdateDate(ZonedDateTime.of(LocalDateTime.now(), ZoneId.of("Asia/Ho_Chi_Minh")));
        shopMain.setLinkShop(shop.getLinkShop());
        shopMain.setCreateDate(shop.getCreateDate());
        shopMainRepository.save(shopMain);
    }
}
