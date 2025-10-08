package com.psjw.point.init;


import com.psjw.point.domain.Point;
import com.psjw.point.infrastructure.PointRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class TestDataCreator {
    private final PointRepository pointRepository;

    public TestDataCreator(PointRepository pointRepository) {
        this.pointRepository = pointRepository;
    }

    @PostConstruct
    public void createTestData(){
        pointRepository.save(new Point(1L, 10000L));
    }
}
