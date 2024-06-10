package com.kailas.mm.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kailas.mm.model.entity.sql.Reservation;

import java.util.Date;

public class ReservationDto {
    private int Id;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM-dd-yyyy")
    private Date checkInDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM-dd-yyyy")
    private Date checkoutDate;
    private String guestName;
    private String guestEmail;
    private int roomNumber;

    public ReservationDto(int id, Date checkInDate, Date checkoutDate, String guestName, String guestEmail, int roomNumber) {
        Id = id;
        this.checkInDate = checkInDate;
        this.checkoutDate = checkoutDate;
        this.guestName = guestName;
        this.guestEmail = guestEmail;
        this.roomNumber = roomNumber;
    }

    public ReservationDto() {
    }

    public ReservationDto(Reservation reservation) {
        this.Id =reservation.getReservationId();
        this.checkInDate = reservation.getCheckInDate();
        this.checkoutDate = reservation.getCheckOutDate();
        this.guestName = reservation.getGuestName();
        this.guestEmail = reservation.getGuestEmail();
        this.roomNumber = reservation.getRoomNumber();

    }


    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public Date getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(Date checkInDate) {
        this.checkInDate = checkInDate;
    }

    public Date getCheckoutDate() {
        return checkoutDate;
    }

    public void setCheckoutDate(Date checkoutDate) {
        this.checkoutDate = checkoutDate;
    }

    public String getGuestName() {
        return guestName;
    }

    public void setGuestName(String guestName) {
        this.guestName = guestName;
    }

    public String getGuestEmail() {
        return guestEmail;
    }

    public void setGuestEmail(String guestEmail) {
        this.guestEmail = guestEmail;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(int roomNumber) {
        this.roomNumber = roomNumber;
    }
}

