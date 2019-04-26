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

import com.openwarehouse.openwarehousemanagement.domain.Item;
import com.openwarehouse.openwarehousemanagement.domain.*; // for static metamodels
import com.openwarehouse.openwarehousemanagement.repository.ItemRepository;
import com.openwarehouse.openwarehousemanagement.service.dto.ItemCriteria;

/**
 * Service for executing complex queries for Item entities in the database.
 * The main input is a {@link ItemCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Item} or a {@link Page} of {@link Item} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ItemQueryService extends QueryService<Item> {

    private final Logger log = LoggerFactory.getLogger(ItemQueryService.class);

    private final ItemRepository itemRepository;

    public ItemQueryService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    /**
     * Return a {@link List} of {@link Item} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Item> findByCriteria(ItemCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Item> specification = createSpecification(criteria);
        return itemRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Item} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Item> findByCriteria(ItemCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Item> specification = createSpecification(criteria);
        return itemRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ItemCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Item> specification = createSpecification(criteria);
        return itemRepository.count(specification);
    }

    /**
     * Function to convert ItemCriteria to a {@link Specification}
     */
    private Specification<Item> createSpecification(ItemCriteria criteria) {
        Specification<Item> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Item_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Item_.name));
            }
            if (criteria.getType() != null) {
                specification = specification.and(buildStringSpecification(criteria.getType(), Item_.type));
            }
            if (criteria.getAvailableQuantity() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAvailableQuantity(), Item_.availableQuantity));
            }
            if (criteria.getMinimumQuantity() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getMinimumQuantity(), Item_.minimumQuantity));
            }
            if (criteria.getPrice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPrice(), Item_.price));
            }
            if (criteria.getSpecification() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSpecification(), Item_.specification));
            }
            if (criteria.getSupplier() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSupplier(), Item_.supplier));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), Item_.description));
            }
            if (criteria.getOutOrdersId() != null) {
                specification = specification.and(buildSpecification(criteria.getOutOrdersId(),
                    root -> root.join(Item_.outOrders, JoinType.LEFT).get(OutOrder_.id)));
            }
            if (criteria.getInOrdersId() != null) {
                specification = specification.and(buildSpecification(criteria.getInOrdersId(),
                    root -> root.join(Item_.inOrders, JoinType.LEFT).get(InOrder_.id)));
            }
        }
        return specification;
    }
}
