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

import com.openwarehouse.openwarehousemanagement.domain.OutOrder;
import com.openwarehouse.openwarehousemanagement.domain.*; // for static metamodels
import com.openwarehouse.openwarehousemanagement.repository.OutOrderRepository;
import com.openwarehouse.openwarehousemanagement.service.dto.OutOrderCriteria;

/**
 * Service for executing complex queries for OutOrder entities in the database.
 * The main input is a {@link OutOrderCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link OutOrder} or a {@link Page} of {@link OutOrder} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class OutOrderQueryService extends QueryService<OutOrder> {

    private final Logger log = LoggerFactory.getLogger(OutOrderQueryService.class);

    private final OutOrderRepository outOrderRepository;

    public OutOrderQueryService(OutOrderRepository outOrderRepository) {
        this.outOrderRepository = outOrderRepository;
    }

    /**
     * Return a {@link List} of {@link OutOrder} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<OutOrder> findByCriteria(OutOrderCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<OutOrder> specification = createSpecification(criteria);
        return outOrderRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link OutOrder} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<OutOrder> findByCriteria(OutOrderCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<OutOrder> specification = createSpecification(criteria);
        return outOrderRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(OutOrderCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<OutOrder> specification = createSpecification(criteria);
        return outOrderRepository.count(specification);
    }

    /**
     * Function to convert OutOrderCriteria to a {@link Specification}
     */
    private Specification<OutOrder> createSpecification(OutOrderCriteria criteria) {
        Specification<OutOrder> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), OutOrder_.id));
            }
            if (criteria.getTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTitle(), OutOrder_.title));
            }
            if (criteria.getInformation() != null) {
                specification = specification.and(buildStringSpecification(criteria.getInformation(), OutOrder_.information));
            }
            if (criteria.getOrderQuantity() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getOrderQuantity(), OutOrder_.orderQuantity));
            }
            if (criteria.getDelivered() != null) {
                specification = specification.and(buildSpecification(criteria.getDelivered(), OutOrder_.delivered));
            }
            if (criteria.getOrderDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getOrderDate(), OutOrder_.orderDate));
            }
            if (criteria.getDeliveryDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDeliveryDate(), OutOrder_.deliveryDate));
            }
            if (criteria.getAuthorized() != null) {
                specification = specification.and(buildSpecification(criteria.getAuthorized(), OutOrder_.authorized));
            }
            if (criteria.getItemId() != null) {
                specification = specification.and(buildSpecification(criteria.getItemId(),
                    root -> root.join(OutOrder_.item, JoinType.LEFT).get(Item_.id)));
            }
        }
        return specification;
    }
}
