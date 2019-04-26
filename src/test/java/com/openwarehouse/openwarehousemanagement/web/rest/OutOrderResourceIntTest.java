package com.openwarehouse.openwarehousemanagement.web.rest;

import com.openwarehouse.openwarehousemanagement.OpenWarehouseManagementApp;

import com.openwarehouse.openwarehousemanagement.domain.OutOrder;
import com.openwarehouse.openwarehousemanagement.domain.Item;
import com.openwarehouse.openwarehousemanagement.repository.OutOrderRepository;
import com.openwarehouse.openwarehousemanagement.service.OutOrderService;
import com.openwarehouse.openwarehousemanagement.web.rest.errors.ExceptionTranslator;
import com.openwarehouse.openwarehousemanagement.service.dto.OutOrderCriteria;
import com.openwarehouse.openwarehousemanagement.service.OutOrderQueryService;

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
 * Test class for the OutOrderResource REST controller.
 *
 * @see OutOrderResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = OpenWarehouseManagementApp.class)
public class OutOrderResourceIntTest {

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

    @Autowired
    private OutOrderRepository outOrderRepository;

    @Autowired
    private OutOrderService outOrderService;

    @Autowired
    private OutOrderQueryService outOrderQueryService;

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

    private MockMvc restOutOrderMockMvc;

    private OutOrder outOrder;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final OutOrderResource outOrderResource = new OutOrderResource(outOrderService, outOrderQueryService);
        this.restOutOrderMockMvc = MockMvcBuilders.standaloneSetup(outOrderResource)
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
    public static OutOrder createEntity(EntityManager em) {
        OutOrder outOrder = new OutOrder()
            .title(DEFAULT_TITLE)
            .information(DEFAULT_INFORMATION)
            .orderQuantity(DEFAULT_ORDER_QUANTITY)
            .delivered(DEFAULT_DELIVERED)
            .orderDate(DEFAULT_ORDER_DATE)
            .deliveryDate(DEFAULT_DELIVERY_DATE);
        return outOrder;
    }

    @Before
    public void initTest() {
        outOrder = createEntity(em);
    }

    @Test
    @Transactional
    public void createOutOrder() throws Exception {
        int databaseSizeBeforeCreate = outOrderRepository.findAll().size();

        // Create the OutOrder
        restOutOrderMockMvc.perform(post("/api/out-orders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(outOrder)))
            .andExpect(status().isCreated());

        // Validate the OutOrder in the database
        List<OutOrder> outOrderList = outOrderRepository.findAll();
        assertThat(outOrderList).hasSize(databaseSizeBeforeCreate + 1);
        OutOrder testOutOrder = outOrderList.get(outOrderList.size() - 1);
        assertThat(testOutOrder.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testOutOrder.getInformation()).isEqualTo(DEFAULT_INFORMATION);
        assertThat(testOutOrder.getOrderQuantity()).isEqualTo(DEFAULT_ORDER_QUANTITY);
        assertThat(testOutOrder.isDelivered()).isEqualTo(DEFAULT_DELIVERED);
        assertThat(testOutOrder.getOrderDate()).isEqualTo(DEFAULT_ORDER_DATE);
        assertThat(testOutOrder.getDeliveryDate()).isEqualTo(DEFAULT_DELIVERY_DATE);
    }

    @Test
    @Transactional
    public void createOutOrderWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = outOrderRepository.findAll().size();

        // Create the OutOrder with an existing ID
        outOrder.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restOutOrderMockMvc.perform(post("/api/out-orders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(outOrder)))
            .andExpect(status().isBadRequest());

        // Validate the OutOrder in the database
        List<OutOrder> outOrderList = outOrderRepository.findAll();
        assertThat(outOrderList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = outOrderRepository.findAll().size();
        // set the field null
        outOrder.setTitle(null);

        // Create the OutOrder, which fails.

        restOutOrderMockMvc.perform(post("/api/out-orders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(outOrder)))
            .andExpect(status().isBadRequest());

        List<OutOrder> outOrderList = outOrderRepository.findAll();
        assertThat(outOrderList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkInformationIsRequired() throws Exception {
        int databaseSizeBeforeTest = outOrderRepository.findAll().size();
        // set the field null
        outOrder.setInformation(null);

        // Create the OutOrder, which fails.

        restOutOrderMockMvc.perform(post("/api/out-orders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(outOrder)))
            .andExpect(status().isBadRequest());

        List<OutOrder> outOrderList = outOrderRepository.findAll();
        assertThat(outOrderList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkOrderQuantityIsRequired() throws Exception {
        int databaseSizeBeforeTest = outOrderRepository.findAll().size();
        // set the field null
        outOrder.setOrderQuantity(null);

        // Create the OutOrder, which fails.

        restOutOrderMockMvc.perform(post("/api/out-orders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(outOrder)))
            .andExpect(status().isBadRequest());

        List<OutOrder> outOrderList = outOrderRepository.findAll();
        assertThat(outOrderList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllOutOrders() throws Exception {
        // Initialize the database
        outOrderRepository.saveAndFlush(outOrder);

        // Get all the outOrderList
        restOutOrderMockMvc.perform(get("/api/out-orders?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(outOrder.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
            .andExpect(jsonPath("$.[*].information").value(hasItem(DEFAULT_INFORMATION.toString())))
            .andExpect(jsonPath("$.[*].orderQuantity").value(hasItem(DEFAULT_ORDER_QUANTITY.doubleValue())))
            .andExpect(jsonPath("$.[*].delivered").value(hasItem(DEFAULT_DELIVERED.booleanValue())))
            .andExpect(jsonPath("$.[*].orderDate").value(hasItem(DEFAULT_ORDER_DATE.toString())))
            .andExpect(jsonPath("$.[*].deliveryDate").value(hasItem(DEFAULT_DELIVERY_DATE.toString())));
    }
    
    @Test
    @Transactional
    public void getOutOrder() throws Exception {
        // Initialize the database
        outOrderRepository.saveAndFlush(outOrder);

        // Get the outOrder
        restOutOrderMockMvc.perform(get("/api/out-orders/{id}", outOrder.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(outOrder.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()))
            .andExpect(jsonPath("$.information").value(DEFAULT_INFORMATION.toString()))
            .andExpect(jsonPath("$.orderQuantity").value(DEFAULT_ORDER_QUANTITY.doubleValue()))
            .andExpect(jsonPath("$.delivered").value(DEFAULT_DELIVERED.booleanValue()))
            .andExpect(jsonPath("$.orderDate").value(DEFAULT_ORDER_DATE.toString()))
            .andExpect(jsonPath("$.deliveryDate").value(DEFAULT_DELIVERY_DATE.toString()));
    }

    @Test
    @Transactional
    public void getAllOutOrdersByTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        outOrderRepository.saveAndFlush(outOrder);

        // Get all the outOrderList where title equals to DEFAULT_TITLE
        defaultOutOrderShouldBeFound("title.equals=" + DEFAULT_TITLE);

        // Get all the outOrderList where title equals to UPDATED_TITLE
        defaultOutOrderShouldNotBeFound("title.equals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllOutOrdersByTitleIsInShouldWork() throws Exception {
        // Initialize the database
        outOrderRepository.saveAndFlush(outOrder);

        // Get all the outOrderList where title in DEFAULT_TITLE or UPDATED_TITLE
        defaultOutOrderShouldBeFound("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE);

        // Get all the outOrderList where title equals to UPDATED_TITLE
        defaultOutOrderShouldNotBeFound("title.in=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllOutOrdersByTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        outOrderRepository.saveAndFlush(outOrder);

        // Get all the outOrderList where title is not null
        defaultOutOrderShouldBeFound("title.specified=true");

        // Get all the outOrderList where title is null
        defaultOutOrderShouldNotBeFound("title.specified=false");
    }

    @Test
    @Transactional
    public void getAllOutOrdersByInformationIsEqualToSomething() throws Exception {
        // Initialize the database
        outOrderRepository.saveAndFlush(outOrder);

        // Get all the outOrderList where information equals to DEFAULT_INFORMATION
        defaultOutOrderShouldBeFound("information.equals=" + DEFAULT_INFORMATION);

        // Get all the outOrderList where information equals to UPDATED_INFORMATION
        defaultOutOrderShouldNotBeFound("information.equals=" + UPDATED_INFORMATION);
    }

    @Test
    @Transactional
    public void getAllOutOrdersByInformationIsInShouldWork() throws Exception {
        // Initialize the database
        outOrderRepository.saveAndFlush(outOrder);

        // Get all the outOrderList where information in DEFAULT_INFORMATION or UPDATED_INFORMATION
        defaultOutOrderShouldBeFound("information.in=" + DEFAULT_INFORMATION + "," + UPDATED_INFORMATION);

        // Get all the outOrderList where information equals to UPDATED_INFORMATION
        defaultOutOrderShouldNotBeFound("information.in=" + UPDATED_INFORMATION);
    }

    @Test
    @Transactional
    public void getAllOutOrdersByInformationIsNullOrNotNull() throws Exception {
        // Initialize the database
        outOrderRepository.saveAndFlush(outOrder);

        // Get all the outOrderList where information is not null
        defaultOutOrderShouldBeFound("information.specified=true");

        // Get all the outOrderList where information is null
        defaultOutOrderShouldNotBeFound("information.specified=false");
    }

    @Test
    @Transactional
    public void getAllOutOrdersByOrderQuantityIsEqualToSomething() throws Exception {
        // Initialize the database
        outOrderRepository.saveAndFlush(outOrder);

        // Get all the outOrderList where orderQuantity equals to DEFAULT_ORDER_QUANTITY
        defaultOutOrderShouldBeFound("orderQuantity.equals=" + DEFAULT_ORDER_QUANTITY);

        // Get all the outOrderList where orderQuantity equals to UPDATED_ORDER_QUANTITY
        defaultOutOrderShouldNotBeFound("orderQuantity.equals=" + UPDATED_ORDER_QUANTITY);
    }

    @Test
    @Transactional
    public void getAllOutOrdersByOrderQuantityIsInShouldWork() throws Exception {
        // Initialize the database
        outOrderRepository.saveAndFlush(outOrder);

        // Get all the outOrderList where orderQuantity in DEFAULT_ORDER_QUANTITY or UPDATED_ORDER_QUANTITY
        defaultOutOrderShouldBeFound("orderQuantity.in=" + DEFAULT_ORDER_QUANTITY + "," + UPDATED_ORDER_QUANTITY);

        // Get all the outOrderList where orderQuantity equals to UPDATED_ORDER_QUANTITY
        defaultOutOrderShouldNotBeFound("orderQuantity.in=" + UPDATED_ORDER_QUANTITY);
    }

    @Test
    @Transactional
    public void getAllOutOrdersByOrderQuantityIsNullOrNotNull() throws Exception {
        // Initialize the database
        outOrderRepository.saveAndFlush(outOrder);

        // Get all the outOrderList where orderQuantity is not null
        defaultOutOrderShouldBeFound("orderQuantity.specified=true");

        // Get all the outOrderList where orderQuantity is null
        defaultOutOrderShouldNotBeFound("orderQuantity.specified=false");
    }

    @Test
    @Transactional
    public void getAllOutOrdersByDeliveredIsEqualToSomething() throws Exception {
        // Initialize the database
        outOrderRepository.saveAndFlush(outOrder);

        // Get all the outOrderList where delivered equals to DEFAULT_DELIVERED
        defaultOutOrderShouldBeFound("delivered.equals=" + DEFAULT_DELIVERED);

        // Get all the outOrderList where delivered equals to UPDATED_DELIVERED
        defaultOutOrderShouldNotBeFound("delivered.equals=" + UPDATED_DELIVERED);
    }

    @Test
    @Transactional
    public void getAllOutOrdersByDeliveredIsInShouldWork() throws Exception {
        // Initialize the database
        outOrderRepository.saveAndFlush(outOrder);

        // Get all the outOrderList where delivered in DEFAULT_DELIVERED or UPDATED_DELIVERED
        defaultOutOrderShouldBeFound("delivered.in=" + DEFAULT_DELIVERED + "," + UPDATED_DELIVERED);

        // Get all the outOrderList where delivered equals to UPDATED_DELIVERED
        defaultOutOrderShouldNotBeFound("delivered.in=" + UPDATED_DELIVERED);
    }

    @Test
    @Transactional
    public void getAllOutOrdersByDeliveredIsNullOrNotNull() throws Exception {
        // Initialize the database
        outOrderRepository.saveAndFlush(outOrder);

        // Get all the outOrderList where delivered is not null
        defaultOutOrderShouldBeFound("delivered.specified=true");

        // Get all the outOrderList where delivered is null
        defaultOutOrderShouldNotBeFound("delivered.specified=false");
    }

    @Test
    @Transactional
    public void getAllOutOrdersByOrderDateIsEqualToSomething() throws Exception {
        // Initialize the database
        outOrderRepository.saveAndFlush(outOrder);

        // Get all the outOrderList where orderDate equals to DEFAULT_ORDER_DATE
        defaultOutOrderShouldBeFound("orderDate.equals=" + DEFAULT_ORDER_DATE);

        // Get all the outOrderList where orderDate equals to UPDATED_ORDER_DATE
        defaultOutOrderShouldNotBeFound("orderDate.equals=" + UPDATED_ORDER_DATE);
    }

    @Test
    @Transactional
    public void getAllOutOrdersByOrderDateIsInShouldWork() throws Exception {
        // Initialize the database
        outOrderRepository.saveAndFlush(outOrder);

        // Get all the outOrderList where orderDate in DEFAULT_ORDER_DATE or UPDATED_ORDER_DATE
        defaultOutOrderShouldBeFound("orderDate.in=" + DEFAULT_ORDER_DATE + "," + UPDATED_ORDER_DATE);

        // Get all the outOrderList where orderDate equals to UPDATED_ORDER_DATE
        defaultOutOrderShouldNotBeFound("orderDate.in=" + UPDATED_ORDER_DATE);
    }

    @Test
    @Transactional
    public void getAllOutOrdersByOrderDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        outOrderRepository.saveAndFlush(outOrder);

        // Get all the outOrderList where orderDate is not null
        defaultOutOrderShouldBeFound("orderDate.specified=true");

        // Get all the outOrderList where orderDate is null
        defaultOutOrderShouldNotBeFound("orderDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllOutOrdersByOrderDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        outOrderRepository.saveAndFlush(outOrder);

        // Get all the outOrderList where orderDate greater than or equals to DEFAULT_ORDER_DATE
        defaultOutOrderShouldBeFound("orderDate.greaterOrEqualThan=" + DEFAULT_ORDER_DATE);

        // Get all the outOrderList where orderDate greater than or equals to UPDATED_ORDER_DATE
        defaultOutOrderShouldNotBeFound("orderDate.greaterOrEqualThan=" + UPDATED_ORDER_DATE);
    }

    @Test
    @Transactional
    public void getAllOutOrdersByOrderDateIsLessThanSomething() throws Exception {
        // Initialize the database
        outOrderRepository.saveAndFlush(outOrder);

        // Get all the outOrderList where orderDate less than or equals to DEFAULT_ORDER_DATE
        defaultOutOrderShouldNotBeFound("orderDate.lessThan=" + DEFAULT_ORDER_DATE);

        // Get all the outOrderList where orderDate less than or equals to UPDATED_ORDER_DATE
        defaultOutOrderShouldBeFound("orderDate.lessThan=" + UPDATED_ORDER_DATE);
    }


    @Test
    @Transactional
    public void getAllOutOrdersByDeliveryDateIsEqualToSomething() throws Exception {
        // Initialize the database
        outOrderRepository.saveAndFlush(outOrder);

        // Get all the outOrderList where deliveryDate equals to DEFAULT_DELIVERY_DATE
        defaultOutOrderShouldBeFound("deliveryDate.equals=" + DEFAULT_DELIVERY_DATE);

        // Get all the outOrderList where deliveryDate equals to UPDATED_DELIVERY_DATE
        defaultOutOrderShouldNotBeFound("deliveryDate.equals=" + UPDATED_DELIVERY_DATE);
    }

    @Test
    @Transactional
    public void getAllOutOrdersByDeliveryDateIsInShouldWork() throws Exception {
        // Initialize the database
        outOrderRepository.saveAndFlush(outOrder);

        // Get all the outOrderList where deliveryDate in DEFAULT_DELIVERY_DATE or UPDATED_DELIVERY_DATE
        defaultOutOrderShouldBeFound("deliveryDate.in=" + DEFAULT_DELIVERY_DATE + "," + UPDATED_DELIVERY_DATE);

        // Get all the outOrderList where deliveryDate equals to UPDATED_DELIVERY_DATE
        defaultOutOrderShouldNotBeFound("deliveryDate.in=" + UPDATED_DELIVERY_DATE);
    }

    @Test
    @Transactional
    public void getAllOutOrdersByDeliveryDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        outOrderRepository.saveAndFlush(outOrder);

        // Get all the outOrderList where deliveryDate is not null
        defaultOutOrderShouldBeFound("deliveryDate.specified=true");

        // Get all the outOrderList where deliveryDate is null
        defaultOutOrderShouldNotBeFound("deliveryDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllOutOrdersByDeliveryDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        outOrderRepository.saveAndFlush(outOrder);

        // Get all the outOrderList where deliveryDate greater than or equals to DEFAULT_DELIVERY_DATE
        defaultOutOrderShouldBeFound("deliveryDate.greaterOrEqualThan=" + DEFAULT_DELIVERY_DATE);

        // Get all the outOrderList where deliveryDate greater than or equals to UPDATED_DELIVERY_DATE
        defaultOutOrderShouldNotBeFound("deliveryDate.greaterOrEqualThan=" + UPDATED_DELIVERY_DATE);
    }

    @Test
    @Transactional
    public void getAllOutOrdersByDeliveryDateIsLessThanSomething() throws Exception {
        // Initialize the database
        outOrderRepository.saveAndFlush(outOrder);

        // Get all the outOrderList where deliveryDate less than or equals to DEFAULT_DELIVERY_DATE
        defaultOutOrderShouldNotBeFound("deliveryDate.lessThan=" + DEFAULT_DELIVERY_DATE);

        // Get all the outOrderList where deliveryDate less than or equals to UPDATED_DELIVERY_DATE
        defaultOutOrderShouldBeFound("deliveryDate.lessThan=" + UPDATED_DELIVERY_DATE);
    }


    @Test
    @Transactional
    public void getAllOutOrdersByItemIsEqualToSomething() throws Exception {
        // Initialize the database
        Item item = ItemResourceIntTest.createEntity(em);
        em.persist(item);
        em.flush();
        outOrder.setItem(item);
        outOrderRepository.saveAndFlush(outOrder);
        Long itemId = item.getId();

        // Get all the outOrderList where item equals to itemId
        defaultOutOrderShouldBeFound("itemId.equals=" + itemId);

        // Get all the outOrderList where item equals to itemId + 1
        defaultOutOrderShouldNotBeFound("itemId.equals=" + (itemId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultOutOrderShouldBeFound(String filter) throws Exception {
        restOutOrderMockMvc.perform(get("/api/out-orders?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(outOrder.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].information").value(hasItem(DEFAULT_INFORMATION)))
            .andExpect(jsonPath("$.[*].orderQuantity").value(hasItem(DEFAULT_ORDER_QUANTITY.doubleValue())))
            .andExpect(jsonPath("$.[*].delivered").value(hasItem(DEFAULT_DELIVERED.booleanValue())))
            .andExpect(jsonPath("$.[*].orderDate").value(hasItem(DEFAULT_ORDER_DATE.toString())))
            .andExpect(jsonPath("$.[*].deliveryDate").value(hasItem(DEFAULT_DELIVERY_DATE.toString())));

        // Check, that the count call also returns 1
        restOutOrderMockMvc.perform(get("/api/out-orders/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultOutOrderShouldNotBeFound(String filter) throws Exception {
        restOutOrderMockMvc.perform(get("/api/out-orders?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restOutOrderMockMvc.perform(get("/api/out-orders/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingOutOrder() throws Exception {
        // Get the outOrder
        restOutOrderMockMvc.perform(get("/api/out-orders/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateOutOrder() throws Exception {
        // Initialize the database
        outOrderService.save(outOrder);

        int databaseSizeBeforeUpdate = outOrderRepository.findAll().size();

        // Update the outOrder
        OutOrder updatedOutOrder = outOrderRepository.findById(outOrder.getId()).get();
        // Disconnect from session so that the updates on updatedOutOrder are not directly saved in db
        em.detach(updatedOutOrder);
        updatedOutOrder
            .title(UPDATED_TITLE)
            .information(UPDATED_INFORMATION)
            .orderQuantity(UPDATED_ORDER_QUANTITY)
            .delivered(UPDATED_DELIVERED)
            .orderDate(UPDATED_ORDER_DATE)
            .deliveryDate(UPDATED_DELIVERY_DATE);

        restOutOrderMockMvc.perform(put("/api/out-orders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedOutOrder)))
            .andExpect(status().isOk());

        // Validate the OutOrder in the database
        List<OutOrder> outOrderList = outOrderRepository.findAll();
        assertThat(outOrderList).hasSize(databaseSizeBeforeUpdate);
        OutOrder testOutOrder = outOrderList.get(outOrderList.size() - 1);
        assertThat(testOutOrder.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testOutOrder.getInformation()).isEqualTo(UPDATED_INFORMATION);
        assertThat(testOutOrder.getOrderQuantity()).isEqualTo(UPDATED_ORDER_QUANTITY);
        assertThat(testOutOrder.isDelivered()).isEqualTo(UPDATED_DELIVERED);
        assertThat(testOutOrder.getOrderDate()).isEqualTo(UPDATED_ORDER_DATE);
        assertThat(testOutOrder.getDeliveryDate()).isEqualTo(UPDATED_DELIVERY_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingOutOrder() throws Exception {
        int databaseSizeBeforeUpdate = outOrderRepository.findAll().size();

        // Create the OutOrder

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOutOrderMockMvc.perform(put("/api/out-orders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(outOrder)))
            .andExpect(status().isBadRequest());

        // Validate the OutOrder in the database
        List<OutOrder> outOrderList = outOrderRepository.findAll();
        assertThat(outOrderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteOutOrder() throws Exception {
        // Initialize the database
        outOrderService.save(outOrder);

        int databaseSizeBeforeDelete = outOrderRepository.findAll().size();

        // Delete the outOrder
        restOutOrderMockMvc.perform(delete("/api/out-orders/{id}", outOrder.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<OutOrder> outOrderList = outOrderRepository.findAll();
        assertThat(outOrderList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(OutOrder.class);
        OutOrder outOrder1 = new OutOrder();
        outOrder1.setId(1L);
        OutOrder outOrder2 = new OutOrder();
        outOrder2.setId(outOrder1.getId());
        assertThat(outOrder1).isEqualTo(outOrder2);
        outOrder2.setId(2L);
        assertThat(outOrder1).isNotEqualTo(outOrder2);
        outOrder1.setId(null);
        assertThat(outOrder1).isNotEqualTo(outOrder2);
    }
}
