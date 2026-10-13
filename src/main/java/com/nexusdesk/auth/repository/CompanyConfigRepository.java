package com.nexusdesk.auth.repository;

import com.nexusdesk.auth.entity.CompanyConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyConfigRepository extends JpaRepository<CompanyConfig, Short> {
}
