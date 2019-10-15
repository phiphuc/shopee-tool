package com.hunre.phinp.web.rest;

import com.hunre.phinp.ShopeeApp;
import com.hunre.phinp.domain.ShopMain;
import com.hunre.phinp.repository.ShopMainRepository;
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
 * Integration tests for the {@link ShopMainResource} REST controller.
 */
@SpringBootTest(classes = ShopeeApp.class)
public class ShopMainResourceIT {

    private static final String DEFAULT_SHOP_ID = "AAAAAAAAAA";
    private static final String UPDATED_SHOP_ID = "BBBBBBBBBB";

    private static final String DEFAULT_USER_ID = "AAAAAAAAAA";
    private static final String UPDATED_USER_ID = "BBBBBBBBBB";

    private static final String DEFAULT_LINK_SHOP = "AAAAAAAAAA";
    private static final String UPDATED_LINK_SHOP = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PRODUCT = "AAAAAAAAAA";
    private static final String UPDATED_PRODUCT = "BBBBBBBBBB";

    private static final String DEFAULT_FOLLOW = "AAAAAAAAAA";
    private static final String UPDATED_FOLLOW = "BBBBBBBBBB";

    private static final String DEFAULT_FOLLOWING = "AAAAAAAAAA";
    private static final String UPDATED_FOLLOWING = "BBBBBBBBBB";

    private static final String DEFAULT_RATE = "AAAAAAAAAA";
    private static final String UPDATED_RATE = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_VERSION = "AAAAAAAAAA";
    private static final String UPDATED_VERSION = "BBBBBBBBBB";

    private static final String DEFAULT_ERROR_MSG = "AAAAAAAAAA";
    private static final String UPDATED_ERROR_MSG = "BBBBBBBBBB";

    private static final String DEFAULT_ERROR = "AAAAAAAAAA";
    private static final String UPDATED_ERROR = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_CREATE_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATE_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_UPDATE_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_UPDATE_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    @Autowired
    private ShopMainRepository shopMainRepository;

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

    private MockMvc restShopMainMockMvc;

    private ShopMain shopMain;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
//        final ShopMainResource shopMainResource = new ShopMainResource(shopMainRepository);
//        this.restShopMainMockMvc = MockMvcBuilders.standaloneSetup(shopMainResource)
//            .setCustomArgumentResolvers(pageableArgumentResolver)
//            .setControllerAdvice(exceptionTranslator)
//            .setConversionService(createFormattingConversionService())
//            .setMessageConverters(jacksonMessageConverter)
//            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ShopMain createEntity(EntityManager em) {
        ShopMain shopMain = new ShopMain()
            .shopId(DEFAULT_SHOP_ID)
            .userId(DEFAULT_USER_ID)
            .linkShop(DEFAULT_LINK_SHOP)
            .name(DEFAULT_NAME)
            .product(DEFAULT_PRODUCT)
            .follow(DEFAULT_FOLLOW)
            .following(DEFAULT_FOLLOWING)
            .rate(DEFAULT_RATE)
            .address(DEFAULT_ADDRESS)
            .version(DEFAULT_VERSION)
            .errorMsg(DEFAULT_ERROR_MSG)
            .error(DEFAULT_ERROR)
            .createDate(DEFAULT_CREATE_DATE)
            .updateDate(DEFAULT_UPDATE_DATE);
        return shopMain;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ShopMain createUpdatedEntity(EntityManager em) {
        ShopMain shopMain = new ShopMain()
            .shopId(UPDATED_SHOP_ID)
            .userId(UPDATED_USER_ID)
            .linkShop(UPDATED_LINK_SHOP)
            .name(UPDATED_NAME)
            .product(UPDATED_PRODUCT)
            .follow(UPDATED_FOLLOW)
            .following(UPDATED_FOLLOWING)
            .rate(UPDATED_RATE)
            .address(UPDATED_ADDRESS)
            .version(UPDATED_VERSION)
            .errorMsg(UPDATED_ERROR_MSG)
            .error(UPDATED_ERROR)
            .createDate(UPDATED_CREATE_DATE)
            .updateDate(UPDATED_UPDATE_DATE);
        return shopMain;
    }

    @BeforeEach
    public void initTest() {
        shopMain = createEntity(em);
    }

    @Test
    @Transactional
    public void createShopMain() throws Exception {
        int databaseSizeBeforeCreate = shopMainRepository.findAll().size();

        // Create the ShopMain
        restShopMainMockMvc.perform(post("/api/shop-mains")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(shopMain)))
            .andExpect(status().isCreated());

        // Validate the ShopMain in the database
        List<ShopMain> shopMainList = shopMainRepository.findAll();
        assertThat(shopMainList).hasSize(databaseSizeBeforeCreate + 1);
        ShopMain testShopMain = shopMainList.get(shopMainList.size() - 1);
        assertThat(testShopMain.getShopId()).isEqualTo(DEFAULT_SHOP_ID);
        assertThat(testShopMain.getUserId()).isEqualTo(DEFAULT_USER_ID);
        assertThat(testShopMain.getLinkShop()).isEqualTo(DEFAULT_LINK_SHOP);
        assertThat(testShopMain.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testShopMain.getProduct()).isEqualTo(DEFAULT_PRODUCT);
        assertThat(testShopMain.getFollow()).isEqualTo(DEFAULT_FOLLOW);
        assertThat(testShopMain.getFollowing()).isEqualTo(DEFAULT_FOLLOWING);
        assertThat(testShopMain.getRate()).isEqualTo(DEFAULT_RATE);
        assertThat(testShopMain.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testShopMain.getVersion()).isEqualTo(DEFAULT_VERSION);
        assertThat(testShopMain.getErrorMsg()).isEqualTo(DEFAULT_ERROR_MSG);
        assertThat(testShopMain.getError()).isEqualTo(DEFAULT_ERROR);
        assertThat(testShopMain.getCreateDate()).isEqualTo(DEFAULT_CREATE_DATE);
        assertThat(testShopMain.getUpdateDate()).isEqualTo(DEFAULT_UPDATE_DATE);
    }

    @Test
    @Transactional
    public void createShopMainWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = shopMainRepository.findAll().size();

        // Create the ShopMain with an existing ID
        shopMain.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restShopMainMockMvc.perform(post("/api/shop-mains")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(shopMain)))
            .andExpect(status().isBadRequest());

        // Validate the ShopMain in the database
        List<ShopMain> shopMainList = shopMainRepository.findAll();
        assertThat(shopMainList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllShopMains() throws Exception {
        // Initialize the database
        shopMainRepository.saveAndFlush(shopMain);

        // Get all the shopMainList
        restShopMainMockMvc.perform(get("/api/shop-mains?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(shopMain.getId().intValue())))
            .andExpect(jsonPath("$.[*].shopId").value(hasItem(DEFAULT_SHOP_ID)))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID)))
            .andExpect(jsonPath("$.[*].linkShop").value(hasItem(DEFAULT_LINK_SHOP)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].product").value(hasItem(DEFAULT_PRODUCT)))
            .andExpect(jsonPath("$.[*].follow").value(hasItem(DEFAULT_FOLLOW)))
            .andExpect(jsonPath("$.[*].following").value(hasItem(DEFAULT_FOLLOWING)))
            .andExpect(jsonPath("$.[*].rate").value(hasItem(DEFAULT_RATE)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].version").value(hasItem(DEFAULT_VERSION)))
            .andExpect(jsonPath("$.[*].errorMsg").value(hasItem(DEFAULT_ERROR_MSG)))
            .andExpect(jsonPath("$.[*].error").value(hasItem(DEFAULT_ERROR)))
            .andExpect(jsonPath("$.[*].createDate").value(hasItem(sameInstant(DEFAULT_CREATE_DATE))))
            .andExpect(jsonPath("$.[*].updateDate").value(hasItem(sameInstant(DEFAULT_UPDATE_DATE))));
    }
    
    @Test
    @Transactional
    public void getShopMain() throws Exception {
        // Initialize the database
        shopMainRepository.saveAndFlush(shopMain);

        // Get the shopMain
        restShopMainMockMvc.perform(get("/api/shop-mains/{id}", shopMain.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(shopMain.getId().intValue()))
            .andExpect(jsonPath("$.shopId").value(DEFAULT_SHOP_ID))
            .andExpect(jsonPath("$.userId").value(DEFAULT_USER_ID))
            .andExpect(jsonPath("$.linkShop").value(DEFAULT_LINK_SHOP))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.product").value(DEFAULT_PRODUCT))
            .andExpect(jsonPath("$.follow").value(DEFAULT_FOLLOW))
            .andExpect(jsonPath("$.following").value(DEFAULT_FOLLOWING))
            .andExpect(jsonPath("$.rate").value(DEFAULT_RATE))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS))
            .andExpect(jsonPath("$.version").value(DEFAULT_VERSION))
            .andExpect(jsonPath("$.errorMsg").value(DEFAULT_ERROR_MSG))
            .andExpect(jsonPath("$.error").value(DEFAULT_ERROR))
            .andExpect(jsonPath("$.createDate").value(sameInstant(DEFAULT_CREATE_DATE)))
            .andExpect(jsonPath("$.updateDate").value(sameInstant(DEFAULT_UPDATE_DATE)));
    }

    @Test
    @Transactional
    public void getNonExistingShopMain() throws Exception {
        // Get the shopMain
        restShopMainMockMvc.perform(get("/api/shop-mains/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateShopMain() throws Exception {
        // Initialize the database
        shopMainRepository.saveAndFlush(shopMain);

        int databaseSizeBeforeUpdate = shopMainRepository.findAll().size();

        // Update the shopMain
        ShopMain updatedShopMain = shopMainRepository.findById(shopMain.getId()).get();
        // Disconnect from session so that the updates on updatedShopMain are not directly saved in db
        em.detach(updatedShopMain);
        updatedShopMain
            .shopId(UPDATED_SHOP_ID)
            .userId(UPDATED_USER_ID)
            .linkShop(UPDATED_LINK_SHOP)
            .name(UPDATED_NAME)
            .product(UPDATED_PRODUCT)
            .follow(UPDATED_FOLLOW)
            .following(UPDATED_FOLLOWING)
            .rate(UPDATED_RATE)
            .address(UPDATED_ADDRESS)
            .version(UPDATED_VERSION)
            .errorMsg(UPDATED_ERROR_MSG)
            .error(UPDATED_ERROR)
            .createDate(UPDATED_CREATE_DATE)
            .updateDate(UPDATED_UPDATE_DATE);

        restShopMainMockMvc.perform(put("/api/shop-mains")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedShopMain)))
            .andExpect(status().isOk());

        // Validate the ShopMain in the database
        List<ShopMain> shopMainList = shopMainRepository.findAll();
        assertThat(shopMainList).hasSize(databaseSizeBeforeUpdate);
        ShopMain testShopMain = shopMainList.get(shopMainList.size() - 1);
        assertThat(testShopMain.getShopId()).isEqualTo(UPDATED_SHOP_ID);
        assertThat(testShopMain.getUserId()).isEqualTo(UPDATED_USER_ID);
        assertThat(testShopMain.getLinkShop()).isEqualTo(UPDATED_LINK_SHOP);
        assertThat(testShopMain.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testShopMain.getProduct()).isEqualTo(UPDATED_PRODUCT);
        assertThat(testShopMain.getFollow()).isEqualTo(UPDATED_FOLLOW);
        assertThat(testShopMain.getFollowing()).isEqualTo(UPDATED_FOLLOWING);
        assertThat(testShopMain.getRate()).isEqualTo(UPDATED_RATE);
        assertThat(testShopMain.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testShopMain.getVersion()).isEqualTo(UPDATED_VERSION);
        assertThat(testShopMain.getErrorMsg()).isEqualTo(UPDATED_ERROR_MSG);
        assertThat(testShopMain.getError()).isEqualTo(UPDATED_ERROR);
        assertThat(testShopMain.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testShopMain.getUpdateDate()).isEqualTo(UPDATED_UPDATE_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingShopMain() throws Exception {
        int databaseSizeBeforeUpdate = shopMainRepository.findAll().size();

        // Create the ShopMain

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restShopMainMockMvc.perform(put("/api/shop-mains")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(shopMain)))
            .andExpect(status().isBadRequest());

        // Validate the ShopMain in the database
        List<ShopMain> shopMainList = shopMainRepository.findAll();
        assertThat(shopMainList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteShopMain() throws Exception {
        // Initialize the database
        shopMainRepository.saveAndFlush(shopMain);

        int databaseSizeBeforeDelete = shopMainRepository.findAll().size();

        // Delete the shopMain
        restShopMainMockMvc.perform(delete("/api/shop-mains/{id}", shopMain.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ShopMain> shopMainList = shopMainRepository.findAll();
        assertThat(shopMainList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ShopMain.class);
        ShopMain shopMain1 = new ShopMain();
        shopMain1.setId(1L);
        ShopMain shopMain2 = new ShopMain();
        shopMain2.setId(shopMain1.getId());
        assertThat(shopMain1).isEqualTo(shopMain2);
        shopMain2.setId(2L);
        assertThat(shopMain1).isNotEqualTo(shopMain2);
        shopMain1.setId(null);
        assertThat(shopMain1).isNotEqualTo(shopMain2);
    }
}
