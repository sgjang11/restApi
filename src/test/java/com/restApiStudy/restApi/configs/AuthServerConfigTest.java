package com.restApiStudy.restApi.configs;

import com.restApiStudy.restApi.account.Account;
import com.restApiStudy.restApi.account.AccountService;
import com.restApiStudy.restApi.account.AcountRole;
import com.restApiStudy.restApi.common.BaseControllerTest;
import com.restApiStudy.restApi.commons.AppProperties;
import com.restApiStudy.restApi.commons.TestDescription;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Set;

import static org.junit.Assert.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class AuthServerConfigTest extends BaseControllerTest {

    // 여기서 서버가 설정이 되었을 경우 토큰을 발급받아야함

    @Autowired
    AccountService accountService;

    @Autowired
    AppProperties appProperties;

    @Test
    @TestDescription("인증 토큰 발급 받는 테스트")
    public void getAuthToken() throws Exception {
        // Given
//        String username = "test@email.com";
//        String password = "test";
        // 부트가 실행될 때 새로 생성되기 때문에 필요없음
//        Account test = Account.builder()
//                .email(appProperties.getUserUsername())
//                .password(appProperties.getUserPassword())
//                .roles(Set.of(AcountRole.ADMIN, AcountRole.USER))
//                .build();
//        this.accountService.saveAccount(test);

//        String clientId = "myApp";
//        String clientSecret = "pass";

        // When & Then
        // post("/oauth/token") 하면 처리할 수 있는 것이 적용됨.
        this.mockMvc.perform(post("/oauth/token")
                        .with(httpBasic(appProperties.getClientId(), appProperties.getClientPassword()))
                        .param("grant_type", "password")
                        .param("username", appProperties.getUserUsername())
                        .param("password", appProperties.getUserPassword())
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("access_token").exists())
        ;
    }
}