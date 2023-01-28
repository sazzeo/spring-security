package com.spring.security.springsecurity.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;


@Configuration
@EnableWebSecurity //웹 보안 활성화
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        //인가 정책
        http
                .authorizeRequests()
                .anyRequest().authenticated();

        //인증 정책
        http
                .formLogin();

    }


    private void logout(HttpSecurity http) throws Exception {
        http.logout()
                .logoutUrl("/logout") //기본적으로 post 요청임
                .logoutSuccessUrl("/login")
                .deleteCookies("JSESSIONID" , "remember-me")
                .addLogoutHandler((request, response, authentication) -> {
                    //로그아웃 핸들러
                    HttpSession httpSession = request.getSession();
                    httpSession.invalidate(); // 세션 무효화 (세션 삭제 아님)
                })
                .logoutSuccessHandler((request, response, authentication) -> {
                    //로그아웃 성공 후 핸들러
                    response.sendRedirect("/login");
                });
    }

}
