package com.assigment.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.assigment.model.AuditLog;
import com.assigment.model.Product;
import com.assigment.repository.AuditLogRepository;

import jakarta.transaction.Transactional;

/**
 * This class is kafka consumer class responsible to read data from kafka topics .
 */
@Service
public class KafkaConsumerService {


	 // Initialize the logger for this class
  private static final Logger logger = LoggerFactory.getLogger(KafkaConsumerService.class);
	
    @Autowired
    private AuditLogRepository auditLogRepository;

    /*
     * This method is responsible to read data from 'product_create' topics and store in audit table.
     */
	@KafkaListener(topics = "product_create", groupId = "group_id")
	@Transactional
	public void handleCreateEvent(Product product) {
		try {
			logger.info("recive consumer side for create audit with details:{}",product.toString());
			saveAuditLog("CREATE", product);

		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}

	}

	/**
     * This method is responsible to update data from 'product_updadte' topics and store in audit table.
	 * @param product
	 */
    @KafkaListener(topics = "product_update", groupId = "group_id")
    @Transactional
    public void handleUpdateEvent(Product product) {
    	logger.info("Received for product update log audit  product: " + product);
        saveAuditLog("UPDATE", product);
    }

    /**
     * This method is responsible to read data from 'product_delete' topics and store in audit table.
     * @param productId
     */
    @KafkaListener(topics = "product_delete", groupId = "group_id")
    @Transactional
    public void handleDeleteEvent(Long productId) {
    	logger.info("Delete for product update log audit  for productId: " + productId);
        saveAuditLog("DELETE", "Product ID: " + productId);
    }

    /**
     * This method is responsible to store data in audit table.
     * @param action
     * @param details
     */
    private void saveAuditLog(String action, Object details) {
        AuditLog auditLog = new AuditLog(action, details.toString());
        auditLogRepository.save(auditLog);
    }
}
