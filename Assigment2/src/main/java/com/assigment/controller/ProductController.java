package com.assigment.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.assigment.model.Product;
import com.assigment.service.KafkaProducerService;
import com.assigment.service.ProductService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@RestController
@RequestMapping("/products")
/**
 * This is class is as Spring MVC controller responsible to handling  http request to perform CURD operation.
 * @author salam
 *  
 */
public class ProductController {

	 // Initialize the logger for this class
    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

	
    @Autowired
    private ProductService productService;

    @Autowired
    private KafkaProducerService kafkaProducerService;

    /**
     * This method is Post method used to create or entry new data. 
     * @param product
     * @return
     */
    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
    	logger.info("Creating product: {}", product);
    	Product savedProduct = productService.createProduct(product);
        kafkaProducerService.sendCreateMessage(savedProduct);  // Send event to Kafka topic
        logger.info("Product created and sent to Kafka: {}", savedProduct);
        return ResponseEntity.ok(savedProduct);
    }


    /**
     * This method is Get method used to search data with id.
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
    	logger.info("Serchning product with id: {}",id);
        Product product = productService.getProductById(id);
        logger.info("Product found with data:{}",product.toString());
        return ResponseEntity.ok(product);
    }

    /**
     * This is Put Method  used to update the data . 
     * @param id
     * @param is product
     * @return
     */
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product product) {
    	logger.info("Product to be updated with id: {} details: {}", id, product.toString());
    	Product updatedProduct = productService.updateProduct(id, product);
    	logger.info("Product updated with :{}",updatedProduct.toString());
        kafkaProducerService.sendUpdateMessage(updatedProduct);  // Send update event to Kafka
        return ResponseEntity.ok(updatedProduct);
    }

    /**
     * This is Delete method used to delete data by using id.
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
    	logger.info("Request recived for deleting order with id:{}",id);
        productService.deleteProduct(id);
        kafkaProducerService.sendDeleteMessage(id);  // Send delete event to Kafka
        return ResponseEntity.noContent().build();
    }
}