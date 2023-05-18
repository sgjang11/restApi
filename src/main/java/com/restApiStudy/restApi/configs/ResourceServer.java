package com.restApiStudy.restApi.configs;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;

@Configuration
@EnableResourceServer
public class ResourceServer extends ResourceServerConfigurerAdapter {

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.resourceId("event");
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.anonymous() //익명 허용
                .and()
                .authorizeRequests()
                //mvc 패턴으로 get으로 들어오는 /api/에 대한 모든것
                .mvcMatchers(HttpMethod.GET, "/api/**")
                //.anonymous()             // 이걸하면 인증안한 사람만 사용가능해짐
                .permitAll()               // 이게 모두 허용임
                .anyRequest()              // 그 밖의 다른 요청들은
                .authenticated()           // 인증이 필요함
                .and()
                .exceptionHandling() // 인증이 잘못되었거나 권한이 없는 경우 예외 발생
                // 그 중에 접근 권한이 없는 아이는 요 예외 를 줄거임( 403으로 응답 보냄)
                .accessDeniedHandler(new OAuth2AccessDeniedHandler())
        ;

    }
}
