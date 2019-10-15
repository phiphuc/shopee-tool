package com.hunre.phinp.web.rest;

import com.hunre.phinp.domain.ShopMain;
import com.hunre.phinp.repository.ShopMainRepository;
import com.hunre.phinp.service.ShopMainService;
import com.hunre.phinp.web.rest.errors.BadRequestAlertException;

import domain.shopee.request.GetInformationRequest;
import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.errors.ApiException;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyFuture;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.support.SendResult;
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
import java.util.concurrent.ExecutionException;

/**
 * REST controller for managing {@link com.hunre.phinp.domain.ShopMain}.
 */
@RestController
@RequestMapping("/api")
public class ShopMainResource {

    private final Logger log = LoggerFactory.getLogger(ShopMainResource.class);

    private static final String ENTITY_NAME = "shopMain";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    @Autowired
    ReplyingKafkaTemplate<String, String,String> kafkaTemplate;

    private final ShopMainRepository shopMainRepository;
    private final ShopMainService shopMainService;

    public ShopMainResource(ShopMainRepository shopMainRepository, ShopMainService shopMainService) {
        this.shopMainRepository = shopMainRepository;
        this.shopMainService = shopMainService;
    }

    /**
     * {@code POST  /shop-mains} : Create a new shopMain.
     *
     * @param shopMain the shopMain to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new shopMain, or with status {@code 400 (Bad Request)} if the shopMain has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/shop-mains")
    public ResponseEntity<ShopMain> createShopMain(@RequestBody ShopMain shopMain) throws URISyntaxException, ExecutionException, InterruptedException {
        log.debug("REST request to save ShopMain : {}", shopMain);
        if (shopMain.getId() != null) {
            throw new BadRequestAlertException("A new shopMain cannot already have an ID", ENTITY_NAME, "idexists");
        }

//        DeferredResult<ResponseEntity<ShopMain>> result = new DeferredResult<>();
//        CompletableFuture<ShopMain> reply = shopMainService.createShopAsyns(shopMain);
//        reply.thenAccept(car ->
//            result.setResult(new ResponseEntity<>(car, HttpStatus.OK))
//        ).exceptionally(ex -> {
//            result.setErrorResult(new ApiException());
//            return null;
//        });

        shopMain.setCreateDate(ZonedDateTime.of(LocalDateTime.now(), ZoneId.of("Asia/Ho_Chi_Minh")));
        ShopMain shop = shopMainRepository.save(shopMain);
        GetInformationRequest request = new GetInformationRequest();
        request.setId(shop.getId());
        request.setUsername(shop.getLinkShop());
        // create producer record
        ProducerRecord<String, String> record = new ProducerRecord<>("get-username-request", request.toString());
        // set reply topic in header
        record.headers().add(new RecordHeader(KafkaHeaders.REPLY_TOPIC, "get-username-request".getBytes()));
        // post in kafka topic
        RequestReplyFuture<String, String, String> sendAndReceive = kafkaTemplate.sendAndReceive(record);

        // confirm if producer produced successfully
        SendResult<String, String> sendResult = sendAndReceive.getSendFuture().get();

        //print all headers
        sendResult.getProducerRecord().headers().forEach(header -> System.out.println(header.key() + ":" + header.value().toString()));

        // get consumer record
        ConsumerRecord<String, String> consumerRecord = sendAndReceive.get();
        log.info(consumerRecord.value());




//        ShopMain result = shopMainService.createShop(shopMain);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * {@code PUT  /shop-mains} : Updates an existing shopMain.
     *
     * @param shopMain the shopMain to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated shopMain,
     * or with status {@code 400 (Bad Request)} if the shopMain is not valid,
     * or with status {@code 500 (Internal Server Error)} if the shopMain couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/shop-mains")
    public ResponseEntity<ShopMain> updateShopMain(@RequestBody ShopMain shopMain) throws URISyntaxException {
        log.debug("REST request to update ShopMain : {}", shopMain);
        if (shopMain.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ShopMain result = shopMainRepository.save(shopMain);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, shopMain.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /shop-mains} : get all the shopMains.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of shopMains in body.
     */
    @GetMapping("/shop-mains")
    public List<ShopMain> getAllShopMains() {
        log.debug("REST request to get all ShopMains");
        return shopMainRepository.findAll();
    }

    /**
     * {@code GET  /shop-mains/:id} : get the "id" shopMain.
     *
     * @param id the id of the shopMain to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the shopMain, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/shop-mains/{id}")
    public ResponseEntity<ShopMain> getShopMain(@PathVariable Long id) {
        log.debug("REST request to get ShopMain : {}", id);
        Optional<ShopMain> shopMain = shopMainRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(shopMain);
    }

    /**
     * {@code DELETE  /shop-mains/:id} : delete the "id" shopMain.
     *
     * @param id the id of the shopMain to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/shop-mains/{id}")
    public ResponseEntity<Void> deleteShopMain(@PathVariable Long id) {
        log.debug("REST request to delete ShopMain : {}", id);
        shopMainRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
