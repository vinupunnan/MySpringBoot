package com.kailas.mm.service.impl;

import com.kailas.mm.dao.ReservationDao;
import com.kailas.mm.model.dto.ReservationDto;
import com.kailas.mm.service.ReservationService;
import jakarta.persistence.Column;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResrvationServiceImpl implements ReservationService {

    @Autowired
    ReservationDao reservationDao;

    public int saveReservation(ReservationDto reservationDto) {
   return reservationDao.saveReservation(reservationDto);

    }

    @Override
    public List<ReservationDto> getAllReservations() {
        return reservationDao.getAllReservations();
    }
}
