package com.kailas.mm.repository;

import com.kailas.mm.model.entity.sql.Reservation;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservationRepository extends
        JpaRepository<Reservation,Integer> {
   Reservation save(Reservation reservation);
   List<Reservation> findAll();
}
