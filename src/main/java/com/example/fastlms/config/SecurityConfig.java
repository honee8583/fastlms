package com.example.fastlms.config;

import com.example.fastlms.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final MemberService memberService;

    @Bean
    PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    UserAuthenticationFailureHandler getFailureHandler() {  // Bean으로 등록
        return new UserAuthenticationFailureHandler();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.csrf().disable();

        http.headers().frameOptions().sameOrigin(); // smartEditor용 설정

        http.authorizeRequests()    // 권한설정
                .antMatchers("/",
                        "/member/register",
                        "/member/register_complete",
                        "/member/email-auth",
                        "/member/find/password",
                        "/member/reset/password")
                .permitAll();    // 모든 접근 허용(로그인불필요)

        http.authorizeRequests()
                        .antMatchers("/admin/**")
                        .hasAuthority("ROLE_ADMIN");

        http.formLogin()
                .loginPage("/member/login")
                .failureHandler(getFailureHandler())   // 로그인 실패 케이스
                .permitAll();

        http.logout()
                .logoutRequestMatcher(
                        new AntPathRequestMatcher("/member/logout"))
                        .logoutSuccessUrl("/")  // 로그인 성공후 이동 페이지
                                .invalidateHttpSession(true);   // 세션 초기화

        http.exceptionHandling()
                .accessDeniedPage("/error/denied"); // 접근불가시 이동할 페이지

        super.configure(http);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(memberService)
                .passwordEncoder(getPasswordEncoder());

        super.configure(auth);
    }
}
