package com.restApiStudy.restApi.configs;

import com.restApiStudy.restApi.account.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;

// 이 설정을 해주면 더이상 시큐리티 기본 설정을 사용 못함 여기서 정의한 것만 사용가능
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    AccountService accountService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Bean
    public TokenStore tokenStore(){
        return new InMemoryTokenStore();
    };
    
    @Bean // 이걸 해줘야 다른 곳에서 참조 가능
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    // AuthenticationManager 설정 빈
    // userDetailsService는 내가 만든 accountService고,
    // passwordEncoder는 내가 만든 passwordEncoder다라고 재정의
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(accountService).passwordEncoder(passwordEncoder);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        // 정적인 resource들 무시 즉 favicon이나 docs의 경우
        // web으로 가는 것 중 스프링 시큐리티를 적용하지 않도록 하기 위한 것
        // 이건 docs에 관한 것 무시
        web.ignoring().mvcMatchers("/docs/index.html");
        // 이건 정적인 resource에 관한 모든 파일들 무시
        web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http.authorizeRequests()
//                .mvcMatchers("/docs/index.html").anonymous()
//                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).anonymous()
//        ;
//    }

/*
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 이것은 익명사용자 허용, formLogin 사용
        // 허용할 메서드는 get요청으로 /api/로 시작하는 모든 것은 익명사용자도 사용 가능
        // 나머지는 인증 필요
        http
                .anonymous()
                .and()
                .formLogin()
                .and()
                .authorizeRequests()
                // 테스트를 하기 위해 주석처리
                //.mvcMatchers(HttpMethod.GET, "/api/**").anonymous()
                .mvcMatchers(HttpMethod.GET, "/api/**").authenticated()
                .anyRequest().authenticated();
    }
*/

}
