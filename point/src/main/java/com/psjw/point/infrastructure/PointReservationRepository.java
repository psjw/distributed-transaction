package com.psjw.point.infrastructure;

import com.psjw.point.domain.PointReservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PointReservationRepository extends JpaRepository<PointReservation, Long> {
    PointReservation findByRequestId(String requestId);
}
