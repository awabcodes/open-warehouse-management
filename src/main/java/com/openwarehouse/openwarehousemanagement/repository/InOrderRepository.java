package com.openwarehouse.openwarehousemanagement.repository;

import com.openwarehouse.openwarehousemanagement.domain.InOrder;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the InOrder entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InOrderRepository extends JpaRepository<InOrder, Long>, JpaSpecificationExecutor<InOrder> {

}
