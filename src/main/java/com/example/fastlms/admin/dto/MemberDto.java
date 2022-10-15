package com.example.fastlms.admin.dto;

import com.example.fastlms.member.entity.Member;
import lombok.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class MemberDto {
    private String userId;
    private String password;
    private String userName;
    private String phone;
    private LocalDateTime regDt;
    private LocalDateTime upDt;

    private String emailAuthKey;
    private boolean emailAuthYn;
    private LocalDateTime emailAuthDt;

    private String resetPasswordKey;
    private LocalDateTime resetPasswordLimitDt;

    private boolean adminYn;
    private String userStatus;

    private String zipcode;
    private String addr;
    private String addrDetail;

    // additional Column
    private long totalCount;    // 데이터 총 개수
    private long seq;           // 페이지내 번호

    public static MemberDto of(Member member) {
        return MemberDto.builder()
                .userId(member.getUserId())
                .userName(member.getUserName())
                .phone(member.getPhone())
//                .password(member.getPassword())
                .regDt(member.getRegDt())
                .upDt(member.getUpDt())
                .emailAuthYn(member.isEmailAuthYn())
                .emailAuthDt(member.getEmailAuthDt())
                .emailAuthKey(member.getEmailAuthKey())
                .resetPasswordKey(member.getResetPasswordKey())
                .resetPasswordLimitDt(member.getResetPasswordLimitDt())
                .adminYn(member.isAdminYn())
                .userStatus(member.getUserStatus())
                .zipcode(member.getZipcode())
                .addr(member.getAddr())
                .addrDetail(member.getAddrDetail())
                .build();
    }

    public String getRegDtText() {
        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        return regDt != null ? regDt.format(formatter) : "";
    }

    public String getUpDtText() {
        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        return upDt != null ? upDt.format(formatter) : "";
    }
}
