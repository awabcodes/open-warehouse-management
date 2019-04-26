package com.openwarehouse.openwarehousemanagement.service;

import com.openwarehouse.openwarehousemanagement.domain.InOrder;
import com.openwarehouse.openwarehousemanagement.repository.InOrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing InOrder.
 */
@Service
@Transactional
public class InOrderService {

    private final Logger log = LoggerFactory.getLogger(InOrderService.class);

    private final InOrderRepository inOrderRepository;

    public InOrderService(InOrderRepository inOrderRepository) {
        this.inOrderRepository = inOrderRepository;
    }

    /**
     * Save a inOrder.
     *
     * @param inOrder the entity to save
     * @return the persisted entity
     */
    public InOrder save(InOrder inOrder) {
        log.debug("Request to save InOrder : {}", inOrder);
        return inOrderRepository.save(inOrder);
    }

    /**
     * Get all the inOrders.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<InOrder> findAll(Pageable pageable) {
        log.debug("Request to get all InOrders");
        return inOrderRepository.findAll(pageable);
    }


    /**
     * Get one inOrder by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<InOrder> findOne(Long id) {
        log.debug("Request to get InOrder : {}", id);
        return inOrderRepository.findById(id);
    }

    /**
     * Delete the inOrder by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete InOrder : {}", id);
        inOrderRepository.deleteById(id);
    }
}
