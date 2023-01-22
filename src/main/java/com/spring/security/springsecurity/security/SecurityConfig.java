package com.spring.security.springsecurity.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;


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
}
