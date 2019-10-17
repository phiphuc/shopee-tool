package com.hunre.phinp.service;


import com.hunre.phinp.repository.ShopMainRepository;
import com.hunre.phinp.repository.ShopSubRepository;
import domain.shopee.request.OtpRequest;
import domain.shopee.response.OtpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class ShopeeService {
    private final Logger log = LoggerFactory.getLogger(ShopeeService.class);


    private ShopMainRepository shopMainRepository;
    private ShopSubRepository shopSubRepository;

    public ShopeeService(ShopMainRepository shopMainRepository, ShopSubRepository shopSubRepository) {
        this.shopMainRepository = shopMainRepository;
        this.shopSubRepository = shopSubRepository;
    }

    public void getOtp(OtpRequest request){

    }


}
