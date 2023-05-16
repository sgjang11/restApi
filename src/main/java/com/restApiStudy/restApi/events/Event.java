package com.restApiStudy.restApi.events;

import com.restApiStudy.restApi.account.Account;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
@Getter @Setter
@EqualsAndHashCode(of = "id")
@Builder @NoArgsConstructor @AllArgsConstructor
@Entity
public class Event {
    @Id @GeneratedValue
    private Integer id;
    private String name;
    private String description;
    private LocalDateTime beginEnrollmentDateTime;
    private LocalDateTime closeEnrollmentDateTime;
    private LocalDateTime beginEventDateTime;
    private LocalDateTime endEventDateTime;
    private String location; // (optional) 이게 없으면 온라인 모임
    private int basePrice; // (optional)
    private int maxPrice; // (optional)
    private int limitOfEnrollment;
    private boolean offline;
    private boolean free;
    @Enumerated(EnumType.STRING) // ORDINAL이 디폴트값 이건 순서대로 번호 부여 String으로 주는 것이 좋음 이유는 데이터가 꼬일 수 있음
    private EventStatus eventStatus = EventStatus.DRAFT;

    // 단방향으로 설정
    @ManyToOne
    private Account manager;

    public void update() {
        // update free 무료인지 유료인지 확인
        if (this.basePrice == 0 && this.maxPrice == 0) {
            this.free = true;
        } else {
            this.free = false;
        }
        // update offline 비어있는지 확인
        if (this.location == null || this.location.isBlank()) {
            this.offline = false;
        } else {
            this.offline = true;
        }
    }
}
