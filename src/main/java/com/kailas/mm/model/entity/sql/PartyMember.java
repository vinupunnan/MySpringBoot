package com.kailas.mm.model.entity.sql;

import com.kailas.mm.model.dto.PartyMemberDto;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "party_members")
public class PartyMember {
    @Id
    @Column(name= "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "member_code", unique = true)
    private String memberCode;
    private String name;

    public String getMemberCode() {
        return memberCode;
    }

    public void setMemberCode(String memberCode) {
        this.memberCode = memberCode;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    private String email;

    private int age;


    private String bloodGroup;

    private String qualification;

    @OneToMany(mappedBy = "partyMember", cascade = CascadeType.ALL)
    private List<Address> addresses = new ArrayList<>();


    @ManyToOne
    @JoinColumn(name = "role_id", insertable = false, updatable = false)
    private MemberRole role; // Reference to Role object

    private String userName;

    private String password;


    @OneToOne
    @JoinColumn(name = "role_id")
    private MemberRole memberRole;

    public PartyMember(PartyMemberDto partyMemberDto) {

        this.name =partyMemberDto.getName();
        this.memberCode =partyMemberDto.getEmpCode();
        this.password=partyMemberDto.getPassword();
        this.bloodGroup =partyMemberDto.getBloodGroup();
        this.email =partyMemberDto.getEmail();
        this.qualification =partyMemberDto.getQualifation();
        this.userName=partyMemberDto.getUserName();
        this.age = partyMemberDto.getAge();
        this.addresses = partyMemberDto.getAddresses().stream()
                .map(dto -> {
                    Address address = new Address(dto);
                    address.setPartyMember(this); // Set the PartyMember reference
                    return address;
                })
                .collect(Collectors.toList());

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }


    public void setId(Integer id) {
        this.id = id;
    }



    public MemberRole getMemberRole() {
        return memberRole;
    }

    public void setMemberRole(MemberRole memberRole) {
        this.memberRole = memberRole;
    }

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public List<Address> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<Address> addresses) {
        this.addresses = addresses;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public MemberRole getRole() {
        return role;
    }

    public void setRole(MemberRole role) {
        this.role = role;
    }
}
