package com.restApiStudy.restApi.account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AccountService implements UserDetailsService {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    // 이걸하는 이유는 저장을 할 때 password를 encode하지 않고 저장하기 때문에 일치하지 않아서
    // 저장하기 전 인코딩해서 저장해야함
    // 테스트를 할 때에도 accountRepository로 바로 저장하지 않고 인코딩하고 저장할 수 있게 설정해야함
    public Account saveAccount(Account account) {
        account.setPassword(this.passwordEncoder.encode(account.getPassword()));
        return this.accountRepository.save(account);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // email로 user를 찾을 때
        Account account = accountRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException(username)); // username이 없다면 에러를 던짐.

        // 그리고 account를 UserDetails 타입으로 변환해야 시큐리티가 알 수 있다.
        return new User(account.getEmail(), account.getPassword(), authorities(account.getRoles()));
    }

    private Collection<? extends GrantedAuthority> authorities(Set<AcountRole> roles) {
//        return roles.stream().map(role -> {
//            return  new SimpleGrantedAuthority("ROLE_" + role.name());
//        }).collect(Collectors.toSet()); 아래와 동일함
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.name()))
                .collect(Collectors.toSet());
    }
}
