package com.openwarehouse.openwarehousemanagement.repository;

import com.openwarehouse.openwarehousemanagement.domain.Authority;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the Authority entity.
 */
public interface AuthorityRepository extends JpaRepository<Authority, String> {
}
