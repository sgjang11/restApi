package com.restApiStudy.restApi.account;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Entity
@Getter @Setter @EqualsAndHashCode(of = "id")
@Builder @NoArgsConstructor @AllArgsConstructor
public class Account {

    @Id @GeneratedValue
    private Integer id;

    @Column(unique = true) // email은 unique가 되어야함
    private String email;

    private String password;

    // 여러개의 열거형을 가질 수 있어서 추가해줌
    // role 자체는 별로 없지만 항상 가져와야함으로 EAGER로 설정
    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(value = EnumType.STRING)
    private Set<AcountRole> roles;

}
