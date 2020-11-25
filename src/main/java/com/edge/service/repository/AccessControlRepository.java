package com.edge.service.repository;

import com.edge.service.models.AccessControl;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccessControlRepository extends JpaRepository<AccessControl, Long> {
    public AccessControl findByClientAndApi(String client, String api);
}
