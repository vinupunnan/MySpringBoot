package com.kailas.mm.dao;

import com.kailas.mm.model.dto.PartyMemberDto;
import com.kailas.mm.model.entity.sql.PartyMember;

public interface MemberDao {
    public PartyMemberDto getMember(int memberid);
    public PartyMember registerMember(PartyMemberDto payerMemberDto);

}
