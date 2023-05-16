package com.restApiStudy.restApi.account;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Integer> {
    Optional<Account> findByEmail(String username); // null 리턴할 수 있어서 optional로 감쌈
}
