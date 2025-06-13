package com.example.demo.global;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/*
================================================
[BaseTimeEntity]
 ├── createdAt (자동)
 └── updatedAt (자동)
       ↑
       │
[User] ──────── extends BaseTimeEntity
[Article] ───── extends BaseTimeEntity
[Comment] ───── extends BaseTimeEntity

=> createdAt, updatedAt은 공통으로 상속 + 자동 처리됨

===============================================
*/

@Getter
@MappedSuperclass   //이 클래스 자체에서 테이블이 되지 않고 자식 클래스에게 필드만 상속
@EntityListeners(AuditingEntityListener.class)  //엔티티의 생성/수정 이벤트 발생 시 자동으로 시간 주입
public class BaseTimeEntity {
    /*
    모든 엔티티에서 공통적으로 사용하는 생성시각(createdAt)과 수정 시각(updateAt)을 자동으로 관리하기 위한 추상 부모 클래스
    
    만든 이유
        1. 거의 모든 테이블에서 필요
        2. 매번 중복 선언하지 않고, 상속만으로 공통 필드 재사용
        3. JPA Auditing 기능으로 날짜가 자동으로 채워짐
     */
    
    @CreatedDate    //처음 저장 될때 자동으로 시간 기록
    @Column(updatable = false)//이후에 수정 불가
    private LocalDateTime createdAt;

    @LastModifiedDate   //수정될 때 마다 자동으로 시간 갱신
    private LocalDateTime updatedAt;
}
