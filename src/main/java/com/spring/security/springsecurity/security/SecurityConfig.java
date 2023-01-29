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

        loginApi(http);
    }


    //Form Login 인증 API
    private void loginApi(HttpSecurity http) throws Exception {

        http.formLogin()
//                .loginPage("/loginPage") //사용자 정의 로그인 페이지
                .defaultSuccessUrl("/") // 성공시 이동 페이지
                .failureUrl("/login") //실패시 이동 페이지
                .usernameParameter("userId") // id 파라미터명
                .passwordParameter("password") // password 파라미터명
                .loginProcessingUrl("/login_proc") // form 요청 url
                .successHandler((request , response , authentication)-> {
                        System.out.println("성공!");
                        response.sendRedirect("/");
                })
                .failureHandler((request , response , exception)-> {
                        System.out.println("실패");
                        response.sendRedirect("/login");
                })// 로그인 실패후 핸들러
                .permitAll();  //로그인 페이지 permit 뚫어주기
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
