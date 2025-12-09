package com.kailas.mm.model.entity.sql;

import jakarta.persistence.*;

@Entity
@Table(name = "alert_outbox")
public class AlertOutBox {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
   private String dcId;

    private String alertData;

    }
