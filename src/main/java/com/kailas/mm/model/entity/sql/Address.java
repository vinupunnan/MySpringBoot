package com.kailas.mm.model.entity.sql;

import com.kailas.mm.model.dto.AddressDto;
import com.kailas.mm.model.entity.sql.PartyMember;
import jakarta.persistence.*;
@Entity
@Table(name = "member_address")
public class Address {
    @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name= "id")
    private int id;

    private String street;

    private String city;

    private String state;

    private String zipcode;

    private String country;

    public void setCountry(String country) {
        this.country = country;
    }

    public Address(AddressDto dto) {
        this.city =dto.getCity();
        this.state =dto.getState();
        this.zipcode=dto.getZipCode();
        this.country= dto.getCountry();
    }

    @ManyToOne
    @JoinColumn(name="member_id")
    private PartyMember partyMember;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public PartyMember getPartyMember() {
        return partyMember;
    }

    public void setPartyMember(PartyMember partyMember) {
        this.partyMember = partyMember;
    }
}
