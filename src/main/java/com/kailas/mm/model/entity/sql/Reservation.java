package com.kailas.mm.model.entity.sql;

import com.kailas.mm.model.dto.AddressDto;
import com.kailas.mm.model.dto.ReservationDto;
import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "reservation")

public class Reservation {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer reservationId;

    private Date checkInDate;
    private Date checkOutDate;
    private String guestName;
    private String guestEmail;
    private Integer roomNumber;

    public Reservation() {
    }

    public Reservation(ReservationDto reservationDto) {
        this.checkInDate =reservationDto.getCheckInDate();
        this.checkOutDate =reservationDto.getCheckoutDate();
        this.guestName =reservationDto.getGuestName();
        this.guestEmail =reservationDto.getGuestEmail();
        this.roomNumber =reservationDto.getRoomNumber();
    }


    public Integer getReservationId() {
        return reservationId;
    }

    public void setReservationId(Integer reservationId) {
        this.reservationId = reservationId;
    }

    public Date getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(Date checkInDate) {
        this.checkInDate = checkInDate;
    }

    public Date getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(Date checkOutDate) {
        this.checkOutDate = checkOutDate;
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

    public Integer getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(Integer roomNumber) {
        this.roomNumber = roomNumber;
    }
}
