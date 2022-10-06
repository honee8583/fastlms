package com.example.fastlms.member.service.impl;

import com.example.fastlms.admin.dto.MemberDto;
import com.example.fastlms.admin.mapper.MemberMapper;
import com.example.fastlms.admin.model.MemberParam;
import com.example.fastlms.component.MailComponents;
import com.example.fastlms.member.entity.Member;
import com.example.fastlms.member.exception.MemberEmailNotAuthenticatedException;
import com.example.fastlms.member.model.MemberInput;
import com.example.fastlms.member.model.ResetPasswordInput;
import com.example.fastlms.member.repository.MemberRepository;
import com.example.fastlms.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final MailComponents mailComponents;

    private final MemberMapper memberMapper;

    /**
     * 회원 가입
     */
    @Override
    public boolean register(MemberInput parameter) {
        Optional<Member> result =
                memberRepository.findById(parameter.getUserId());
        if (result.isPresent()) {
            // 현재 해당 아이디의 회원이 존재.
            return false;
        }

        String encPassword =
                BCrypt.hashpw(parameter.getPassword(), BCrypt.gensalt());

        String uuid = UUID.randomUUID().toString();

        Member member = Member.builder()
                .userId(parameter.getUserId())
                .userName(parameter.getUserName())
                .password(encPassword)
                .phone(parameter.getPhone())
                .regDt(LocalDateTime.now())
                .emailAuthYn(false)
                .emailAuthKey(uuid)
                .build();
        memberRepository.save(member);

        String email = parameter.getUserId();
        String subject = "fastlms 사이트 가입을 축하드립니다.";
        String text = "<p>fastlms 사이트 가입을 축하드립니다.</p>" +
                        "<p>아래 링크를 클릭하셔서 가입을 완료하세요</p>" +
                        "<div><a href='http://localhost:8080/member/email-auth?id=" + uuid + "'>" +
                        "회원가입" +
                        "</a></div>";
        mailComponents.sendMail(email, subject, text);

        return true;
    }

    /**
     * uuid에 해당하는 계정을 활성화
     */
    @Override
    public boolean emailAuth(String uuid) {

        Optional<Member> optionalMember = memberRepository.findByEmailAuthKey(uuid);
        if (!optionalMember.isPresent()) {
            return false;
        }

        Member member = optionalMember.get();

        // 이미 활성화 한적이 있는 경우
        if (member.isEmailAuthYn()) {
            return false;
        }

        member.setEmailAuthYn(true);
        member.setEmailAuthDt(LocalDateTime.now());
        memberRepository.save(member);

        return true;
    }

    /**
     * 입력한 이메일로 비밀번호 초기화 정보 메일을 전송
     */
    @Override
    public boolean sendResetPassword(ResetPasswordInput resetPasswordInput) {
        Optional<Member> optionalMember =
                memberRepository.findByUserIdAndUserName(
                        resetPasswordInput.getUserId(),
                        resetPasswordInput.getUserName());
        if (!optionalMember.isPresent()) {
            throw new UsernameNotFoundException("회원정보가 존재하지 않습니다.");
        }

        Member member = optionalMember.get();

        String uuid = UUID.randomUUID().toString();

        member.setResetPasswordKey(uuid);
        member.setResetPasswordLimitDt(LocalDateTime.now().plusDays(1));
        memberRepository.save(member);

        String email = resetPasswordInput.getUserId();
        String subject = "[fastlms] 비밀번호 초기화 메일 입니다.";
        String text = "<p>fastlms 비밀번호 초기화 메일입니다.</p>" +
                "<p>아래 링크를 클릭하셔서 비밀번호를 초기화 해주세요.</p>" +
                "<div>" +
                    "<a href='http://localhost:8080/member/reset/password?id=" + uuid + "'>" +
                        "비밀번호 초기화 링크" +
                    "</a></div>";
        mailComponents.sendMail(email, subject, text);

        return true;
    }

    /**
     * 입력받은 uuid에 대해서 password로 초기화 진행
     */
    @Override
    public boolean resetPassword(String id, String password) {
        Optional<Member> optionalMember =
                memberRepository.findByResetPasswordKey(id);
        if (!optionalMember.isPresent()) {
            throw new UsernameNotFoundException("회원정보가 존재하지 않습니다.");
        }

        Member member = optionalMember.get();

        // 초기화 날짜가 유효한지 검사
        if (member.getResetPasswordLimitDt() == null) {
            throw new RuntimeException("초기화 가능한 시간이 지났습니다.");
        }

        if (member.getResetPasswordLimitDt().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("초기화 가능한 시간이 지났습니다.");
        }

        String encPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        member.setPassword(encPassword);
        member.setResetPasswordKey(""); // resetKey 초기화
        member.setResetPasswordLimitDt(null);   // resetDate 초기화
        memberRepository.save(member);

        return true;
    }

    /**
     * 입력받은 uuid 유효성 검사
     */
    @Override
    public boolean checkResetPassword(String uuid) {
        Optional<Member> optionalMember =
                memberRepository.findByResetPasswordKey(uuid);
        if (!optionalMember.isPresent()) {
            return false;
        }

        Member member = optionalMember.get();

        // 초기화 날짜가 유효한지 검사
        if (member.getResetPasswordLimitDt() == null) {
            throw new RuntimeException("초기화 가능한 시간이 지났습니다.");
        }

        if (member.getResetPasswordLimitDt().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("초기화 가능한 시간이 지났습니다.");
        }

        return true;
    }

    /**
     * 회원 목록 리턴 (관리자에서만 사용가능)
     */
    @Override
    public List<MemberDto> list(MemberParam parameter) {

        long totalCount = memberMapper.selectListCount(parameter);  // 검색결과개수
        List<MemberDto> list = memberMapper.selectList(parameter);

        if (!CollectionUtils.isEmpty(list)) {
            int i = 0;
            for (MemberDto x: list) {
                x.setTotalCount(totalCount);
                x.setSeq(totalCount - parameter.getPageStart() - i);
                i++;
            }
        }

        return list;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Member> optionalMember = memberRepository.findById(username);
        if (!optionalMember.isPresent()) {
            throw new UsernameNotFoundException("회원정보가 존재하지 않습니다.");
        }

        Member member = optionalMember.get();

        if (!member.isEmailAuthYn()) {
            throw new MemberEmailNotAuthenticatedException("이메일 활성화 이후에 로그인해주세요.");
        }

        List<GrantedAuthority>  grantedAuthorityList = new ArrayList<>();
        grantedAuthorityList.add(new SimpleGrantedAuthority("ROLE_USER"));

        if (member.isAdminYn()) {   // 관리자 권한 추가
            grantedAuthorityList.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        }

        // 아이디, 비밀번호, 권한(Collection<GrantedAuthority>)
        return new User(member.getUserId(),
                member.getPassword(),
                grantedAuthorityList);
    }
}
