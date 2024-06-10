package com.kailas.mm.dao.impl;

import com.kailas.mm.dao.MemberDao;
import com.kailas.mm.model.dto.PartyMemberDto;
import com.kailas.mm.model.entity.sql.PartyMember;
import com.kailas.mm.repository.MemberRepository;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;



@Component
public class MemberDaoImpl implements MemberDao {

    @Autowired
    MemberRepository  memberRepository;

    private final Timer memberInsertTimer;//TODO

    public MemberDaoImpl(MeterRegistry registry) {
        this.memberInsertTimer = Timer.builder("member.insert.time")
                .description("Time taken to insert an employee record")
                .register(registry);
    }
    @Override
    public PartyMemberDto getMember(int memberid) {
        return null;
    }

    @Override
    @Transactional
    public PartyMember registerMember(PartyMemberDto payerMemberDto) {
        PartyMember member = new PartyMember(payerMemberDto);
        PartyMember savedMember =  memberRepository.save(member);
        return savedMember;

    }
}
