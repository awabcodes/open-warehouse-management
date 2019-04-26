package com.openwarehouse.openwarehousemanagement.web.rest;

import com.openwarehouse.openwarehousemanagement.OpenWarehouseManagementApp;

import com.openwarehouse.openwarehousemanagement.domain.InOrder;
import com.openwarehouse.openwarehousemanagement.domain.Item;
import com.openwarehouse.openwarehousemanagement.repository.InOrderRepository;
import com.openwarehouse.openwarehousemanagement.service.InOrderService;
import com.openwarehouse.openwarehousemanagement.web.rest.errors.ExceptionTranslator;
import com.openwarehouse.openwarehousemanagement.service.dto.InOrderCriteria;
import com.openwarehouse.openwarehousemanagement.service.InOrderQueryService;

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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;


import static com.openwarehouse.openwarehousemanagement.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the InOrderResource REST controller.
 *
 * @see InOrderResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = OpenWarehouseManagementApp.class)
public class InOrderResourceIntTest {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_INFORMATION = "AAAAAAAAAA";
    private static final String UPDATED_INFORMATION = "BBBBBBBBBB";

    private static final Double DEFAULT_ORDER_QUANTITY = 1D;
    private static final Double UPDATED_ORDER_QUANTITY = 2D;

    private static final Boolean DEFAULT_DELIVERED = false;
    private static final Boolean UPDATED_DELIVERED = true;

    private static final LocalDate DEFAULT_ORDER_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_ORDER_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DELIVERY_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DELIVERY_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Boolean DEFAULT_AUTHORIZED = false;
    private static final Boolean UPDATED_AUTHORIZED = true;

    @Autowired
    private InOrderRepository inOrderRepository;

    @Autowired
    private InOrderService inOrderService;

    @Autowired
    private InOrderQueryService inOrderQueryService;

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

    private MockMvc restInOrderMockMvc;

    private InOrder inOrder;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final InOrderResource inOrderResource = new InOrderResource(inOrderService, inOrderQueryService);
        this.restInOrderMockMvc = MockMvcBuilders.standaloneSetup(inOrderResource)
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
    public static InOrder createEntity(EntityManager em) {
        InOrder inOrder = new InOrder()
            .title(DEFAULT_TITLE)
            .information(DEFAULT_INFORMATION)
            .orderQuantity(DEFAULT_ORDER_QUANTITY)
            .delivered(DEFAULT_DELIVERED)
            .orderDate(DEFAULT_ORDER_DATE)
            .deliveryDate(DEFAULT_DELIVERY_DATE)
            .authorized(DEFAULT_AUTHORIZED);
        return inOrder;
    }

    @Before
    public void initTest() {
        inOrder = createEntity(em);
    }

    @Test
    @Transactional
    public void createInOrder() throws Exception {
        int databaseSizeBeforeCreate = inOrderRepository.findAll().size();

        // Create the InOrder
        restInOrderMockMvc.perform(post("/api/in-orders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(inOrder)))
            .andExpect(status().isCreated());

        // Validate the InOrder in the database
        List<InOrder> inOrderList = inOrderRepository.findAll();
        assertThat(inOrderList).hasSize(databaseSizeBeforeCreate + 1);
        InOrder testInOrder = inOrderList.get(inOrderList.size() - 1);
        assertThat(testInOrder.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testInOrder.getInformation()).isEqualTo(DEFAULT_INFORMATION);
        assertThat(testInOrder.getOrderQuantity()).isEqualTo(DEFAULT_ORDER_QUANTITY);
        assertThat(testInOrder.isDelivered()).isEqualTo(DEFAULT_DELIVERED);
        assertThat(testInOrder.getOrderDate()).isEqualTo(DEFAULT_ORDER_DATE);
        assertThat(testInOrder.getDeliveryDate()).isEqualTo(DEFAULT_DELIVERY_DATE);
        assertThat(testInOrder.isAuthorized()).isEqualTo(DEFAULT_AUTHORIZED);
    }

    @Test
    @Transactional
    public void createInOrderWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = inOrderRepository.findAll().size();

        // Create the InOrder with an existing ID
        inOrder.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restInOrderMockMvc.perform(post("/api/in-orders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(inOrder)))
            .andExpect(status().isBadRequest());

        // Validate the InOrder in the database
        List<InOrder> inOrderList = inOrderRepository.findAll();
        assertThat(inOrderList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = inOrderRepository.findAll().size();
        // set the field null
        inOrder.setTitle(null);

        // Create the InOrder, which fails.

        restInOrderMockMvc.perform(post("/api/in-orders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(inOrder)))
            .andExpect(status().isBadRequest());

        List<InOrder> inOrderList = inOrderRepository.findAll();
        assertThat(inOrderList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkInformationIsRequired() throws Exception {
        int databaseSizeBeforeTest = inOrderRepository.findAll().size();
        // set the field null
        inOrder.setInformation(null);

        // Create the InOrder, which fails.

        restInOrderMockMvc.perform(post("/api/in-orders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(inOrder)))
            .andExpect(status().isBadRequest());

        List<InOrder> inOrderList = inOrderRepository.findAll();
        assertThat(inOrderList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkOrderQuantityIsRequired() throws Exception {
        int databaseSizeBeforeTest = inOrderRepository.findAll().size();
        // set the field null
        inOrder.setOrderQuantity(null);

        // Create the InOrder, which fails.

        restInOrderMockMvc.perform(post("/api/in-orders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(inOrder)))
            .andExpect(status().isBadRequest());

        List<InOrder> inOrderList = inOrderRepository.findAll();
        assertThat(inOrderList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllInOrders() throws Exception {
        // Initialize the database
        inOrderRepository.saveAndFlush(inOrder);

        // Get all the inOrderList
        restInOrderMockMvc.perform(get("/api/in-orders?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(inOrder.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
            .andExpect(jsonPath("$.[*].information").value(hasItem(DEFAULT_INFORMATION.toString())))
            .andExpect(jsonPath("$.[*].orderQuantity").value(hasItem(DEFAULT_ORDER_QUANTITY.doubleValue())))
            .andExpect(jsonPath("$.[*].delivered").value(hasItem(DEFAULT_DELIVERED.booleanValue())))
            .andExpect(jsonPath("$.[*].orderDate").value(hasItem(DEFAULT_ORDER_DATE.toString())))
            .andExpect(jsonPath("$.[*].deliveryDate").value(hasItem(DEFAULT_DELIVERY_DATE.toString())))
            .andExpect(jsonPath("$.[*].authorized").value(hasItem(DEFAULT_AUTHORIZED.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getInOrder() throws Exception {
        // Initialize the database
        inOrderRepository.saveAndFlush(inOrder);

        // Get the inOrder
        restInOrderMockMvc.perform(get("/api/in-orders/{id}", inOrder.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(inOrder.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()))
            .andExpect(jsonPath("$.information").value(DEFAULT_INFORMATION.toString()))
            .andExpect(jsonPath("$.orderQuantity").value(DEFAULT_ORDER_QUANTITY.doubleValue()))
            .andExpect(jsonPath("$.delivered").value(DEFAULT_DELIVERED.booleanValue()))
            .andExpect(jsonPath("$.orderDate").value(DEFAULT_ORDER_DATE.toString()))
            .andExpect(jsonPath("$.deliveryDate").value(DEFAULT_DELIVERY_DATE.toString()))
            .andExpect(jsonPath("$.authorized").value(DEFAULT_AUTHORIZED.booleanValue()));
    }

    @Test
    @Transactional
    public void getAllInOrdersByTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        inOrderRepository.saveAndFlush(inOrder);

        // Get all the inOrderList where title equals to DEFAULT_TITLE
        defaultInOrderShouldBeFound("title.equals=" + DEFAULT_TITLE);

        // Get all the inOrderList where title equals to UPDATED_TITLE
        defaultInOrderShouldNotBeFound("title.equals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllInOrdersByTitleIsInShouldWork() throws Exception {
        // Initialize the database
        inOrderRepository.saveAndFlush(inOrder);

        // Get all the inOrderList where title in DEFAULT_TITLE or UPDATED_TITLE
        defaultInOrderShouldBeFound("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE);

        // Get all the inOrderList where title equals to UPDATED_TITLE
        defaultInOrderShouldNotBeFound("title.in=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllInOrdersByTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        inOrderRepository.saveAndFlush(inOrder);

        // Get all the inOrderList where title is not null
        defaultInOrderShouldBeFound("title.specified=true");

        // Get all the inOrderList where title is null
        defaultInOrderShouldNotBeFound("title.specified=false");
    }

    @Test
    @Transactional
    public void getAllInOrdersByInformationIsEqualToSomething() throws Exception {
        // Initialize the database
        inOrderRepository.saveAndFlush(inOrder);

        // Get all the inOrderList where information equals to DEFAULT_INFORMATION
        defaultInOrderShouldBeFound("information.equals=" + DEFAULT_INFORMATION);

        // Get all the inOrderList where information equals to UPDATED_INFORMATION
        defaultInOrderShouldNotBeFound("information.equals=" + UPDATED_INFORMATION);
    }

    @Test
    @Transactional
    public void getAllInOrdersByInformationIsInShouldWork() throws Exception {
        // Initialize the database
        inOrderRepository.saveAndFlush(inOrder);

        // Get all the inOrderList where information in DEFAULT_INFORMATION or UPDATED_INFORMATION
        defaultInOrderShouldBeFound("information.in=" + DEFAULT_INFORMATION + "," + UPDATED_INFORMATION);

        // Get all the inOrderList where information equals to UPDATED_INFORMATION
        defaultInOrderShouldNotBeFound("information.in=" + UPDATED_INFORMATION);
    }

    @Test
    @Transactional
    public void getAllInOrdersByInformationIsNullOrNotNull() throws Exception {
        // Initialize the database
        inOrderRepository.saveAndFlush(inOrder);

        // Get all the inOrderList where information is not null
        defaultInOrderShouldBeFound("information.specified=true");

        // Get all the inOrderList where information is null
        defaultInOrderShouldNotBeFound("information.specified=false");
    }

    @Test
    @Transactional
    public void getAllInOrdersByOrderQuantityIsEqualToSomething() throws Exception {
        // Initialize the database
        inOrderRepository.saveAndFlush(inOrder);

        // Get all the inOrderList where orderQuantity equals to DEFAULT_ORDER_QUANTITY
        defaultInOrderShouldBeFound("orderQuantity.equals=" + DEFAULT_ORDER_QUANTITY);

        // Get all the inOrderList where orderQuantity equals to UPDATED_ORDER_QUANTITY
        defaultInOrderShouldNotBeFound("orderQuantity.equals=" + UPDATED_ORDER_QUANTITY);
    }

    @Test
    @Transactional
    public void getAllInOrdersByOrderQuantityIsInShouldWork() throws Exception {
        // Initialize the database
        inOrderRepository.saveAndFlush(inOrder);

        // Get all the inOrderList where orderQuantity in DEFAULT_ORDER_QUANTITY or UPDATED_ORDER_QUANTITY
        defaultInOrderShouldBeFound("orderQuantity.in=" + DEFAULT_ORDER_QUANTITY + "," + UPDATED_ORDER_QUANTITY);

        // Get all the inOrderList where orderQuantity equals to UPDATED_ORDER_QUANTITY
        defaultInOrderShouldNotBeFound("orderQuantity.in=" + UPDATED_ORDER_QUANTITY);
    }

    @Test
    @Transactional
    public void getAllInOrdersByOrderQuantityIsNullOrNotNull() throws Exception {
        // Initialize the database
        inOrderRepository.saveAndFlush(inOrder);

        // Get all the inOrderList where orderQuantity is not null
        defaultInOrderShouldBeFound("orderQuantity.specified=true");

        // Get all the inOrderList where orderQuantity is null
        defaultInOrderShouldNotBeFound("orderQuantity.specified=false");
    }

    @Test
    @Transactional
    public void getAllInOrdersByDeliveredIsEqualToSomething() throws Exception {
        // Initialize the database
        inOrderRepository.saveAndFlush(inOrder);

        // Get all the inOrderList where delivered equals to DEFAULT_DELIVERED
        defaultInOrderShouldBeFound("delivered.equals=" + DEFAULT_DELIVERED);

        // Get all the inOrderList where delivered equals to UPDATED_DELIVERED
        defaultInOrderShouldNotBeFound("delivered.equals=" + UPDATED_DELIVERED);
    }

    @Test
    @Transactional
    public void getAllInOrdersByDeliveredIsInShouldWork() throws Exception {
        // Initialize the database
        inOrderRepository.saveAndFlush(inOrder);

        // Get all the inOrderList where delivered in DEFAULT_DELIVERED or UPDATED_DELIVERED
        defaultInOrderShouldBeFound("delivered.in=" + DEFAULT_DELIVERED + "," + UPDATED_DELIVERED);

        // Get all the inOrderList where delivered equals to UPDATED_DELIVERED
        defaultInOrderShouldNotBeFound("delivered.in=" + UPDATED_DELIVERED);
    }

    @Test
    @Transactional
    public void getAllInOrdersByDeliveredIsNullOrNotNull() throws Exception {
        // Initialize the database
        inOrderRepository.saveAndFlush(inOrder);

        // Get all the inOrderList where delivered is not null
        defaultInOrderShouldBeFound("delivered.specified=true");

        // Get all the inOrderList where delivered is null
        defaultInOrderShouldNotBeFound("delivered.specified=false");
    }

    @Test
    @Transactional
    public void getAllInOrdersByOrderDateIsEqualToSomething() throws Exception {
        // Initialize the database
        inOrderRepository.saveAndFlush(inOrder);

        // Get all the inOrderList where orderDate equals to DEFAULT_ORDER_DATE
        defaultInOrderShouldBeFound("orderDate.equals=" + DEFAULT_ORDER_DATE);

        // Get all the inOrderList where orderDate equals to UPDATED_ORDER_DATE
        defaultInOrderShouldNotBeFound("orderDate.equals=" + UPDATED_ORDER_DATE);
    }

    @Test
    @Transactional
    public void getAllInOrdersByOrderDateIsInShouldWork() throws Exception {
        // Initialize the database
        inOrderRepository.saveAndFlush(inOrder);

        // Get all the inOrderList where orderDate in DEFAULT_ORDER_DATE or UPDATED_ORDER_DATE
        defaultInOrderShouldBeFound("orderDate.in=" + DEFAULT_ORDER_DATE + "," + UPDATED_ORDER_DATE);

        // Get all the inOrderList where orderDate equals to UPDATED_ORDER_DATE
        defaultInOrderShouldNotBeFound("orderDate.in=" + UPDATED_ORDER_DATE);
    }

    @Test
    @Transactional
    public void getAllInOrdersByOrderDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        inOrderRepository.saveAndFlush(inOrder);

        // Get all the inOrderList where orderDate is not null
        defaultInOrderShouldBeFound("orderDate.specified=true");

        // Get all the inOrderList where orderDate is null
        defaultInOrderShouldNotBeFound("orderDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllInOrdersByOrderDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        inOrderRepository.saveAndFlush(inOrder);

        // Get all the inOrderList where orderDate greater than or equals to DEFAULT_ORDER_DATE
        defaultInOrderShouldBeFound("orderDate.greaterOrEqualThan=" + DEFAULT_ORDER_DATE);

        // Get all the inOrderList where orderDate greater than or equals to UPDATED_ORDER_DATE
        defaultInOrderShouldNotBeFound("orderDate.greaterOrEqualThan=" + UPDATED_ORDER_DATE);
    }

    @Test
    @Transactional
    public void getAllInOrdersByOrderDateIsLessThanSomething() throws Exception {
        // Initialize the database
        inOrderRepository.saveAndFlush(inOrder);

        // Get all the inOrderList where orderDate less than or equals to DEFAULT_ORDER_DATE
        defaultInOrderShouldNotBeFound("orderDate.lessThan=" + DEFAULT_ORDER_DATE);

        // Get all the inOrderList where orderDate less than or equals to UPDATED_ORDER_DATE
        defaultInOrderShouldBeFound("orderDate.lessThan=" + UPDATED_ORDER_DATE);
    }


    @Test
    @Transactional
    public void getAllInOrdersByDeliveryDateIsEqualToSomething() throws Exception {
        // Initialize the database
        inOrderRepository.saveAndFlush(inOrder);

        // Get all the inOrderList where deliveryDate equals to DEFAULT_DELIVERY_DATE
        defaultInOrderShouldBeFound("deliveryDate.equals=" + DEFAULT_DELIVERY_DATE);

        // Get all the inOrderList where deliveryDate equals to UPDATED_DELIVERY_DATE
        defaultInOrderShouldNotBeFound("deliveryDate.equals=" + UPDATED_DELIVERY_DATE);
    }

    @Test
    @Transactional
    public void getAllInOrdersByDeliveryDateIsInShouldWork() throws Exception {
        // Initialize the database
        inOrderRepository.saveAndFlush(inOrder);

        // Get all the inOrderList where deliveryDate in DEFAULT_DELIVERY_DATE or UPDATED_DELIVERY_DATE
        defaultInOrderShouldBeFound("deliveryDate.in=" + DEFAULT_DELIVERY_DATE + "," + UPDATED_DELIVERY_DATE);

        // Get all the inOrderList where deliveryDate equals to UPDATED_DELIVERY_DATE
        defaultInOrderShouldNotBeFound("deliveryDate.in=" + UPDATED_DELIVERY_DATE);
    }

    @Test
    @Transactional
    public void getAllInOrdersByDeliveryDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        inOrderRepository.saveAndFlush(inOrder);

        // Get all the inOrderList where deliveryDate is not null
        defaultInOrderShouldBeFound("deliveryDate.specified=true");

        // Get all the inOrderList where deliveryDate is null
        defaultInOrderShouldNotBeFound("deliveryDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllInOrdersByDeliveryDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        inOrderRepository.saveAndFlush(inOrder);

        // Get all the inOrderList where deliveryDate greater than or equals to DEFAULT_DELIVERY_DATE
        defaultInOrderShouldBeFound("deliveryDate.greaterOrEqualThan=" + DEFAULT_DELIVERY_DATE);

        // Get all the inOrderList where deliveryDate greater than or equals to UPDATED_DELIVERY_DATE
        defaultInOrderShouldNotBeFound("deliveryDate.greaterOrEqualThan=" + UPDATED_DELIVERY_DATE);
    }

    @Test
    @Transactional
    public void getAllInOrdersByDeliveryDateIsLessThanSomething() throws Exception {
        // Initialize the database
        inOrderRepository.saveAndFlush(inOrder);

        // Get all the inOrderList where deliveryDate less than or equals to DEFAULT_DELIVERY_DATE
        defaultInOrderShouldNotBeFound("deliveryDate.lessThan=" + DEFAULT_DELIVERY_DATE);

        // Get all the inOrderList where deliveryDate less than or equals to UPDATED_DELIVERY_DATE
        defaultInOrderShouldBeFound("deliveryDate.lessThan=" + UPDATED_DELIVERY_DATE);
    }


    @Test
    @Transactional
    public void getAllInOrdersByAuthorizedIsEqualToSomething() throws Exception {
        // Initialize the database
        inOrderRepository.saveAndFlush(inOrder);

        // Get all the inOrderList where authorized equals to DEFAULT_AUTHORIZED
        defaultInOrderShouldBeFound("authorized.equals=" + DEFAULT_AUTHORIZED);

        // Get all the inOrderList where authorized equals to UPDATED_AUTHORIZED
        defaultInOrderShouldNotBeFound("authorized.equals=" + UPDATED_AUTHORIZED);
    }

    @Test
    @Transactional
    public void getAllInOrdersByAuthorizedIsInShouldWork() throws Exception {
        // Initialize the database
        inOrderRepository.saveAndFlush(inOrder);

        // Get all the inOrderList where authorized in DEFAULT_AUTHORIZED or UPDATED_AUTHORIZED
        defaultInOrderShouldBeFound("authorized.in=" + DEFAULT_AUTHORIZED + "," + UPDATED_AUTHORIZED);

        // Get all the inOrderList where authorized equals to UPDATED_AUTHORIZED
        defaultInOrderShouldNotBeFound("authorized.in=" + UPDATED_AUTHORIZED);
    }

    @Test
    @Transactional
    public void getAllInOrdersByAuthorizedIsNullOrNotNull() throws Exception {
        // Initialize the database
        inOrderRepository.saveAndFlush(inOrder);

        // Get all the inOrderList where authorized is not null
        defaultInOrderShouldBeFound("authorized.specified=true");

        // Get all the inOrderList where authorized is null
        defaultInOrderShouldNotBeFound("authorized.specified=false");
    }

    @Test
    @Transactional
    public void getAllInOrdersByItemIsEqualToSomething() throws Exception {
        // Initialize the database
        Item item = ItemResourceIntTest.createEntity(em);
        em.persist(item);
        em.flush();
        inOrder.setItem(item);
        inOrderRepository.saveAndFlush(inOrder);
        Long itemId = item.getId();

        // Get all the inOrderList where item equals to itemId
        defaultInOrderShouldBeFound("itemId.equals=" + itemId);

        // Get all the inOrderList where item equals to itemId + 1
        defaultInOrderShouldNotBeFound("itemId.equals=" + (itemId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultInOrderShouldBeFound(String filter) throws Exception {
        restInOrderMockMvc.perform(get("/api/in-orders?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(inOrder.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].information").value(hasItem(DEFAULT_INFORMATION)))
            .andExpect(jsonPath("$.[*].orderQuantity").value(hasItem(DEFAULT_ORDER_QUANTITY.doubleValue())))
            .andExpect(jsonPath("$.[*].delivered").value(hasItem(DEFAULT_DELIVERED.booleanValue())))
            .andExpect(jsonPath("$.[*].orderDate").value(hasItem(DEFAULT_ORDER_DATE.toString())))
            .andExpect(jsonPath("$.[*].deliveryDate").value(hasItem(DEFAULT_DELIVERY_DATE.toString())))
            .andExpect(jsonPath("$.[*].authorized").value(hasItem(DEFAULT_AUTHORIZED.booleanValue())));

        // Check, that the count call also returns 1
        restInOrderMockMvc.perform(get("/api/in-orders/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultInOrderShouldNotBeFound(String filter) throws Exception {
        restInOrderMockMvc.perform(get("/api/in-orders?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restInOrderMockMvc.perform(get("/api/in-orders/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingInOrder() throws Exception {
        // Get the inOrder
        restInOrderMockMvc.perform(get("/api/in-orders/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateInOrder() throws Exception {
        // Initialize the database
        inOrderService.save(inOrder);

        int databaseSizeBeforeUpdate = inOrderRepository.findAll().size();

        // Update the inOrder
        InOrder updatedInOrder = inOrderRepository.findById(inOrder.getId()).get();
        // Disconnect from session so that the updates on updatedInOrder are not directly saved in db
        em.detach(updatedInOrder);
        updatedInOrder
            .title(UPDATED_TITLE)
            .information(UPDATED_INFORMATION)
            .orderQuantity(UPDATED_ORDER_QUANTITY)
            .delivered(UPDATED_DELIVERED)
            .orderDate(UPDATED_ORDER_DATE)
            .deliveryDate(UPDATED_DELIVERY_DATE)
            .authorized(UPDATED_AUTHORIZED);

        restInOrderMockMvc.perform(put("/api/in-orders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedInOrder)))
            .andExpect(status().isOk());

        // Validate the InOrder in the database
        List<InOrder> inOrderList = inOrderRepository.findAll();
        assertThat(inOrderList).hasSize(databaseSizeBeforeUpdate);
        InOrder testInOrder = inOrderList.get(inOrderList.size() - 1);
        assertThat(testInOrder.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testInOrder.getInformation()).isEqualTo(UPDATED_INFORMATION);
        assertThat(testInOrder.getOrderQuantity()).isEqualTo(UPDATED_ORDER_QUANTITY);
        assertThat(testInOrder.isDelivered()).isEqualTo(UPDATED_DELIVERED);
        assertThat(testInOrder.getOrderDate()).isEqualTo(UPDATED_ORDER_DATE);
        assertThat(testInOrder.getDeliveryDate()).isEqualTo(UPDATED_DELIVERY_DATE);
        assertThat(testInOrder.isAuthorized()).isEqualTo(UPDATED_AUTHORIZED);
    }

    @Test
    @Transactional
    public void updateNonExistingInOrder() throws Exception {
        int databaseSizeBeforeUpdate = inOrderRepository.findAll().size();

        // Create the InOrder

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInOrderMockMvc.perform(put("/api/in-orders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(inOrder)))
            .andExpect(status().isBadRequest());

        // Validate the InOrder in the database
        List<InOrder> inOrderList = inOrderRepository.findAll();
        assertThat(inOrderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteInOrder() throws Exception {
        // Initialize the database
        inOrderService.save(inOrder);

        int databaseSizeBeforeDelete = inOrderRepository.findAll().size();

        // Delete the inOrder
        restInOrderMockMvc.perform(delete("/api/in-orders/{id}", inOrder.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<InOrder> inOrderList = inOrderRepository.findAll();
        assertThat(inOrderList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(InOrder.class);
        InOrder inOrder1 = new InOrder();
        inOrder1.setId(1L);
        InOrder inOrder2 = new InOrder();
        inOrder2.setId(inOrder1.getId());
        assertThat(inOrder1).isEqualTo(inOrder2);
        inOrder2.setId(2L);
        assertThat(inOrder1).isNotEqualTo(inOrder2);
        inOrder1.setId(null);
        assertThat(inOrder1).isNotEqualTo(inOrder2);
    }
}
