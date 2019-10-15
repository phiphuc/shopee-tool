package com.hunre.phinp.web.rest;

import com.hunre.phinp.ShopeeApp;
import com.hunre.phinp.domain.ShopSub;
import com.hunre.phinp.repository.ShopSubRepository;
import com.hunre.phinp.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.List;

import static com.hunre.phinp.web.rest.TestUtil.sameInstant;
import static com.hunre.phinp.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link ShopSubResource} REST controller.
 */
@SpringBootTest(classes = ShopeeApp.class)
public class ShopSubResourceIT {

    private static final String DEFAULT_USERNAME = "AAAAAAAAAA";
    private static final String UPDATED_USERNAME = "BBBBBBBBBB";

    private static final String DEFAULT_PASSWORD = "AAAAAAAAAA";
    private static final String UPDATED_PASSWORD = "BBBBBBBBBB";

    private static final String DEFAULT_TOKEN = "AAAAAAAAAA";
    private static final String UPDATED_TOKEN = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_CREATE_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATE_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_UPDATE_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_UPDATE_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    @Autowired
    private ShopSubRepository shopSubRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restShopSubMockMvc;

    private ShopSub shopSub;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ShopSubResource shopSubResource = new ShopSubResource(shopSubRepository);
        this.restShopSubMockMvc = MockMvcBuilders.standaloneSetup(shopSubResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ShopSub createEntity(EntityManager em) {
        ShopSub shopSub = new ShopSub()
            .username(DEFAULT_USERNAME)
            .password(DEFAULT_PASSWORD)
            .token(DEFAULT_TOKEN)
            .name(DEFAULT_NAME)
            .createDate(DEFAULT_CREATE_DATE)
            .updateDate(DEFAULT_UPDATE_DATE);
        return shopSub;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ShopSub createUpdatedEntity(EntityManager em) {
        ShopSub shopSub = new ShopSub()
            .username(UPDATED_USERNAME)
            .password(UPDATED_PASSWORD)
            .token(UPDATED_TOKEN)
            .name(UPDATED_NAME)
            .createDate(UPDATED_CREATE_DATE)
            .updateDate(UPDATED_UPDATE_DATE);
        return shopSub;
    }

    @BeforeEach
    public void initTest() {
        shopSub = createEntity(em);
    }

    @Test
    @Transactional
    public void createShopSub() throws Exception {
        int databaseSizeBeforeCreate = shopSubRepository.findAll().size();

        // Create the ShopSub
        restShopSubMockMvc.perform(post("/api/shop-subs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(shopSub)))
            .andExpect(status().isCreated());

        // Validate the ShopSub in the database
        List<ShopSub> shopSubList = shopSubRepository.findAll();
        assertThat(shopSubList).hasSize(databaseSizeBeforeCreate + 1);
        ShopSub testShopSub = shopSubList.get(shopSubList.size() - 1);
        assertThat(testShopSub.getUsername()).isEqualTo(DEFAULT_USERNAME);
        assertThat(testShopSub.getPassword()).isEqualTo(DEFAULT_PASSWORD);
        assertThat(testShopSub.getToken()).isEqualTo(DEFAULT_TOKEN);
        assertThat(testShopSub.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testShopSub.getCreateDate()).isEqualTo(DEFAULT_CREATE_DATE);
        assertThat(testShopSub.getUpdateDate()).isEqualTo(DEFAULT_UPDATE_DATE);
    }

    @Test
    @Transactional
    public void createShopSubWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = shopSubRepository.findAll().size();

        // Create the ShopSub with an existing ID
        shopSub.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restShopSubMockMvc.perform(post("/api/shop-subs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(shopSub)))
            .andExpect(status().isBadRequest());

        // Validate the ShopSub in the database
        List<ShopSub> shopSubList = shopSubRepository.findAll();
        assertThat(shopSubList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllShopSubs() throws Exception {
        // Initialize the database
        shopSubRepository.saveAndFlush(shopSub);

        // Get all the shopSubList
        restShopSubMockMvc.perform(get("/api/shop-subs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(shopSub.getId().intValue())))
            .andExpect(jsonPath("$.[*].username").value(hasItem(DEFAULT_USERNAME)))
            .andExpect(jsonPath("$.[*].password").value(hasItem(DEFAULT_PASSWORD)))
            .andExpect(jsonPath("$.[*].token").value(hasItem(DEFAULT_TOKEN)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].createDate").value(hasItem(sameInstant(DEFAULT_CREATE_DATE))))
            .andExpect(jsonPath("$.[*].updateDate").value(hasItem(sameInstant(DEFAULT_UPDATE_DATE))));
    }
    
    @Test
    @Transactional
    public void getShopSub() throws Exception {
        // Initialize the database
        shopSubRepository.saveAndFlush(shopSub);

        // Get the shopSub
        restShopSubMockMvc.perform(get("/api/shop-subs/{id}", shopSub.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(shopSub.getId().intValue()))
            .andExpect(jsonPath("$.username").value(DEFAULT_USERNAME))
            .andExpect(jsonPath("$.password").value(DEFAULT_PASSWORD))
            .andExpect(jsonPath("$.token").value(DEFAULT_TOKEN))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.createDate").value(sameInstant(DEFAULT_CREATE_DATE)))
            .andExpect(jsonPath("$.updateDate").value(sameInstant(DEFAULT_UPDATE_DATE)));
    }

    @Test
    @Transactional
    public void getNonExistingShopSub() throws Exception {
        // Get the shopSub
        restShopSubMockMvc.perform(get("/api/shop-subs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateShopSub() throws Exception {
        // Initialize the database
        shopSubRepository.saveAndFlush(shopSub);

        int databaseSizeBeforeUpdate = shopSubRepository.findAll().size();

        // Update the shopSub
        ShopSub updatedShopSub = shopSubRepository.findById(shopSub.getId()).get();
        // Disconnect from session so that the updates on updatedShopSub are not directly saved in db
        em.detach(updatedShopSub);
        updatedShopSub
            .username(UPDATED_USERNAME)
            .password(UPDATED_PASSWORD)
            .token(UPDATED_TOKEN)
            .name(UPDATED_NAME)
            .createDate(UPDATED_CREATE_DATE)
            .updateDate(UPDATED_UPDATE_DATE);

        restShopSubMockMvc.perform(put("/api/shop-subs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedShopSub)))
            .andExpect(status().isOk());

        // Validate the ShopSub in the database
        List<ShopSub> shopSubList = shopSubRepository.findAll();
        assertThat(shopSubList).hasSize(databaseSizeBeforeUpdate);
        ShopSub testShopSub = shopSubList.get(shopSubList.size() - 1);
        assertThat(testShopSub.getUsername()).isEqualTo(UPDATED_USERNAME);
        assertThat(testShopSub.getPassword()).isEqualTo(UPDATED_PASSWORD);
        assertThat(testShopSub.getToken()).isEqualTo(UPDATED_TOKEN);
        assertThat(testShopSub.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testShopSub.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testShopSub.getUpdateDate()).isEqualTo(UPDATED_UPDATE_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingShopSub() throws Exception {
        int databaseSizeBeforeUpdate = shopSubRepository.findAll().size();

        // Create the ShopSub

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restShopSubMockMvc.perform(put("/api/shop-subs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(shopSub)))
            .andExpect(status().isBadRequest());

        // Validate the ShopSub in the database
        List<ShopSub> shopSubList = shopSubRepository.findAll();
        assertThat(shopSubList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteShopSub() throws Exception {
        // Initialize the database
        shopSubRepository.saveAndFlush(shopSub);

        int databaseSizeBeforeDelete = shopSubRepository.findAll().size();

        // Delete the shopSub
        restShopSubMockMvc.perform(delete("/api/shop-subs/{id}", shopSub.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ShopSub> shopSubList = shopSubRepository.findAll();
        assertThat(shopSubList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ShopSub.class);
        ShopSub shopSub1 = new ShopSub();
        shopSub1.setId(1L);
        ShopSub shopSub2 = new ShopSub();
        shopSub2.setId(shopSub1.getId());
        assertThat(shopSub1).isEqualTo(shopSub2);
        shopSub2.setId(2L);
        assertThat(shopSub1).isNotEqualTo(shopSub2);
        shopSub1.setId(null);
        assertThat(shopSub1).isNotEqualTo(shopSub2);
    }
}
