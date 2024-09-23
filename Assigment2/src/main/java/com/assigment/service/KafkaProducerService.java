package com.assigment.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.assigment.model.Product;

import jakarta.transaction.Transactional;

/**
 * This class send message to its respective topics
 * @author salam
 *
 */
@Service
public class KafkaProducerService {

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    private static final String TOPIC_CREATE = "product_create";
    private static final String TOPIC_UPDATE = "product_update";
    private static final String TOPIC_DELETE = "product_delete";

    public void sendCreateMessage(Product product) {
        kafkaTemplate.send(TOPIC_CREATE, product);
    }

    public void sendUpdateMessage(Product product) {
        kafkaTemplate.send(TOPIC_UPDATE, product);
    }

    public void sendDeleteMessage(Long productId) {
        kafkaTemplate.send(TOPIC_DELETE, productId);
    }
}