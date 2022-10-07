package com.example.fastlms.member.service;

import com.example.fastlms.admin.dto.MemberDto;
import com.example.fastlms.admin.model.MemberParam;
import com.example.fastlms.member.entity.Member;
import com.example.fastlms.member.model.MemberInput;
import com.example.fastlms.member.model.ResetPasswordInput;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface MemberService extends UserDetailsService {

    boolean register(MemberInput memberInput);

    boolean emailAuth(String uuid);

    boolean sendResetPassword(ResetPasswordInput resetPasswordInput);

    boolean resetPassword(String id, String password);

    boolean checkResetPassword(String uuid);

    List<MemberDto> list(MemberParam parameter);

    MemberDto detail(String userId);

    boolean updateStatus(String userId, String userStatus);

    boolean updatePassword(String userId, String password);
}
