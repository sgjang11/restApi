package com.restApiStudy.restApi.configs;

import com.restApiStudy.restApi.account.Account;
import com.restApiStudy.restApi.account.AccountService;
import com.restApiStudy.restApi.account.AcountRole;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@Configuration
public class AppConfig {
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    // password encode 지정하는 빈
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    // 부트가 구동될 때 하나 생성해줌
    @Bean
    public ApplicationRunner applicationRunner() {
        return new ApplicationRunner() {

            @Autowired
            AccountService accountService;

            @Override
            public void run(ApplicationArguments args) throws Exception {
                Account test = Account.builder()
                        .email("sgjang@email.com")
                        .password("test")
                        .roles(Set.of(AcountRole.ADMIN, AcountRole.USER))
                        .build();
                accountService.saveAccount(test);
            }
        };
    }

}
