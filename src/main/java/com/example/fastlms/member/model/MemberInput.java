package com.example.fastlms.member.model;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberInput {
    private String userId;
    private String userName;
    private String password;
    private String phone;

    // 회원정보수정
    private String newPassword;
    private String zipcode;
    private String addr;
    private String addrDetail;
}
