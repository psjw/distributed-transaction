package com.psjw.point.application;

import com.psjw.point.application.dto.PointReserveCommand;
import com.psjw.point.application.dto.PointReserveConfirmCommand;
import com.psjw.point.application.dto.PointReserveCancelCommand;
import com.psjw.point.domain.Point;
import com.psjw.point.domain.PointReservation;
import com.psjw.point.domain.PointReservation.PointReservationStatus;
import com.psjw.point.infrastructure.PointRepository;
import com.psjw.point.infrastructure.PointReservationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PointService {

    private final PointRepository pointRepository;

    private final PointReservationRepository pointReservationRepository;

    public PointService(PointRepository pointRepository,
            PointReservationRepository pointReservationRepository) {
        this.pointRepository = pointRepository;
        this.pointReservationRepository = pointReservationRepository;
    }

    @Transactional
    public void tryReserve(PointReserveCommand command) {
        PointReservation reservation = pointReservationRepository.findByRequestId(
                command.requestId());

        if(reservation != null) {
            System.out.println("이미 예약된 요청입니다.");
            return;
        }

        Point point = pointRepository.findByUserId(command.userId());

        point.reserve(command.reserveAmount());
        pointReservationRepository.save(
                new PointReservation(
                        command.requestId(),
                        point.getId(),
                        command.reserveAmount()
                )
        );
    }


    @Transactional
    public void confirmReserve(PointReserveConfirmCommand command) {
        PointReservation reservation = pointReservationRepository.findByRequestId(
                command.requestId());

        if(reservation == null) {
            throw new RuntimeException("예약 내역이 존재하지 않습니다.");
        }

        if(reservation.getStatus() == PointReservationStatus.CONFIRMED){
            System.out.println("이미 확정된 예약입니다.");
            return;
        }

        Point point = pointRepository.findById(reservation.getPointId()).orElseThrow();

        point.confirm(reservation.getReservedAmount());
        reservation.confirm();

        pointRepository.save(point);
        pointReservationRepository.save(reservation);
    }

    @Transactional
    public void cancelReserve(PointReserveCancelCommand command) {
        PointReservation reservation = pointReservationRepository.findByRequestId(
                command.requestId());

        if(reservation == null) {
            throw new RuntimeException("예약 내역이 존재ㅏ지 않습니다.");
        }

        if(reservation.getStatus() == PointReservationStatus.CANCELED){
            System.out.println("이미 취소된 예약입니다.");
            return;
        }

        Point point = pointRepository.findById(reservation.getPointId()).orElseThrow();

        point.cancel(reservation.getReservedAmount());
        reservation.cancel();

        pointRepository.save(point);
        pointReservationRepository.save(reservation);
    }
}
