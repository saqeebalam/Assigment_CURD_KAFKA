package com.assigment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.assigment.model.AuditLog;

/**
 * Spring data JPA responsible to perform CRUD operation for Audit.
 * @author salam
 *
 */

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
   

}
