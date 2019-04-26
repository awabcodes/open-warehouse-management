package com.openwarehouse.openwarehousemanagement.repository;

import com.openwarehouse.openwarehousemanagement.domain.OutOrder;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the OutOrder entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OutOrderRepository extends JpaRepository<OutOrder, Long>, JpaSpecificationExecutor<OutOrder> {

}
