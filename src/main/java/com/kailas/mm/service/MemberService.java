package com.kailas.mm.service;

import com.kailas.mm.model.dto.PartyMemberDto;
import com.kailas.mm.model.entity.sql.PartyMember;

import java.util.List;

public interface MemberService {
    public PartyMemberDto getMember(int memberId);
    public List<PartyMemberDto> getMemberList();

    public PartyMember registerNewMember(PartyMemberDto partyMemberDto);
}
