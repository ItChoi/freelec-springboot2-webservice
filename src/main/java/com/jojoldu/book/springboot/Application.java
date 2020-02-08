package com.jojoldu.book.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

// JPA Auditing 활성화
// @EnableJpaAuditing   // 테스트 사용 시 이것도 읽어 예외가 발생한다. 테스트 시에는 @Entity가 없는 상태에서 진행하지만, 이 애노테이션은 최소 하나의 @Entity가 있어야 한다. 따라서 테스트를 위해 분리 필요
// 스프링의 자동 설정, 스프링 Bean 읽기와 생성을 모두 자동으로 설정된다.
// @SpringBootApplication 위치부터 설정을 읽어가기 때문에 이 클래스는 항상 프로젝트 최상단에 위치해야 한다.
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        // SpringApplication.run으로 인해 내장 WAS 를 실행한다.
        // 내장 WAS란, 별도로 외부에 WAS를 두지 않고 애플리케이션을 실행할 때 내부에서 WAS를 실행하는 것을 야기한다.
        // 이렇게 되면 항상 서버에 톰캣을 설치할 필요가 없게 되고,
        // 스프링 부트로 만들어진 Jar파일(실행 가능한 JAva 패키징 파일)로 실행하면 된다.
        // 스프링 부트에서만 내장 WAS를 사용할 수 있는 것은 아니지만, 스부에서는 내장 WAS 사용을 권장하고 있다.
        // 내장 WAS 권장 이유는 '언제 어디서나 같은 환경에서 스프링 부트를 배포'할 수 있기 때문이다.
        // 외장 WAS 사용 시 모든 서버는 WAS의 종류와 버전, 설정을 일치시켜야 한다.
        SpringApplication.run(Application.class, args);

    }
}
