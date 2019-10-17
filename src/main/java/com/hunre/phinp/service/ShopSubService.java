package com.hunre.phinp.service;

import com.hunre.phinp.config.kafka.CompletableFutureReplyingKafkaOperations;
import com.hunre.phinp.domain.ShopSub;
import com.hunre.phinp.repository.ShopSubRepository;
import com.hunre.phinp.web.rest.ShopSubResource;
import domain.shopee.request.OtpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class ShopSubService {
    private final Logger log = LoggerFactory.getLogger(ShopSubResource.class);

    @Value("${cloud.topic.otp.request}")
    private String otp;

    @Autowired
    private CompletableFutureReplyingKafkaOperations<String, String, String> requestReplyKafkaTemplate;

    private final ShopSubRepository shopSubRepository;

    public ShopSubService(ShopSubRepository shopSubRepository) {
        this.shopSubRepository = shopSubRepository;
    }

    public CompletableFuture<String> getOtp(ShopSub shopSub){
        ShopSub sub = shopSubRepository.save(shopSub);
        OtpRequest otpRequest =  new OtpRequest();
        otpRequest.setId(sub.getId());
        otpRequest.setPhone(shopSub.getUsername());
        otpRequest.setPassword(shopSub.getPassword());
        return requestReplyKafkaTemplate.requestReply(otp, otpRequest.toString());
    }
}
