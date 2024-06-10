package com.kailas.mm.dao.impl;

import com.kailas.mm.dao.ReservationDao;
import com.kailas.mm.model.dto.ReservationDto;
import com.kailas.mm.model.entity.sql.Reservation;
import com.kailas.mm.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ReservationDaoImpl implements ReservationDao {
    @Autowired
    ReservationRepository reservationRepository;

    @Override
    public int saveReservation(ReservationDto reservationDto) {
        Reservation reservation = reservationRepository.save(new Reservation(reservationDto));
        return reservation.getReservationId();
    }

    @Override

    public List<ReservationDto> getAllReservations() {
        List<Reservation> reservationList = reservationRepository.findAll();
        List<ReservationDto> reservationDtoList = reservationList.stream().map(e -> new ReservationDto(e)).collect(Collectors.toList());
        return reservationDtoList;
    }
}
