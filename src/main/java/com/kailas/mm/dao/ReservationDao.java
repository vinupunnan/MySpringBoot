package com.kailas.mm.dao;

import com.kailas.mm.model.dto.ReservationDto;

import java.util.List;

public interface ReservationDao {
    public int saveReservation(ReservationDto reservationDto);
   public List<ReservationDto> getAllReservations();
}
