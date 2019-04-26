package com.openwarehouse.openwarehousemanagement.web.rest;

import com.openwarehouse.openwarehousemanagement.OpenWarehouseManagementApp;

import com.openwarehouse.openwarehousemanagement.domain.Item;
import com.openwarehouse.openwarehousemanagement.domain.OutOrder;
import com.openwarehouse.openwarehousemanagement.domain.InOrder;
import com.openwarehouse.openwarehousemanagement.repository.ItemRepository;
import com.openwarehouse.openwarehousemanagement.service.ItemService;
import com.openwarehouse.openwarehousemanagement.web.rest.errors.ExceptionTranslator;
import com.openwarehouse.openwarehousemanagement.service.dto.ItemCriteria;
import com.openwarehouse.openwarehousemanagement.service.ItemQueryService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.List;


import static com.openwarehouse.openwarehousemanagement.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ItemResource REST controller.
 *
 * @see ItemResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = OpenWarehouseManagementApp.class)
public class ItemResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final Double DEFAULT_AVAILABLE_QUANTITY = 1D;
    private static final Double UPDATED_AVAILABLE_QUANTITY = 2D;

    private static final Double DEFAULT_MINIMUM_QUANTITY = 1D;
    private static final Double UPDATED_MINIMUM_QUANTITY = 2D;

    private static final Double DEFAULT_PRICE = 1D;
    private static final Double UPDATED_PRICE = 2D;

    private static final String DEFAULT_SPECIFICATION = "AAAAAAAAAA";
    private static final String UPDATED_SPECIFICATION = "BBBBBBBBBB";

    private static final String DEFAULT_SUPPLIER = "AAAAAAAAAA";
    private static final String UPDATED_SUPPLIER = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private ItemService itemService;

    @Autowired
    private ItemQueryService itemQueryService;

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

    private MockMvc restItemMockMvc;

    private Item item;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ItemResource itemResource = new ItemResource(itemService, itemQueryService);
        this.restItemMockMvc = MockMvcBuilders.standaloneSetup(itemResource)
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
    public static Item createEntity(EntityManager em) {
        Item item = new Item()
            .name(DEFAULT_NAME)
            .type(DEFAULT_TYPE)
            .availableQuantity(DEFAULT_AVAILABLE_QUANTITY)
            .minimumQuantity(DEFAULT_MINIMUM_QUANTITY)
            .price(DEFAULT_PRICE)
            .specification(DEFAULT_SPECIFICATION)
            .supplier(DEFAULT_SUPPLIER)
            .description(DEFAULT_DESCRIPTION);
        return item;
    }

    @Before
    public void initTest() {
        item = createEntity(em);
    }

    @Test
    @Transactional
    public void createItem() throws Exception {
        int databaseSizeBeforeCreate = itemRepository.findAll().size();

        // Create the Item
        restItemMockMvc.perform(post("/api/items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(item)))
            .andExpect(status().isCreated());

        // Validate the Item in the database
        List<Item> itemList = itemRepository.findAll();
        assertThat(itemList).hasSize(databaseSizeBeforeCreate + 1);
        Item testItem = itemList.get(itemList.size() - 1);
        assertThat(testItem.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testItem.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testItem.getAvailableQuantity()).isEqualTo(DEFAULT_AVAILABLE_QUANTITY);
        assertThat(testItem.getMinimumQuantity()).isEqualTo(DEFAULT_MINIMUM_QUANTITY);
        assertThat(testItem.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testItem.getSpecification()).isEqualTo(DEFAULT_SPECIFICATION);
        assertThat(testItem.getSupplier()).isEqualTo(DEFAULT_SUPPLIER);
        assertThat(testItem.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void createItemWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = itemRepository.findAll().size();

        // Create the Item with an existing ID
        item.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restItemMockMvc.perform(post("/api/items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(item)))
            .andExpect(status().isBadRequest());

        // Validate the Item in the database
        List<Item> itemList = itemRepository.findAll();
        assertThat(itemList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = itemRepository.findAll().size();
        // set the field null
        item.setName(null);

        // Create the Item, which fails.

        restItemMockMvc.perform(post("/api/items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(item)))
            .andExpect(status().isBadRequest());

        List<Item> itemList = itemRepository.findAll();
        assertThat(itemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = itemRepository.findAll().size();
        // set the field null
        item.setType(null);

        // Create the Item, which fails.

        restItemMockMvc.perform(post("/api/items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(item)))
            .andExpect(status().isBadRequest());

        List<Item> itemList = itemRepository.findAll();
        assertThat(itemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAvailableQuantityIsRequired() throws Exception {
        int databaseSizeBeforeTest = itemRepository.findAll().size();
        // set the field null
        item.setAvailableQuantity(null);

        // Create the Item, which fails.

        restItemMockMvc.perform(post("/api/items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(item)))
            .andExpect(status().isBadRequest());

        List<Item> itemList = itemRepository.findAll();
        assertThat(itemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkMinimumQuantityIsRequired() throws Exception {
        int databaseSizeBeforeTest = itemRepository.findAll().size();
        // set the field null
        item.setMinimumQuantity(null);

        // Create the Item, which fails.

        restItemMockMvc.perform(post("/api/items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(item)))
            .andExpect(status().isBadRequest());

        List<Item> itemList = itemRepository.findAll();
        assertThat(itemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPriceIsRequired() throws Exception {
        int databaseSizeBeforeTest = itemRepository.findAll().size();
        // set the field null
        item.setPrice(null);

        // Create the Item, which fails.

        restItemMockMvc.perform(post("/api/items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(item)))
            .andExpect(status().isBadRequest());

        List<Item> itemList = itemRepository.findAll();
        assertThat(itemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllItems() throws Exception {
        // Initialize the database
        itemRepository.saveAndFlush(item);

        // Get all the itemList
        restItemMockMvc.perform(get("/api/items?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(item.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].availableQuantity").value(hasItem(DEFAULT_AVAILABLE_QUANTITY.doubleValue())))
            .andExpect(jsonPath("$.[*].minimumQuantity").value(hasItem(DEFAULT_MINIMUM_QUANTITY.doubleValue())))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].specification").value(hasItem(DEFAULT_SPECIFICATION.toString())))
            .andExpect(jsonPath("$.[*].supplier").value(hasItem(DEFAULT_SUPPLIER.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }
    
    @Test
    @Transactional
    public void getItem() throws Exception {
        // Initialize the database
        itemRepository.saveAndFlush(item);

        // Get the item
        restItemMockMvc.perform(get("/api/items/{id}", item.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(item.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.availableQuantity").value(DEFAULT_AVAILABLE_QUANTITY.doubleValue()))
            .andExpect(jsonPath("$.minimumQuantity").value(DEFAULT_MINIMUM_QUANTITY.doubleValue()))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.doubleValue()))
            .andExpect(jsonPath("$.specification").value(DEFAULT_SPECIFICATION.toString()))
            .andExpect(jsonPath("$.supplier").value(DEFAULT_SUPPLIER.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getAllItemsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        itemRepository.saveAndFlush(item);

        // Get all the itemList where name equals to DEFAULT_NAME
        defaultItemShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the itemList where name equals to UPDATED_NAME
        defaultItemShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllItemsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        itemRepository.saveAndFlush(item);

        // Get all the itemList where name in DEFAULT_NAME or UPDATED_NAME
        defaultItemShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the itemList where name equals to UPDATED_NAME
        defaultItemShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllItemsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        itemRepository.saveAndFlush(item);

        // Get all the itemList where name is not null
        defaultItemShouldBeFound("name.specified=true");

        // Get all the itemList where name is null
        defaultItemShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    public void getAllItemsByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        itemRepository.saveAndFlush(item);

        // Get all the itemList where type equals to DEFAULT_TYPE
        defaultItemShouldBeFound("type.equals=" + DEFAULT_TYPE);

        // Get all the itemList where type equals to UPDATED_TYPE
        defaultItemShouldNotBeFound("type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllItemsByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        itemRepository.saveAndFlush(item);

        // Get all the itemList where type in DEFAULT_TYPE or UPDATED_TYPE
        defaultItemShouldBeFound("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE);

        // Get all the itemList where type equals to UPDATED_TYPE
        defaultItemShouldNotBeFound("type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllItemsByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        itemRepository.saveAndFlush(item);

        // Get all the itemList where type is not null
        defaultItemShouldBeFound("type.specified=true");

        // Get all the itemList where type is null
        defaultItemShouldNotBeFound("type.specified=false");
    }

    @Test
    @Transactional
    public void getAllItemsByAvailableQuantityIsEqualToSomething() throws Exception {
        // Initialize the database
        itemRepository.saveAndFlush(item);

        // Get all the itemList where availableQuantity equals to DEFAULT_AVAILABLE_QUANTITY
        defaultItemShouldBeFound("availableQuantity.equals=" + DEFAULT_AVAILABLE_QUANTITY);

        // Get all the itemList where availableQuantity equals to UPDATED_AVAILABLE_QUANTITY
        defaultItemShouldNotBeFound("availableQuantity.equals=" + UPDATED_AVAILABLE_QUANTITY);
    }

    @Test
    @Transactional
    public void getAllItemsByAvailableQuantityIsInShouldWork() throws Exception {
        // Initialize the database
        itemRepository.saveAndFlush(item);

        // Get all the itemList where availableQuantity in DEFAULT_AVAILABLE_QUANTITY or UPDATED_AVAILABLE_QUANTITY
        defaultItemShouldBeFound("availableQuantity.in=" + DEFAULT_AVAILABLE_QUANTITY + "," + UPDATED_AVAILABLE_QUANTITY);

        // Get all the itemList where availableQuantity equals to UPDATED_AVAILABLE_QUANTITY
        defaultItemShouldNotBeFound("availableQuantity.in=" + UPDATED_AVAILABLE_QUANTITY);
    }

    @Test
    @Transactional
    public void getAllItemsByAvailableQuantityIsNullOrNotNull() throws Exception {
        // Initialize the database
        itemRepository.saveAndFlush(item);

        // Get all the itemList where availableQuantity is not null
        defaultItemShouldBeFound("availableQuantity.specified=true");

        // Get all the itemList where availableQuantity is null
        defaultItemShouldNotBeFound("availableQuantity.specified=false");
    }

    @Test
    @Transactional
    public void getAllItemsByMinimumQuantityIsEqualToSomething() throws Exception {
        // Initialize the database
        itemRepository.saveAndFlush(item);

        // Get all the itemList where minimumQuantity equals to DEFAULT_MINIMUM_QUANTITY
        defaultItemShouldBeFound("minimumQuantity.equals=" + DEFAULT_MINIMUM_QUANTITY);

        // Get all the itemList where minimumQuantity equals to UPDATED_MINIMUM_QUANTITY
        defaultItemShouldNotBeFound("minimumQuantity.equals=" + UPDATED_MINIMUM_QUANTITY);
    }

    @Test
    @Transactional
    public void getAllItemsByMinimumQuantityIsInShouldWork() throws Exception {
        // Initialize the database
        itemRepository.saveAndFlush(item);

        // Get all the itemList where minimumQuantity in DEFAULT_MINIMUM_QUANTITY or UPDATED_MINIMUM_QUANTITY
        defaultItemShouldBeFound("minimumQuantity.in=" + DEFAULT_MINIMUM_QUANTITY + "," + UPDATED_MINIMUM_QUANTITY);

        // Get all the itemList where minimumQuantity equals to UPDATED_MINIMUM_QUANTITY
        defaultItemShouldNotBeFound("minimumQuantity.in=" + UPDATED_MINIMUM_QUANTITY);
    }

    @Test
    @Transactional
    public void getAllItemsByMinimumQuantityIsNullOrNotNull() throws Exception {
        // Initialize the database
        itemRepository.saveAndFlush(item);

        // Get all the itemList where minimumQuantity is not null
        defaultItemShouldBeFound("minimumQuantity.specified=true");

        // Get all the itemList where minimumQuantity is null
        defaultItemShouldNotBeFound("minimumQuantity.specified=false");
    }

    @Test
    @Transactional
    public void getAllItemsByPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        itemRepository.saveAndFlush(item);

        // Get all the itemList where price equals to DEFAULT_PRICE
        defaultItemShouldBeFound("price.equals=" + DEFAULT_PRICE);

        // Get all the itemList where price equals to UPDATED_PRICE
        defaultItemShouldNotBeFound("price.equals=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    public void getAllItemsByPriceIsInShouldWork() throws Exception {
        // Initialize the database
        itemRepository.saveAndFlush(item);

        // Get all the itemList where price in DEFAULT_PRICE or UPDATED_PRICE
        defaultItemShouldBeFound("price.in=" + DEFAULT_PRICE + "," + UPDATED_PRICE);

        // Get all the itemList where price equals to UPDATED_PRICE
        defaultItemShouldNotBeFound("price.in=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    public void getAllItemsByPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        itemRepository.saveAndFlush(item);

        // Get all the itemList where price is not null
        defaultItemShouldBeFound("price.specified=true");

        // Get all the itemList where price is null
        defaultItemShouldNotBeFound("price.specified=false");
    }

    @Test
    @Transactional
    public void getAllItemsBySpecificationIsEqualToSomething() throws Exception {
        // Initialize the database
        itemRepository.saveAndFlush(item);

        // Get all the itemList where specification equals to DEFAULT_SPECIFICATION
        defaultItemShouldBeFound("specification.equals=" + DEFAULT_SPECIFICATION);

        // Get all the itemList where specification equals to UPDATED_SPECIFICATION
        defaultItemShouldNotBeFound("specification.equals=" + UPDATED_SPECIFICATION);
    }

    @Test
    @Transactional
    public void getAllItemsBySpecificationIsInShouldWork() throws Exception {
        // Initialize the database
        itemRepository.saveAndFlush(item);

        // Get all the itemList where specification in DEFAULT_SPECIFICATION or UPDATED_SPECIFICATION
        defaultItemShouldBeFound("specification.in=" + DEFAULT_SPECIFICATION + "," + UPDATED_SPECIFICATION);

        // Get all the itemList where specification equals to UPDATED_SPECIFICATION
        defaultItemShouldNotBeFound("specification.in=" + UPDATED_SPECIFICATION);
    }

    @Test
    @Transactional
    public void getAllItemsBySpecificationIsNullOrNotNull() throws Exception {
        // Initialize the database
        itemRepository.saveAndFlush(item);

        // Get all the itemList where specification is not null
        defaultItemShouldBeFound("specification.specified=true");

        // Get all the itemList where specification is null
        defaultItemShouldNotBeFound("specification.specified=false");
    }

    @Test
    @Transactional
    public void getAllItemsBySupplierIsEqualToSomething() throws Exception {
        // Initialize the database
        itemRepository.saveAndFlush(item);

        // Get all the itemList where supplier equals to DEFAULT_SUPPLIER
        defaultItemShouldBeFound("supplier.equals=" + DEFAULT_SUPPLIER);

        // Get all the itemList where supplier equals to UPDATED_SUPPLIER
        defaultItemShouldNotBeFound("supplier.equals=" + UPDATED_SUPPLIER);
    }

    @Test
    @Transactional
    public void getAllItemsBySupplierIsInShouldWork() throws Exception {
        // Initialize the database
        itemRepository.saveAndFlush(item);

        // Get all the itemList where supplier in DEFAULT_SUPPLIER or UPDATED_SUPPLIER
        defaultItemShouldBeFound("supplier.in=" + DEFAULT_SUPPLIER + "," + UPDATED_SUPPLIER);

        // Get all the itemList where supplier equals to UPDATED_SUPPLIER
        defaultItemShouldNotBeFound("supplier.in=" + UPDATED_SUPPLIER);
    }

    @Test
    @Transactional
    public void getAllItemsBySupplierIsNullOrNotNull() throws Exception {
        // Initialize the database
        itemRepository.saveAndFlush(item);

        // Get all the itemList where supplier is not null
        defaultItemShouldBeFound("supplier.specified=true");

        // Get all the itemList where supplier is null
        defaultItemShouldNotBeFound("supplier.specified=false");
    }

    @Test
    @Transactional
    public void getAllItemsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        itemRepository.saveAndFlush(item);

        // Get all the itemList where description equals to DEFAULT_DESCRIPTION
        defaultItemShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the itemList where description equals to UPDATED_DESCRIPTION
        defaultItemShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllItemsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        itemRepository.saveAndFlush(item);

        // Get all the itemList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultItemShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the itemList where description equals to UPDATED_DESCRIPTION
        defaultItemShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllItemsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        itemRepository.saveAndFlush(item);

        // Get all the itemList where description is not null
        defaultItemShouldBeFound("description.specified=true");

        // Get all the itemList where description is null
        defaultItemShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    public void getAllItemsByOutOrdersIsEqualToSomething() throws Exception {
        // Initialize the database
        OutOrder outOrders = OutOrderResourceIntTest.createEntity(em);
        em.persist(outOrders);
        em.flush();
        item.addOutOrders(outOrders);
        itemRepository.saveAndFlush(item);
        Long outOrdersId = outOrders.getId();

        // Get all the itemList where outOrders equals to outOrdersId
        defaultItemShouldBeFound("outOrdersId.equals=" + outOrdersId);

        // Get all the itemList where outOrders equals to outOrdersId + 1
        defaultItemShouldNotBeFound("outOrdersId.equals=" + (outOrdersId + 1));
    }


    @Test
    @Transactional
    public void getAllItemsByInOrdersIsEqualToSomething() throws Exception {
        // Initialize the database
        InOrder inOrders = InOrderResourceIntTest.createEntity(em);
        em.persist(inOrders);
        em.flush();
        item.addInOrders(inOrders);
        itemRepository.saveAndFlush(item);
        Long inOrdersId = inOrders.getId();

        // Get all the itemList where inOrders equals to inOrdersId
        defaultItemShouldBeFound("inOrdersId.equals=" + inOrdersId);

        // Get all the itemList where inOrders equals to inOrdersId + 1
        defaultItemShouldNotBeFound("inOrdersId.equals=" + (inOrdersId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultItemShouldBeFound(String filter) throws Exception {
        restItemMockMvc.perform(get("/api/items?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(item.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].availableQuantity").value(hasItem(DEFAULT_AVAILABLE_QUANTITY.doubleValue())))
            .andExpect(jsonPath("$.[*].minimumQuantity").value(hasItem(DEFAULT_MINIMUM_QUANTITY.doubleValue())))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].specification").value(hasItem(DEFAULT_SPECIFICATION)))
            .andExpect(jsonPath("$.[*].supplier").value(hasItem(DEFAULT_SUPPLIER)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));

        // Check, that the count call also returns 1
        restItemMockMvc.perform(get("/api/items/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultItemShouldNotBeFound(String filter) throws Exception {
        restItemMockMvc.perform(get("/api/items?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restItemMockMvc.perform(get("/api/items/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingItem() throws Exception {
        // Get the item
        restItemMockMvc.perform(get("/api/items/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateItem() throws Exception {
        // Initialize the database
        itemService.save(item);

        int databaseSizeBeforeUpdate = itemRepository.findAll().size();

        // Update the item
        Item updatedItem = itemRepository.findById(item.getId()).get();
        // Disconnect from session so that the updates on updatedItem are not directly saved in db
        em.detach(updatedItem);
        updatedItem
            .name(UPDATED_NAME)
            .type(UPDATED_TYPE)
            .availableQuantity(UPDATED_AVAILABLE_QUANTITY)
            .minimumQuantity(UPDATED_MINIMUM_QUANTITY)
            .price(UPDATED_PRICE)
            .specification(UPDATED_SPECIFICATION)
            .supplier(UPDATED_SUPPLIER)
            .description(UPDATED_DESCRIPTION);

        restItemMockMvc.perform(put("/api/items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedItem)))
            .andExpect(status().isOk());

        // Validate the Item in the database
        List<Item> itemList = itemRepository.findAll();
        assertThat(itemList).hasSize(databaseSizeBeforeUpdate);
        Item testItem = itemList.get(itemList.size() - 1);
        assertThat(testItem.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testItem.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testItem.getAvailableQuantity()).isEqualTo(UPDATED_AVAILABLE_QUANTITY);
        assertThat(testItem.getMinimumQuantity()).isEqualTo(UPDATED_MINIMUM_QUANTITY);
        assertThat(testItem.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testItem.getSpecification()).isEqualTo(UPDATED_SPECIFICATION);
        assertThat(testItem.getSupplier()).isEqualTo(UPDATED_SUPPLIER);
        assertThat(testItem.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void updateNonExistingItem() throws Exception {
        int databaseSizeBeforeUpdate = itemRepository.findAll().size();

        // Create the Item

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restItemMockMvc.perform(put("/api/items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(item)))
            .andExpect(status().isBadRequest());

        // Validate the Item in the database
        List<Item> itemList = itemRepository.findAll();
        assertThat(itemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteItem() throws Exception {
        // Initialize the database
        itemService.save(item);

        int databaseSizeBeforeDelete = itemRepository.findAll().size();

        // Delete the item
        restItemMockMvc.perform(delete("/api/items/{id}", item.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Item> itemList = itemRepository.findAll();
        assertThat(itemList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Item.class);
        Item item1 = new Item();
        item1.setId(1L);
        Item item2 = new Item();
        item2.setId(item1.getId());
        assertThat(item1).isEqualTo(item2);
        item2.setId(2L);
        assertThat(item1).isNotEqualTo(item2);
        item1.setId(null);
        assertThat(item1).isNotEqualTo(item2);
    }
}
