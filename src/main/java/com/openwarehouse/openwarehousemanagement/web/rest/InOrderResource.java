package com.openwarehouse.openwarehousemanagement.web.rest;
import com.openwarehouse.openwarehousemanagement.domain.InOrder;
import com.openwarehouse.openwarehousemanagement.security.AuthoritiesConstants;
import com.openwarehouse.openwarehousemanagement.service.InOrderService;
import com.openwarehouse.openwarehousemanagement.web.rest.errors.BadRequestAlertException;
import com.openwarehouse.openwarehousemanagement.web.rest.util.HeaderUtil;
import com.openwarehouse.openwarehousemanagement.web.rest.util.PaginationUtil;
import com.openwarehouse.openwarehousemanagement.service.dto.InOrderCriteria;
import com.openwarehouse.openwarehousemanagement.service.InOrderQueryService;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing InOrder.
 */
@RestController
@RequestMapping("/api")
public class InOrderResource {

    private final Logger log = LoggerFactory.getLogger(InOrderResource.class);

    private static final String ENTITY_NAME = "inOrder";

    private final InOrderService inOrderService;

    private final InOrderQueryService inOrderQueryService;

    public InOrderResource(InOrderService inOrderService, InOrderQueryService inOrderQueryService) {
        this.inOrderService = inOrderService;
        this.inOrderQueryService = inOrderQueryService;
    }

    /**
     * POST  /in-orders : Create a new inOrder.
     *
     * @param inOrder the inOrder to create
     * @return the ResponseEntity with status 201 (Created) and with body the new inOrder, or with status 400 (Bad Request) if the inOrder has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/in-orders")
    public ResponseEntity<InOrder> createInOrder(@Valid @RequestBody InOrder inOrder) throws URISyntaxException {
        log.debug("REST request to save InOrder : {}", inOrder);
        if (inOrder.getId() != null) {
            throw new BadRequestAlertException("A new inOrder cannot already have an ID", ENTITY_NAME, "idexists");
        }
        InOrder result = inOrderService.save(inOrder);
        return ResponseEntity.created(new URI("/api/in-orders/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PostMapping("/in-orders-authorize/{id}")
    @Secured(AuthoritiesConstants.AUTHORIZER)
    public ResponseEntity<InOrder> authorizeInOrder(@PathVariable Long id) throws URISyntaxException {
        log.debug("REST request to authorize InOrder : {}", id);
        InOrder result = inOrderService.authorize(id);
        return ResponseEntity.ok().body(result);
    }

    @PostMapping("/in-orders-deliver/{id}")
    @Secured(AuthoritiesConstants.MANAGER)
    public ResponseEntity<InOrder> deliverInOrder(@PathVariable Long id) throws URISyntaxException {
        log.debug("REST request to deliver InOrder : {}", id);
        InOrder result = inOrderService.deliver(id);
        if (result == null) {
            throw new BadRequestAlertException("Either Unauthorized order or No Items", ENTITY_NAME, "notdelivered");
        }
        return ResponseEntity.ok().body(result);
    }

    /**
     * PUT  /in-orders : Updates an existing inOrder.
     *
     * @param inOrder the inOrder to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated inOrder,
     * or with status 400 (Bad Request) if the inOrder is not valid,
     * or with status 500 (Internal Server Error) if the inOrder couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/in-orders")
    public ResponseEntity<InOrder> updateInOrder(@Valid @RequestBody InOrder inOrder) throws URISyntaxException {
        log.debug("REST request to update InOrder : {}", inOrder);
        if (inOrder.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        InOrder result = inOrderService.save(inOrder);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, inOrder.getId().toString()))
            .body(result);
    }

    /**
     * GET  /in-orders : get all the inOrders.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of inOrders in body
     */
    @GetMapping("/in-orders")
    public ResponseEntity<List<InOrder>> getAllInOrders(InOrderCriteria criteria, Pageable pageable) {
        log.debug("REST request to get InOrders by criteria: {}", criteria);
        Page<InOrder> page = inOrderQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/in-orders");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * GET  /in-orders/count : count all the inOrders.
    *
    * @param criteria the criterias which the requested entities should match
    * @return the ResponseEntity with status 200 (OK) and the count in body
    */
    @GetMapping("/in-orders/count")
    public ResponseEntity<Long> countInOrders(InOrderCriteria criteria) {
        log.debug("REST request to count InOrders by criteria: {}", criteria);
        return ResponseEntity.ok().body(inOrderQueryService.countByCriteria(criteria));
    }

    /**
     * GET  /in-orders/:id : get the "id" inOrder.
     *
     * @param id the id of the inOrder to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the inOrder, or with status 404 (Not Found)
     */
    @GetMapping("/in-orders/{id}")
    public ResponseEntity<InOrder> getInOrder(@PathVariable Long id) {
        log.debug("REST request to get InOrder : {}", id);
        Optional<InOrder> inOrder = inOrderService.findOne(id);
        return ResponseUtil.wrapOrNotFound(inOrder);
    }

    /**
     * DELETE  /in-orders/:id : delete the "id" inOrder.
     *
     * @param id the id of the inOrder to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/in-orders/{id}")
    public ResponseEntity<Void> deleteInOrder(@PathVariable Long id) {
        log.debug("REST request to delete InOrder : {}", id);
        inOrderService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
