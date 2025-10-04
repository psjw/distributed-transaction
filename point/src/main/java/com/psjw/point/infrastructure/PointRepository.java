package com.psjw.point.infrastructure;

import com.psjw.point.domain.Point;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PointRepository extends JpaRepository<Point, Long> {
    Point findByUserId(Long userId);
}
