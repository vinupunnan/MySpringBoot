package com.kailas.mm.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kailas.mm.common.BaseResponse;
import com.kailas.mm.model.dto.PartyMemberDto;
import com.kailas.mm.model.entity.sql.PartyMember;
import com.kailas.mm.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/member")
public class MemberController {

    @Autowired
    MemberService memberService;

//    @Autowired
//    PasswordEncoder passwordEncoder;

    @GetMapping("/memberId")
    public ResponseEntity<PartyMemberDto> getEmployee(@RequestParam Integer memberId) {
        PartyMemberDto payerMemberDto = memberService.getMember(memberId);
        return new ResponseEntity<>(payerMemberDto, HttpStatus.OK);
    }

    //http://localhost:8080/member/membercode?membercode=A001
    @GetMapping("/membercode")
    public ResponseEntity<PartyMemberDto> getMember(@RequestParam  String memberCode) throws JsonProcessingException {
        // beanCreation.printGreeting();
        PartyMemberDto partyMemberDto = new PartyMemberDto();
        partyMemberDto.setEmpCode(memberCode);
       // PartyMemberDto payerMemberDto = memberService.getMember(memberId);
        return new ResponseEntity<>(partyMemberDto, HttpStatus.OK);

    }

    @PostMapping("/register")
    public ResponseEntity<String> registerNewMember(@RequestBody PartyMemberDto memberDto) {
        System.out.println("In my controller");
       // memberDto.setPassword(passwordEncoder.encode(memberDto.getPassword()));
        PartyMember member = memberService.registerNewMember(memberDto);
        return new ResponseEntity<>(member.getName(),HttpStatus.OK);

    }

    @PostMapping("/upload")
    public ResponseEntity<String> handleFileUpload(@RequestParam("file") MultipartFile file) {
        System.out.println("Uploaded File");
        return new ResponseEntity<>(HttpStatus.OK);
    }
}