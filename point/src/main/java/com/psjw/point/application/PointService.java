package com.psjw.point.application;

import com.psjw.point.application.dto.PointUseCancelCommand;
import com.psjw.point.application.dto.PointUseCommand;
import com.psjw.point.domain.Point;
import com.psjw.point.domain.PointTransactionHistory;
import com.psjw.point.domain.PointTransactionHistory.TransactionType;
import com.psjw.point.infrastructure.PointRepository;
import com.psjw.point.infrastructure.PointTransactionHistoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PointService {
    private final PointTransactionHistoryRepository pointTransactionHistoryRepository;
    private final PointRepository pointRepository;

    public PointService(PointTransactionHistoryRepository pointTransactionHistoryRepository,
            PointRepository pointRepository) {
        this.pointTransactionHistoryRepository = pointTransactionHistoryRepository;
        this.pointRepository = pointRepository;
    }


    @Transactional
    public void use(PointUseCommand command) {
        PointTransactionHistory useHistory = pointTransactionHistoryRepository.findByRequestIdAndTransactionType(
                command.requestId(),
                TransactionType.USE);

        if(useHistory != null) {
            System.out.println("이미 사용한 이력이 존재합니다.");
            return;
        }

        Point point = pointRepository.findByUserId(command.userId());

        if(point == null) {
            throw new RuntimeException("포인트가 존재하지 않습니다.");
        }

        point.use(command.amount());
        pointTransactionHistoryRepository.save(new PointTransactionHistory(
                command.requestId(),
                point.getId(),
                command.amount(),
                TransactionType.USE
        ));
    }

    @Transactional
    public void cancel(PointUseCancelCommand command) {
        PointTransactionHistory useHistory = pointTransactionHistoryRepository.findByRequestIdAndTransactionType(
                command.requestId(),
                TransactionType.USE
        );

        if(useHistory == null) {
            return;
//            throw new RuntimeException("포인트 사용내역이 존재하지 않습니다.");
        }

        PointTransactionHistory cancelHistory = pointTransactionHistoryRepository.findByRequestIdAndTransactionType(
                command.requestId(), TransactionType.CANCEL);

        if(cancelHistory != null) {
            System.out.println("이미 취소된 요청입니다.");
            return;
        }

        Point point = pointRepository.findById(useHistory.getPointId()).orElseThrow();
        point.cancel(useHistory.getAmount());
        pointTransactionHistoryRepository.save(new PointTransactionHistory(
                command.requestId(),
                point.getId(),
                useHistory.getAmount(),
                TransactionType.CANCEL
        ));
    }

}
