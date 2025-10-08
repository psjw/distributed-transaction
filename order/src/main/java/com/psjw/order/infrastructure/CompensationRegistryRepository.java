package com.psjw.order.infrastructure;

import com.psjw.order.domain.CompensationRegistry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompensationRegistryRepository extends JpaRepository<CompensationRegistry, Long> {

}
