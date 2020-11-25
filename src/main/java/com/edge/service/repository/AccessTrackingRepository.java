package com.edge.service.repository;

import com.edge.service.models.AccessControl;
import com.edge.service.models.AccessTracking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccessTrackingRepository extends JpaRepository<AccessTracking, Long> {
    AccessTracking findByClientAndApi(String client, String api);
}
