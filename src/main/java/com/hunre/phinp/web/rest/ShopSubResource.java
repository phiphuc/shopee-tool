package com.hunre.phinp.web.rest;

import com.google.gson.Gson;
import com.hunre.phinp.domain.ShopSub;
import com.hunre.phinp.repository.ShopSubRepository;
import com.hunre.phinp.service.ShopSubService;
import com.hunre.phinp.service.dto.ShopSubDTO;
import com.hunre.phinp.web.rest.errors.BadRequestAlertException;

import domain.shopee.response.*;
import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.apache.kafka.common.errors.ApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

import java.net.URI;
import java.net.URISyntaxException;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * REST controller for managing {@link com.hunre.phinp.domain.ShopSub}.
 */
@RestController
@RequestMapping("/api")
public class ShopSubResource {

    private final Logger log = LoggerFactory.getLogger(ShopSubResource.class);

    private static final String ENTITY_NAME = "shopSub";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ShopSubRepository shopSubRepository;

    private final ShopSubService shopSubService;

    public ShopSubResource(ShopSubRepository shopSubRepository, ShopSubService shopSubService) {
        this.shopSubRepository = shopSubRepository;
        this.shopSubService = shopSubService;
    }

    /**
     * {@code POST  /shop-subs} : Create a new shopSub.
     *
     * @param shopSub the shopSub to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new shopSub, or with status {@code 400 (Bad Request)} if the shopSub has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/shop-subs")
    public ResponseEntity<ShopSub> createShopSub(@RequestBody ShopSub shopSub) throws URISyntaxException {
        log.debug("REST request to save ShopSub : {}", shopSub);
        if (shopSub.getId() != null) {
            throw new BadRequestAlertException("A new shopSub cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ShopSub result = shopSubRepository.save(shopSub);
        return ResponseEntity.created(new URI("/api/shop-subs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PostMapping("/shop-subs/otp")
    public DeferredResult<ResponseEntity<ShopSubResponse>> getOtp(@RequestBody ShopSubDTO shopSub) {
        log.debug("REST request to getOtp : {}", shopSub);

        DeferredResult<ResponseEntity<ShopSubResponse>> result = new DeferredResult<>();
        ShopSubResponse res  = new ShopSubResponse();
        if (shopSub.getShopId() == null) {
            res.setErrorCode(11);
            res.setMessage("Không có shop id");
            result.setResult(new ResponseEntity<>(res, HttpStatus.OK));
            return result;
        }

        Optional<List<ShopSub>> optionalShopSubs = shopSubRepository.findByUsernameAndStatus(shopSub.getUsername(), "ACTIVE");
        if(optionalShopSubs.isPresent()){
            res.setErrorCode(12);
            res.setMessage("Tài khoản đăng kí trùng");
            result.setResult(new ResponseEntity<>(res, HttpStatus.OK));
            return result;
        }
        CompletableFuture<String> reply = shopSubService.getOtp(shopSub);
        reply.thenAccept(msg -> {
                String msgTemp = String.valueOf(msg);
                Gson requestGson = new Gson();
                //error code = 1 thanh cong
                OtpResponse request = requestGson.fromJson(msgTemp, OtpResponse.class);
                ShopSub shopResult = new ShopSub();
                Optional<ShopSub> optionShop = shopSubRepository.findById(request.getId());
                if (optionShop.isPresent()) {
                    optionShop.ifPresent(shop -> {
                        shopResult.setId(shop.getId());
                        shopResult.setName(shop.getName());
                        shopResult.setCreateDate(shop.getCreateDate());
                        shopResult.setUpdateDate(ZonedDateTime.of(LocalDateTime.now(), ZoneId.of("Asia/Ho_Chi_Minh")));
                        shopResult.setPassword(shop.getPassword());
                        shopResult.setShopId(shop.getShopId());
                        shopResult.setToken(request.getCookie());
                        shopResult.setStatus(request.getErrorCode().equals(0) ? "SUCCESS" : "FALL");
                        shopResult.setMessage(request.getMessage());
                        shopResult.setUsername(shop.getUsername());
                        shopSubRepository.save(shopResult);
                        res.setData(shopResult);
                        res.setErrorCode(Integer.parseInt(request.getErrorCode()));
                    });
                }
                result.setResult(new ResponseEntity<>(res, HttpStatus.OK));
            }
        ).exceptionally(ex -> {
            result.setErrorResult(new ApiException());
            return null;
        });

        return result;

    }

    @PostMapping("/shop-subs/login")
    public DeferredResult<ResponseEntity<ShopSubResponse>> getLogin(@RequestBody ShopSubDTO shopSub) throws URISyntaxException {
        log.debug("REST request to getOtp : {}", shopSub);
        DeferredResult<ResponseEntity<ShopSubResponse>> result = new DeferredResult<>();
        if (shopSub.getShopId() == null) {

        }
       // Optional<List<ShopSub>> optionalShopSubs = shopSubRepository.findByUsernameAndStatus(shopSub.getUsername(),"ACTIVE");

        CompletableFuture<String> reply = shopSubService.getLogin(shopSub);
        ShopSubResponse res = new ShopSubResponse();
        reply.thenAccept(msg -> {
                String msgTemp = String.valueOf(msg);
                Gson requestGson = new Gson();

                //error code = 0 thanh cong, 2 là sai otp
            InformationKafkaResponse response = requestGson.fromJson(msgTemp, InformationKafkaResponse.class);
                ShopSub shopResult = new ShopSub();
                Optional<ShopSub> optionShop = shopSubRepository.findById(response.getId());
                if (optionShop.isPresent()) {
                    optionShop.ifPresent(shop -> {
                        shopResult.setId(shop.getId());
                        shopResult.setName(response.getUsername());
                        shopResult.setCreateDate(shop.getCreateDate());
                        shopResult.setUpdateDate(ZonedDateTime.of(LocalDateTime.now(), ZoneId.of("Asia/Ho_Chi_Minh")));
                        shopResult.setPassword(shop.getPassword());
                        shopResult.setShopId(shop.getShopId());
                        shopResult.setToken(response.getCookie());
                        shopResult.setStatus(response.getError().equals(0) ? "ACTIVE" : "INACTIVE");
                        shopResult.setUsername(shop.getUsername());
                        shopSubRepository.save(shopResult);
                        res.setData(shopResult);
                        res.setErrorCode(response.getError());
                    });
                }

                result.setResult(new ResponseEntity<>(res, HttpStatus.OK));
            }
        ).exceptionally(ex -> {
            result.setErrorResult(new ApiException());
            return null;
        });

        return result;

    }
    /**
     * {@code PUT  /shop-subs} : Updates an existing shopSub.
     *
     * @param shopSub the shopSub to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated shopSub,
     * or with status {@code 400 (Bad Request)} if the shopSub is not valid,
     * or with status {@code 500 (Internal Server Error)} if the shopSub couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/shop-subs")
    public ResponseEntity<ShopSub> updateShopSub(@RequestBody ShopSub shopSub) throws URISyntaxException {
        log.debug("REST request to update ShopSub : {}", shopSub);
        if (shopSub.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ShopSub result = shopSubRepository.save(shopSub);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, shopSub.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /shop-subs} : get all the shopSubs.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of shopSubs in body.
     */
    @GetMapping("/shop-subs")
    public List<ShopSub> getAllShopSubs() {
        log.debug("REST request to get all ShopSubs");
        return shopSubRepository.findAll();
    }

    /**
     * {@code GET  /shop-subs/:id} : get the "id" shopSub.
     *
     * @param id the id of the shopSub to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the shopSub, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/shop-subs/{id}")
    public ResponseEntity<ShopSub> getShopSub(@PathVariable Long id) {
        log.debug("REST request to get ShopSub : {}", id);
        Optional<ShopSub> shopSub = shopSubRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(shopSub);
    }

    /**
     * {@code DELETE  /shop-subs/:id} : delete the "id" shopSub.
     *
     * @param id the id of the shopSub to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/shop-subs/{id}")
    public ResponseEntity<Void> deleteShopSub(@PathVariable Long id) {
        log.debug("REST request to delete ShopSub : {}", id);
        shopSubRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
