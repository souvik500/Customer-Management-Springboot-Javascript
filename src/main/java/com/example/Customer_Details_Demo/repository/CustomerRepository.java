package com.example.Customer_Details_Demo.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.Customer_Details_Demo.entity.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

	Optional<Customer> findByUuid(String uuid);

	@Query("SELECT c FROM Customer c WHERE "
			+ "(:search IS NULL OR LOWER(c.firstName) LIKE LOWER(CONCAT('%', :search, '%')) OR "
			+ "LOWER(c.lastName) LIKE LOWER(CONCAT('%', :search, '%')) OR "
			+ "LOWER(c.email) LIKE LOWER(CONCAT('%', :search, '%')) OR "
			+ "LOWER(c.phone) LIKE LOWER(CONCAT('%', :search, '%')))")
	Page<Customer> findAllWithSearch(@Param("search") String search, Pageable pageable);
}