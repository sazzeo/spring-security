package com.example.chapter1.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;

import javax.servlet.http.HttpSession;

//@Configuration
//@EnableWebSecurity
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
        sessionManagement(http);
    }


    /**
     * Form Login 인증 API
     *
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
//                .successHandler((request , response , authentication)-> {
//                        System.out.println("성공!");
//                        response.sendRedirect("/");
//                })
//                .failureHandler((request , response , exception)-> {
//                        System.out.println("실패");
//                        response.sendRedirect("/login");
//                })// 로그인 실패후 핸들러
                .permitAll();  //로그인 페이지 permit 뚫어주기
    }

    /**
     * Form Logout Api
     *
     * @param http http
     * @throws Exception Exception
     */
    private void logout(HttpSecurity http) throws Exception {
        http.logout()
                .logoutUrl("/logout") //기본적으로 post 요청임
                .logoutSuccessUrl("/login")
                .deleteCookies("JSESSIONID", "remember-me")
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
     *
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


    /**
     * session 설정
     * @param http http
     * @throws Exception Exception
     */
    private void sessionManagement(HttpSecurity http) throws Exception {

        http
                /*
                 * 동시 로그인 제어 정책
                 */
                .sessionManagement()
                .maximumSessions(1)//최대 세션 개수 , -1 : 세션 제한 안함
                .maxSessionsPreventsLogin(true) //true: 동시 로그인 차단하기, false(default)시 이전 세션 만료시킴
//                .expiredUrl("/expired")//세션이 만료된 경우 이동할 페이지
                .and()

                /*
                 * changeSessionId : 기본
                 * none : 체인지 안함 (보안 위험)
                 * migrateSession
                 * newSession
                 */
                //세션 고정 보호 정책
                .sessionFixation().changeSessionId()  //세션 체인지

                /*
                 * Always : 항상 세션 생성
                 * If_required : 필요시 생성 (기본 값)
                 * Never : 시큐리티가 생성하지 않지만 있으면 사용
                 * Stateless : 세션 끄기 (ex: jwt 사용시)
                 */
                //세션 생성정책
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED);
    }


}