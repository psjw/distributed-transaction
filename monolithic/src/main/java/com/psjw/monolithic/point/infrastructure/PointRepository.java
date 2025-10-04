package com.psjw.monolithic.point.infrastructure;

import com.psjw.monolithic.point.domain.Point;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PointRepository extends JpaRepository<Point, Long> {
    Point findByUserId(Long userId);
}
