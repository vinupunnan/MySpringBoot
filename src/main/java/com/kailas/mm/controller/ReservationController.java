package com.kailas.mm.controller;

import com.kailas.mm.common.BaseResponse;
import com.kailas.mm.model.dto.ItemDto;
import com.kailas.mm.model.dto.ReservationDto;
import com.kailas.mm.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/reservations")


public class ReservationController {

    @Autowired
    ReservationService reservationService;

    @PostMapping()
    public ResponseEntity<Integer> createNewReservation(@RequestBody ReservationDto reservationDto) {
        int id = reservationService.saveReservation(reservationDto);
        return new ResponseEntity<>(id, HttpStatus.OK);

    }

    @GetMapping()
    public ResponseEntity<List<ReservationDto>> getReservations() throws ParseException {
        List<ReservationDto> reservationDtoList = reservationService.getAllReservations();
        return new ResponseEntity<>(reservationDtoList, HttpStatus.OK);


    }
}
