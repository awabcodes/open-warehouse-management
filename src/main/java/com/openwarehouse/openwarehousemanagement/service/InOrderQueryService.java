package com.openwarehouse.openwarehousemanagement.service;

import java.util.List;

import javax.persistence.criteria.JoinType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import com.openwarehouse.openwarehousemanagement.domain.InOrder;
import com.openwarehouse.openwarehousemanagement.domain.*; // for static metamodels
import com.openwarehouse.openwarehousemanagement.repository.InOrderRepository;
import com.openwarehouse.openwarehousemanagement.service.dto.InOrderCriteria;

/**
 * Service for executing complex queries for InOrder entities in the database.
 * The main input is a {@link InOrderCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link InOrder} or a {@link Page} of {@link InOrder} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class InOrderQueryService extends QueryService<InOrder> {

    private final Logger log = LoggerFactory.getLogger(InOrderQueryService.class);

    private final InOrderRepository inOrderRepository;

    public InOrderQueryService(InOrderRepository inOrderRepository) {
        this.inOrderRepository = inOrderRepository;
    }

    /**
     * Return a {@link List} of {@link InOrder} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<InOrder> findByCriteria(InOrderCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<InOrder> specification = createSpecification(criteria);
        return inOrderRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link InOrder} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<InOrder> findByCriteria(InOrderCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<InOrder> specification = createSpecification(criteria);
        return inOrderRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(InOrderCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<InOrder> specification = createSpecification(criteria);
        return inOrderRepository.count(specification);
    }

    /**
     * Function to convert InOrderCriteria to a {@link Specification}
     */
    private Specification<InOrder> createSpecification(InOrderCriteria criteria) {
        Specification<InOrder> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), InOrder_.id));
            }
            if (criteria.getTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTitle(), InOrder_.title));
            }
            if (criteria.getInformation() != null) {
                specification = specification.and(buildStringSpecification(criteria.getInformation(), InOrder_.information));
            }
            if (criteria.getOrderQuantity() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getOrderQuantity(), InOrder_.orderQuantity));
            }
            if (criteria.getDelivered() != null) {
                specification = specification.and(buildSpecification(criteria.getDelivered(), InOrder_.delivered));
            }
            if (criteria.getOrderDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getOrderDate(), InOrder_.orderDate));
            }
            if (criteria.getDeliveryDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDeliveryDate(), InOrder_.deliveryDate));
            }
            if (criteria.getAuthorized() != null) {
                specification = specification.and(buildSpecification(criteria.getAuthorized(), InOrder_.authorized));
            }
            if (criteria.getItemId() != null) {
                specification = specification.and(buildSpecification(criteria.getItemId(),
                    root -> root.join(InOrder_.item, JoinType.LEFT).get(Item_.id)));
            }
        }
        return specification;
    }
}
