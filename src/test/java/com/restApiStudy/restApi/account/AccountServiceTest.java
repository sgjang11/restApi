package com.restApiStudy.restApi.account;


import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Set;

import static org.assertj.core.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class AccountServiceTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Autowired
    AccountService accountService;

    //    @Autowired
    //    AccountRepository accountRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    @Test
    public void findByUsername() {
        // Given
        String password = "password";
        String username = "sgjang@gmail.com";
        Account account = Account.builder()
                .email(username)
                .password(password)
                .roles(Set.of(AcountRole.ADMIN, AcountRole.USER))
                .build();

        // 저장할 때 인코딩하고 저장할 수 있게 서비스에서 생성한 메서드 활용
        this.accountService.saveAccount(account); 
        //this.accountRepository.save(account); // 저장을 해줘야 확인 가능

        // When
        UserDetailsService userDetailsService = accountService;
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        // Then (password가 맞아야함)
        //assertThat(userDetails.getPassword()).isEqualTo(password);
        // 이제 인코딩된 패스워드를 일치시켜야함.
        assertThat(this.passwordEncoder.matches(password, userDetails.getPassword())).isTrue();

    }

    @Test
    public void findByUsername_Fail() {
//        String username = "random@email.com";
//        try {
//            accountService.loadUserByUsername(username);
//            fail("supposed to be failed");
//        } catch (UsernameNotFoundException e) {
//            //assertThat(e instanceof UsernameNotFoundException).isTrue();
//            assertThat(e.getMessage()).containsSequence(username);
//        }
        // Expected
        String username = "random@email.com";
        expectedException.expect(UsernameNotFoundException.class);
        expectedException.expectMessage(Matchers.containsString(username));

        // when
        accountService.loadUserByUsername(username);
    }

}