package com.openwarehouse.openwarehousemanagement.web.rest;
import com.openwarehouse.openwarehousemanagement.domain.OutOrder;
import com.openwarehouse.openwarehousemanagement.service.OutOrderService;
import com.openwarehouse.openwarehousemanagement.web.rest.errors.BadRequestAlertException;
import com.openwarehouse.openwarehousemanagement.web.rest.util.HeaderUtil;
import com.openwarehouse.openwarehousemanagement.web.rest.util.PaginationUtil;
import com.openwarehouse.openwarehousemanagement.service.dto.OutOrderCriteria;
import com.openwarehouse.openwarehousemanagement.service.OutOrderQueryService;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing OutOrder.
 */
@RestController
@RequestMapping("/api")
public class OutOrderResource {

    private final Logger log = LoggerFactory.getLogger(OutOrderResource.class);

    private static final String ENTITY_NAME = "outOrder";

    private final OutOrderService outOrderService;

    private final OutOrderQueryService outOrderQueryService;

    public OutOrderResource(OutOrderService outOrderService, OutOrderQueryService outOrderQueryService) {
        this.outOrderService = outOrderService;
        this.outOrderQueryService = outOrderQueryService;
    }

    /**
     * POST  /out-orders : Create a new outOrder.
     *
     * @param outOrder the outOrder to create
     * @return the ResponseEntity with status 201 (Created) and with body the new outOrder, or with status 400 (Bad Request) if the outOrder has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/out-orders")
    public ResponseEntity<OutOrder> createOutOrder(@Valid @RequestBody OutOrder outOrder) throws URISyntaxException {
        log.debug("REST request to save OutOrder : {}", outOrder);
        if (outOrder.getId() != null) {
            throw new BadRequestAlertException("A new outOrder cannot already have an ID", ENTITY_NAME, "idexists");
        }
        OutOrder result = outOrderService.save(outOrder);
        return ResponseEntity.created(new URI("/api/out-orders/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /out-orders : Updates an existing outOrder.
     *
     * @param outOrder the outOrder to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated outOrder,
     * or with status 400 (Bad Request) if the outOrder is not valid,
     * or with status 500 (Internal Server Error) if the outOrder couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/out-orders")
    public ResponseEntity<OutOrder> updateOutOrder(@Valid @RequestBody OutOrder outOrder) throws URISyntaxException {
        log.debug("REST request to update OutOrder : {}", outOrder);
        if (outOrder.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        OutOrder result = outOrderService.save(outOrder);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, outOrder.getId().toString()))
            .body(result);
    }

    /**
     * GET  /out-orders : get all the outOrders.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of outOrders in body
     */
    @GetMapping("/out-orders")
    public ResponseEntity<List<OutOrder>> getAllOutOrders(OutOrderCriteria criteria, Pageable pageable) {
        log.debug("REST request to get OutOrders by criteria: {}", criteria);
        Page<OutOrder> page = outOrderQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/out-orders");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * GET  /out-orders/count : count all the outOrders.
    *
    * @param criteria the criterias which the requested entities should match
    * @return the ResponseEntity with status 200 (OK) and the count in body
    */
    @GetMapping("/out-orders/count")
    public ResponseEntity<Long> countOutOrders(OutOrderCriteria criteria) {
        log.debug("REST request to count OutOrders by criteria: {}", criteria);
        return ResponseEntity.ok().body(outOrderQueryService.countByCriteria(criteria));
    }

    /**
     * GET  /out-orders/:id : get the "id" outOrder.
     *
     * @param id the id of the outOrder to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the outOrder, or with status 404 (Not Found)
     */
    @GetMapping("/out-orders/{id}")
    public ResponseEntity<OutOrder> getOutOrder(@PathVariable Long id) {
        log.debug("REST request to get OutOrder : {}", id);
        Optional<OutOrder> outOrder = outOrderService.findOne(id);
        return ResponseUtil.wrapOrNotFound(outOrder);
    }

    /**
     * DELETE  /out-orders/:id : delete the "id" outOrder.
     *
     * @param id the id of the outOrder to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/out-orders/{id}")
    public ResponseEntity<Void> deleteOutOrder(@PathVariable Long id) {
        log.debug("REST request to delete OutOrder : {}", id);
        outOrderService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
