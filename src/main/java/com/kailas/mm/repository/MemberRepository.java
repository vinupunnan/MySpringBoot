package com.kailas.mm.repository;

import com.kailas.mm.model.entity.sql.PartyMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<PartyMember, Integer> {
    PartyMember save(PartyMember partyMember);

}
