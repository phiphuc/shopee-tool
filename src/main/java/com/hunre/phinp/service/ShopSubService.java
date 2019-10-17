package com.hunre.phinp.service;

import com.hunre.phinp.config.kafka.CompletableFutureReplyingKafkaOperations;
import com.hunre.phinp.domain.ShopMain;
import com.hunre.phinp.domain.ShopSub;
import com.hunre.phinp.repository.ShopMainRepository;
import com.hunre.phinp.repository.ShopSubRepository;
import com.hunre.phinp.service.dto.ShopSubDTO;
import com.hunre.phinp.web.rest.ShopSubResource;
import domain.shopee.request.LoginRequest;
import domain.shopee.request.OtpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
public class ShopSubService {
    private final Logger log = LoggerFactory.getLogger(ShopSubResource.class);

    @Value("${cloud.topic.otp.request}")
    private String otp;

    @Value("${cloud.topic.login.request}")
    private String login;

    @Autowired
    private CompletableFutureReplyingKafkaOperations<String, String, String> requestReplyKafkaTemplate;

    private final ShopSubRepository shopSubRepository;
    private final ShopMainRepository shopMainRepository;

    public ShopSubService(ShopSubRepository shopSubRepository, ShopMainRepository shopMainRepository) {
        this.shopSubRepository = shopSubRepository;
        this.shopMainRepository = shopMainRepository;
    }

    public CompletableFuture<String> getOtp(ShopSubDTO shopSubDTO){
        ShopSub shopSub = new ShopSub();
        shopSub.setPassword(shopSubDTO.getPassword());
        shopSub.setUsername(shopSubDTO.getUsername());
        shopSub.createDate(ZonedDateTime.of(LocalDateTime.now(), ZoneId.of("Asia/Ho_Chi_Minh")));
        shopSub.setShopId(shopMainRepository.findById(shopSubDTO.getShopId()).get());

        Optional<ShopSub> lstShopSub = shopSubRepository.findByUsername(shopSubDTO.getUsername());
        lstShopSub.ifPresent(shopSub1 -> {

        });
        ShopSub sub = shopSubRepository.save(shopSub);
        OtpRequest otpRequest =  new OtpRequest();
        otpRequest.setId(sub.getId());
        otpRequest.setPhone(shopSub.getUsername());
        otpRequest.setPassword(shopSub.getPassword());
        return requestReplyKafkaTemplate.requestReply(otp, otpRequest.toString());
    }

    public CompletableFuture<String> getLogin(ShopSubDTO shopSubDTO){
        LoginRequest request = new LoginRequest();
        request.setId(shopSubDTO.getId());
        request.setCookie(shopSubDTO.getToken());
        request.setPhone(shopSubDTO.getUsername());
        request.setOtp(shopSubDTO.getOtp());
        return requestReplyKafkaTemplate.requestReply(login, request.toString());
    }
}
