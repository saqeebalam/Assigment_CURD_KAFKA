package com.assigment.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.assigment.model.Product;
import com.assigment.repository.ProductRepository;

import jakarta.transaction.Transactional;

/**
 * This is Product service class business logic is performing here.
 * @author salam
 *
 */
@Service
public class ProductService {


	 // Initialize the logger for this class
   private static final Logger logger = LoggerFactory.getLogger(ProductService.class);
	
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private KafkaProducerService kafkaProducerService;
    
    /**
     * On this method we save new created data on table 'products' and produce data to kafka topic 'product_create'
     * @param product
     * @return
     */
    @Transactional
    public Product createProduct(Product product) {
    	logger.info("Save method call to save product with id:{}",product.getId());
        Product savedProduct = productRepository.save(product);
    	logger.info("kafka producer call to audit log for id:{}",product.getId());
        kafkaProducerService.sendCreateMessage(savedProduct);
        return savedProduct;
    }
    /**
     * This method is use to update existence data on DB and produce data to kafka topic 'product_update'
     * @param id
     * @param updatedProduct
     * @return
     */
    @Transactional
    public Product updateProduct(Long id, Product updatedProduct) {
    	logger.info("Update method call to update product with id:{}",updatedProduct.getId());
    	Product product = productRepository.findById(id).orElseThrow();
        product.setName(updatedProduct.getName());
        product.setDescription(updatedProduct.getDescription());
        Product savedProduct = productRepository.save(product);
    	logger.info("kafka producer call to audit log for update id:{}",updatedProduct.getId());
        kafkaProducerService.sendUpdateMessage(savedProduct);
        return savedProduct;
    }

    /**
     * This method is used to delete data from db and produce data to kafka topics 'product_delete'
     * @param id
     */
    @Transactional
    public void deleteProduct(Long id) {
    	logger.info("Delete method call for id:{}",id);
        productRepository.deleteById(id);
    	logger.info("kafka producer call to audit log for delete id:{}",id);
        kafkaProducerService.sendDeleteMessage(id);
    }
    
    /**
     * This method is responsible to check given id is present or not.
     * @param id
     * @return
     */
    @Transactional
    public Product getProductById(Long id) {
    	logger.info("Get method call for id:{}",id);
        return productRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Product not found with ID: " + id));
    }
}
