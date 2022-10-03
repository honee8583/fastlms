package com.example.fastlms.member.service;

import com.example.fastlms.member.model.MemberInput;
import com.example.fastlms.member.model.ResetPasswordInput;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface MemberService extends UserDetailsService {

    boolean register(MemberInput memberInput);

    boolean emailAuth(String uuid);

    boolean sendResetPassword(ResetPasswordInput resetPasswordInput);

    boolean resetPassword(String id, String password);

    boolean checkResetPassword(String uuid);
}
