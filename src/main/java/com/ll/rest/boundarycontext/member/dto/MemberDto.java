package com.ll.rest.boundarycontext.member.dto;

import com.ll.rest.boundarycontext.member.entity.Member;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MemberDto {
    private Long id;
    private LocalDateTime regDate;
    private String username;

    public MemberDto(Member member) {
        this.id = member.getId();
        this.regDate = member.getCreateDate();
        this.username = member.getUsername();
    }

    public static MemberDto of(Member member) {
        return new MemberDto(member);
    }
}
