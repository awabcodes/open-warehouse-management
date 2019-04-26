package com.openwarehouse.openwarehousemanagement.service;

import com.openwarehouse.openwarehousemanagement.domain.Item;
import com.openwarehouse.openwarehousemanagement.domain.OutOrder;
import com.openwarehouse.openwarehousemanagement.repository.OutOrderRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

/**
 * Service Implementation for managing OutOrder.
 */
@Service
@Transactional
public class OutOrderService {

    private final Logger log = LoggerFactory.getLogger(OutOrderService.class);

    private final OutOrderRepository outOrderRepository;

    private final ItemService itemService;

    public OutOrderService(OutOrderRepository outOrderRepository, ItemService itemService) {
        this.outOrderRepository = outOrderRepository;
        this.itemService = itemService;
    }

    /**
     * Save a outOrder.
     *
     * @param outOrder the entity to save
     * @return the persisted entity
     */
    public OutOrder save(OutOrder outOrder) {
        log.debug("Request to save OutOrder : {}", outOrder);
        outOrder.setAuthorized(false);
        outOrder.setDelivered(false);
        outOrder.setOrderDate(LocalDate.now());
        return outOrderRepository.save(outOrder);
    }

    public OutOrder authorize(Long id) {
        log.debug("Request to authorize OutOrder : {}", id);
        OutOrder outOrder = findOne(id).get();
        outOrder.setAuthorized(!outOrder.isAuthorized());
        return outOrderRepository.save(outOrder);
    }

    public OutOrder deliver(Long id) {
        log.debug("Request to deliver OutOrder : {}", id);
        OutOrder outOrder = findOne(id).get();
        Item item = itemService.findOne(outOrder.getItem().getId()).get();
        
        double leftQuantity = item.getAvailableQuantity() - outOrder.getOrderQuantity();
        if (outOrder.isAuthorized()) {
            if (leftQuantity < item.getMinimumQuantity()) {
                return null;   
            } else {
                item.setAvailableQuantity(leftQuantity);
                outOrder.setDelivered(true);
                outOrder.setDeliveryDate(LocalDate.now());
                return outOrderRepository.save(outOrder);
            }
        } else {
            return null;
        }
    }

    /**
     * Get all the outOrders.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<OutOrder> findAll(Pageable pageable) {
        log.debug("Request to get all OutOrders");
        return outOrderRepository.findAll(pageable);
    }


    /**
     * Get one outOrder by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<OutOrder> findOne(Long id) {
        log.debug("Request to get OutOrder : {}", id);
        return outOrderRepository.findById(id);
    }

    /**
     * Delete the outOrder by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete OutOrder : {}", id);
        outOrderRepository.deleteById(id);
    }
}
