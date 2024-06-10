package com.kailas.mm.service;

import com.kailas.mm.model.dto.ReservationDto;

import java.util.List;

public interface ReservationService {
    public int  saveReservation(ReservationDto reservationDto);
    public List<ReservationDto> getAllReservations();
}
