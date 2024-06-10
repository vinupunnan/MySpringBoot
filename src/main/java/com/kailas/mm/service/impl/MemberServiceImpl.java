package com.kailas.mm.service.impl;

import com.kailas.mm.exception.MemberExistsException;
import com.kailas.mm.model.entity.sql.PartyMember;
import org.springframework.stereotype.Service;


import com.kailas.mm.dao.MemberDao;
import com.kailas.mm.model.dto.PartyMemberDto;
import com.kailas.mm.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
@Service
public class MemberServiceImpl implements MemberService {

    @Autowired
    MemberDao memberDao;
    @Override
    public PartyMemberDto getMember(int memberId) {
        return null;
    }

    @Override
    public List<PartyMemberDto> getMemberList() {
        return null;
    }

    @Override
    public PartyMember registerNewMember(PartyMemberDto partyMemberDto) {
        //if (validatetheMemberExist(partyMemberDto)) {
            return memberDao.registerMember(partyMemberDto);
      //  } else {
        //  throw new MemberExistsException("The member with same EmailId" +partyMemberDto.getEmail() +"already exists");
      //  }
    }
        private boolean validatetheMemberExist (PartyMemberDto partyMemberDto){
            return false;
        }


    }