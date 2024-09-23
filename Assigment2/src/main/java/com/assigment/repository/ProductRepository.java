package com.assigment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.assigment.model.Product;

/**
 * Spring data JPA responsible to perform CRUD operation for Product
 * @author salam
 *
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

}
