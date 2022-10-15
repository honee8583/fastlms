package com.example.fastlms.member.service.impl;

import com.example.fastlms.admin.dto.MemberDto;
import com.example.fastlms.admin.mapper.MemberMapper;
import com.example.fastlms.admin.model.MemberParam;
import com.example.fastlms.component.MailComponents;
import com.example.fastlms.course.model.ServiceResult;
import com.example.fastlms.member.entity.Member;
import com.example.fastlms.member.exception.MemberEmailNotAuthenticatedException;
import com.example.fastlms.member.exception.MemberStopUserException;
import com.example.fastlms.member.model.MemberInput;
import com.example.fastlms.member.model.ResetPasswordInput;
import com.example.fastlms.member.repository.MemberRepository;
import com.example.fastlms.member.service.MemberService;
import com.example.fastlms.util.PasswordUtils;
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
                .userStatus(Member.MEMBER_STATUS_REQ)   // 가입요청상태
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

        member.setUserStatus(Member.MEMBER_STATUS_AVAILABLE);   // 사용가능으로 업데이트
        member.setEmailAuthYn(true);
        member.setEmailAuthDt(LocalDateTime.now());
        memberRepository.save(member);

        return true;
    }

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
    public MemberDto detail(String userId) {
        Optional<Member> optionalMember = memberRepository.findById(userId);
        if (!optionalMember.isPresent()) {
            return null;
        }

        Member member = optionalMember.get();

        return MemberDto.of(member);
    }

    @Override
    public boolean updateStatus(String userId, String userStatus) {
        Optional<Member> optionalMember = memberRepository.findById(userId);
        if (!optionalMember.isPresent()) {
            throw new UsernameNotFoundException("회원정보가 존재하지 않습니다.");
        }

        Member member = optionalMember.get();

        member.setUserStatus(userStatus);
        memberRepository.save(member);

        return true;
    }

    @Override
    public boolean updatePassword(String userId, String password) {
        Optional<Member> optionalMember = memberRepository.findById(userId);
        if (!optionalMember.isPresent()) {
            throw new UsernameNotFoundException("회원정보가 존재하지 않습니다.");
        }

        Member member = optionalMember.get();

        String encPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        member.setPassword(encPassword);
        memberRepository.save(member);

        return true;
    }

    @Override
    public ServiceResult updateMember(MemberInput memberInput) {
        String userId = memberInput.getUserId();

        Optional<Member> optionalMember =
                memberRepository.findById(userId);
        if (!optionalMember.isPresent()) {
            return new ServiceResult(false, "회원 정보가 존재하지 않습니다.");
        }

        Member member = optionalMember.get();

        member.setPhone(memberInput.getPhone());
        member.setZipcode(memberInput.getZipcode());
        member.setAddr(memberInput.getAddr());
        member.setAddrDetail(memberInput.getAddrDetail());
        member.setUpDt(LocalDateTime.now());
        memberRepository.save(member);

        return new ServiceResult(true);
    }

    @Override
    public ServiceResult updateMemberPassword(MemberInput memberInput) {

        String userId = memberInput.getUserId();
        Optional<Member> optionalMember =
                memberRepository.findById(userId);
        if (!optionalMember.isPresent()) {
            return new ServiceResult(false, "회원 정보가 존재하지 않습니다.");
        }

        Member member = optionalMember.get();

        if (!PasswordUtils.equals(memberInput.getPassword(), member.getPassword())) {
            return new ServiceResult(false, "비밀번호가 일치하지 않습니다.");
        }

        String encPassword = PasswordUtils.encPassword(memberInput.getPassword());
        member.setPassword(encPassword);
        memberRepository.save(member);

        return new ServiceResult(true);
    }

    @Override
    public ServiceResult withdraw(String userId, String password) {

        Optional<Member> optionalMember = memberRepository.findById(userId);
        if (!optionalMember.isPresent()) {
            return new ServiceResult(false, "회원정보가 존재하지 않습니다.");
        }

        Member member = optionalMember.get();

        if (!PasswordUtils.equals(password, member.getPassword())) {
            return new ServiceResult(false, "비밀번호가 일치하지 않습니다.");
        }

        member.setUserName("삭제회원");
        member.setPhone("");
        member.setPassword("");
        member.setRegDt(null);
        member.setUpDt(null);
        member.setEmailAuthYn(false);
        member.setEmailAuthDt(null);
        member.setEmailAuthKey("");
        member.setResetPasswordKey("");
        member.setResetPasswordLimitDt(null);
        member.setUserStatus(Member.MEMBER_STATUS_WITHDRAW);
        member.setZipcode("");
        member.setAddr("");
        member.setAddrDetail("");

        memberRepository.save(member);

        return new ServiceResult(true);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Member> optionalMember = memberRepository.findById(username);
        if (!optionalMember.isPresent()) {
            throw new UsernameNotFoundException("회원정보가 존재하지 않습니다.");
        }

        Member member = optionalMember.get();

        if (member.getUserStatus().equals(Member.MEMBER_STATUS_REQ)) {
            throw new MemberEmailNotAuthenticatedException("이메일 활성화 이후에 로그인해주세요.");
        }

        if (member.getUserStatus().equals(Member.MEMBER_STATUS_STOPPED)) {
            throw new MemberStopUserException("정지된 회원 입니다.");
        }

        if (member.getUserStatus().equals(Member.MEMBER_STATUS_WITHDRAW)) {
            throw new MemberStopUserException("탈퇴한 회원 입니다.");
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
