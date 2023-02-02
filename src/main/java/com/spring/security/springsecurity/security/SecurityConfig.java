package com.spring.security.springsecurity.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;

import javax.servlet.http.HttpSession;


@Configuration
@EnableWebSecurity //웹 보안 활성화
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        //인가 정책
        http
                .authorizeRequests()
                .anyRequest().authenticated();

        loginApi(http);
        logout(http);
        rememberMe(http);
    }


    /**
     * Form Login 인증 API
     * @param http http
     * @throws Exception Exception
     */
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

    /**
     * Form Logout Api
     * @param http http
     * @throws Exception Exception
     */
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

    /**
     * 자동로그인 기능 : RememberMe
     * 인증 객체가 없을 때만 동작함
     * @param http http
     * @throws Exception Exception
     */
    private void rememberMe(HttpSecurity http) throws Exception {
        http.rememberMe()
                .rememberMeParameter("remember") //기본 파라미터명은 remember-me
                .tokenValiditySeconds(3600) // 토큰 유효시간(3600초) , 기본은 14일
                .alwaysRemember(true) //체킹하지 않아도 리멤버 미 항상 실행
                .userDetailsService(userDetailsService);  //사용자 계정을 관리하는 클래스(* 반드시 설정해야함)

    }


}
