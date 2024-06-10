package com.kailas.mm.model.entity.sql;

import jakarta.persistence.*;

@Entity
@Table(name = "member_roles")
public class MemberRole {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String roleName;

    private String roleDesc;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
