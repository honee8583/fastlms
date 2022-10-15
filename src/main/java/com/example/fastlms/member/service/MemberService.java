package com.example.fastlms.member.service;

import com.example.fastlms.admin.dto.MemberDto;
import com.example.fastlms.admin.model.MemberParam;
import com.example.fastlms.course.model.ServiceResult;
import com.example.fastlms.member.entity.Member;
import com.example.fastlms.member.model.MemberInput;
import com.example.fastlms.member.model.ResetPasswordInput;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface MemberService extends UserDetailsService {

    boolean register(MemberInput memberInput);

    /**
     * uuid에 해당하는 계정을 활성화
     */
    boolean emailAuth(String uuid);

    /**
     * 입력한 이메일로 비밀번호 초기화 정보를 전송
     */
    boolean sendResetPassword(ResetPasswordInput resetPasswordInput);

    boolean resetPassword(String id, String password);

    boolean checkResetPassword(String uuid);

    /**
     * 회원 목록 리턴 (관리자)
     */
    List<MemberDto> list(MemberParam parameter);

    /**
     * 회원 상세 정보
     */
    MemberDto detail(String userId);

    /**
     * 회원 상태 변경
     */
    boolean updateStatus(String userId, String userStatus);

    /**
     * 회원 비밀번호 초기화
     */
    boolean updatePassword(String userId, String password);

    /**
     * 회원 정보 수정
     */
    ServiceResult updateMember(MemberInput memberInput);

    /**
     * 회원용 비밀번호 변경 기능
     */
    ServiceResult updateMemberPassword(MemberInput memberInput);

    /**
     * 회원 탈퇴 로직
     */
    ServiceResult withdraw(String userId, String password);
}
