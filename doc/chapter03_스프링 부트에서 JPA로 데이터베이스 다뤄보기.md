## 스프링 부트에서 JPA로 데이터베이스 다뤄보기
- JPA는 ORM이다. (Object Relational Mapping)
- MyBatis, Ibatis는 ORM이 아니라, SQL Mapper다.
- ORM은 객체를 매핑하는 것, SQL Mapper는 쿼리를 매핑한다.

### JPA 소개
- 현대의 웹 어플리케이션에서 관계형 데이터베이스는 빠질 수 없는 요소이다. 따라서 객체를 객체를 관계형 데이터베이스에서 관리하는 것은 무엇보다 중요하다.
- RDB가 계속해서 웹 서비스의 중심이 되면서 모든 코드는 SQL 중심이 되어 갔다. 따라서 현업 플젝 대부분이 애플리케이션 코드보다는 SQL로 가득하게 됐다.
- ORM과 SQL Mapper는 패러다임이 다르다. ORM은 메시지를 기반으로 기능과 속성을 한 곳에 관리하지만, SQL Mapper의 경우 어떻게 데이터를 저장할지에 초점을 둔다.

#### SpringData JPA
- JPA는 인터페이스로서 자바 표준 명세서이다. 이를 사용하기 위해서는 구현체가 필요한데, 대표적으로 Hibernate, Dclipse Link 등이 있다. 하지만 Spring에서 JPA를 사용할 때는 이 구현체들을 직접 다루진 않는다.
- 스프링은 구현체들을 좀 더 쉽게 사용하고자 추상화시킨 Spring Data JPA라는 모듈을 이용하여 JPA 기술을 다룬다.
- 이들의 관계를 보면, JPA <-- Hibernate <-- Spring Data JPA
- Hibernate를 쓰는 것과 Spring Data JPA를 쓰는 것은 큰 차이가 없다. 그럼에도 스프링 진영에서는 Spring Data JPA를 개발했고, 이를 권장하고 있다.
- Spring Data JPA 등장 이유는 크게 두 가지다.
  1. 구현체 교체의 용이성
    - Hibernate 외에 다른 구현체로 쉽게 교체하기 위함
    - 새로운 JPA 구현체가 대세로 됐을 때 Spring Data JPA 사용 시 쉽게 교체 가능, 왜냐하면 SDJ 내부에서 구현체 매핑을 지원해주기 때문이다.
  2. 저장소 교체의 용이성
    - 괸계형 데이터베이스 외에 다른 저장소로 쉽게 교체하기위함
      - 점점 트래픽이 많아져 관계형 데이터베이스로 도저히 감당이 안될 때, 예를 들어 MongoDB 교체가 필요하다면, 개발자는 SDJ에서 Spring Data MongoDB로 의존성만 교체하면 된다. 이는 Spring Data의 하위 프로젝트들은 기본적인 CRUD의 인터페이스가 같기 때문이다.
   


#### 실무에서 JPA
- 실무에서 JPA를 사용하지 못하는 가장 큰 이유는 높은 러닝 커브를 야기한다. JPA를 잘 쓰기 위해서는 객체지향 프로그래밍과 관계형 데이터베이스를 둘 다 이해해야 한다.
- 둘 다 이해 해야하는 만큼 보상이 크다. CURD 쿼리를 직접 작성 할 필요가 없고, 부모-자식 관계의 표현, 1:N 관계 표현, 상태와 행위를 한 곳에서 관리하는 등 객체지향적으로 할 수 있다.
- 속도이슈는, JPA에서는 여러 성능 이슈 해결책들을 이미 준비해놓았기 때문에, 잘 활용하면 네이티브 쿼리만큼의 퍼포먼스를 낼 수 있다.

#### 요구사항 분석
- 게시판 기능
  - 게시글 조회
  - 게시글 등록
  - 게시글 수정
  - 게시글 삭제

  
- 회원 기능
  - 구글 / 네이버 로그인
  - 로그인한 사용자 글 작성 권한
  - 본인 작성 글에 대한 권한 관리
  
  
### 프로젝트에 Spring Data JPA 적용하기
- build.gradle에 의존성 추가
  - org.springframework.boot:spring-boot-starter-data-jpa
  - com.h2.database:h2  
- 도메인 패키지
  - 도메인을 담을 패키지
  - 소프트웨어에 대한 요구사항 혹은 문제 영역
  - 기존 MyBatis 사용 시 xml에 쿼리를 담고, 클래스는 오로지 쿼리의 결과만 담던 일들이 모두 도메인 클래스라고 불리는 곳에서 해결된다.
- 애노테이션
  - @Entity
     - 테이블과 링크될 클래스임을 나타낸다
     - 기본 값으로 클래스의 카멜케이스 이름을 언더스코어 네이밍(_)으로 테이블 이름을 매칭한다.
  - @Id
    - 해당 테이블의 PK 필드를 나타낸다.
  - @GeneratedValue
    - PK의 생성 규칙을 나타낸다.
    - 스프링 부트 2.0 에서는 GenerationType.IDENTITY 옵션을 추가해야만 auto_increment가 된다.
    - 스프링 부트 2.0버전과 1.5 버전 차이 [https://jojoldu.tistory.com/295](https://jojoldu.tistory.com/295 "asd")
  - @Column
    - 테이블의 컬럼을 나타내며, 선언하지 않더라도 해당 클래스의 필드는 모두 컬럼이 된다.
    - 사용 이유는 기본 값 외에 추가로 옵션이 필요한 경우 사용
  - 롬복 어노테이션
    - 서비스 초기 구축 단계에선 요구사항이 빈번하게 변경되는데, 이때 롬복의 어노테이션들을 사용하면 코드 변경량을 최소화 시켜준다.
    - @NoArgsConstructor
      - 기본 생성자 자동 추가
    - @Builder
      - 해당 클래스의 빌더 패턴 클래스 생성
      - 생성자 상단에 선언 시 생성자에 포함된 필드만 빌더에 포함
    - @RequiredArgsConstructor
      - final이선언된 모든 필드를 인자 값으로 하는 생성자를 롬복의 @RequiredArgsConstructor가 대신 생성하여 스프링의 @Autowired를 사용할 필요가 없다.
  - 테스트 어노테이션
    - @After
      - JUnit에서 단위 테스트가 끝날 때마다 수행되는 메소드를 지정
      - 보통 배포 전 전체 테스트를 수행할 때 테스트간 데이터 침범을 막기 위해 사용한다.
      - 여러 테스트가 동시에 수행되면 테스트용 ㅔ이터베이스인 H2에 데이터가 그대로 남아 있어 다음 테스트 실행 시 테스트가 실패할 수 있다.
  
  
- 꿀팁
  - 웬만하면 Entity의 PK는 Long타입 및 Auto_increment를 추천한다.
  - 주민등록번호와 같이 비즈니스상 유니크 키나, 여러 키를 조합한 복합키로 PK를 잡을 경우 난감한 상황이 종종 발생한다.
    1. FK를 맺을 때 다른 테이블에서 복합키 전부를 갖고 있거나, 중간 테이블을 하나 더 둬야 하는 상황 발생
    2. 인덱스에 좋은 영향을 끼치지 못한다.
    3. 유니크한 조건이 변경될 경우 PK 전체를 수정해야 하는 일이 발생
  - 주민등록번호, 복합키 등은 유니크 키로 별도로 추가하는 것을 추천
   
- 클래스에 자바빈 규약을 생각하며 getter/setter를 무작적 생성하는 경우가 많은데, 이는 해당 클래스의 인스턴스 값들이 언제 어디서 변해야 하는지 코드상으로 명확하게 구분할 수가 없어서 차후 기능 변경 시 정말 복잡해진다.
  - 따라서 @Entity 클래스에서는 절대 Setter 메소드를 만들지 않는다. 대신 해당 필드의 값 변경이 필요하면 명확히 그 목적과 의도를 나타낼 수 있는 메소드를 추가해야 한다.
    - ex) setStatus(false) 하면 안되고, changeStatus() -> this.status = false; 이런 방식으로 사용
  - Setter가 없는 상황에서 어떻게 값을 채워 DB에 삽입할까?
    - 기본적인 구조는 생성자를 통해 최종 값을채운 후 DB에 삽입한다.
    - 이 책에서는 생성자 대신, @Builder를 통해 제공되는 빌더 클래스를 사용한다. 생성자나 빌더나 생성 시점에 값을 채워주는 역할은 똑같다. 다만 생성자의 경우 지금 채워야 할필드가 무엇인지 명확히 지정할 수 없다.  
  
- 실제 실행된 쿼리 확인하기.
  - 쿼리 로그 확인을 ON / OFF로 확인할 수 있는 설정이 있다. 이런 설정들은 JAVA 클래스로 구현할 수 있으나, 스프링 부트에서는 application.properties, application.yml등의 파일로 한 줄의 코드로 설정할 수 있도록 지원하고 권장하고 있다.
  - src/main/resources 디렉토리 하위에 application.properties 파일 생성 



### 등록/수정/조회 API 만들기
- API 만들기 위한 3가지 클래스 필요
  - Request 데이터 받을 Dto
  - API 요청을 받을 Controller
  - 트랜잭션, 도메인 기능 간의 순서를 보장하는 Service ()
  
- Spring 웹 계층
  - Service는 비즈니스 로직 처리가아닌, 트랜잭션 및 도메인 기능 간의 순서를 보장한다.
  - 그럼 누가 비즈니스 로직을 처리하느냐?
    - Spring Web Layer에서 Domain에서 처리해야 한다.
    - 기존 서비스로 처리하던 방식을 트랜잭션 스크립트라고 한다.
      - 모든 로직이 서비스 클래스 내부에서 처리되며, 서비스 계층이 무의미해지고, 객체란 단순히 데이터 덩어리 역할만 하게 된다.
  - Spring 웹 계층을 알아보자.
    - Web Layer
      - 흔히 사용하는 @Controller, jsp/Freemaker 등 뷰 템플릿 영역
      - 이외에도 필터(@Filter), 인터셉터, 컨트롤러 어드바이스(@ControllerAdvice) 등 외부 요청과 응답에 대한 전반적인 영역
    - Service Layer
      - @Service에 사용되는 서비스 영역
      - 일반적으로 Controller와 Dao의 중간 영역에서 사용
      - @Transactional이 사용되는 영역
    - Repository Layer
      - Database와 같이 데이터 저장소에 접근하는 영역
      - Dao(Data Access Object) 영역으로 이해하면 쉽다.
    - Dtos
      - Dto(Data Transfer Object)는 계층 간에 데이터 교환을 위한 객체를 이야기한다.
      - 예를 들어 뷰 템플릿 엔진에서 사용될 객체나 Repository Layer에서 결과로 넘겨준 객체 등이 있다.
    - Domain Model
      - 도메인이라 불리는 개발 대상을 모든 사람이 동일한 관점에서 이해할 수 있고 공유할 수 있도록 단순화시킨 것을 도메인 모델이라 한다.
      - ex) 택시 앱에서 배차, 탑승, 요금 등
      - @Entity 사용 영역
      - 무조건 Database의 테이블과 관계가 있어야만 하는 것은 아니다.
      - VO처럼 값 객체들도 이 영역에 해당한다.
      
- Entity 클래스
  - Entity 클래스를 절대로 Request / Response 클래스로 사용해서는 안된다.
  - View Layer와 DB Layer의 역할 분리는 철저하게 하는 게 좋다.  
  - 꼭 Entity 클래스와 Controller에서 쓸 Dto는 분리해서 사용해야한다.
  <br/>
  
- 테스트 시 랜덤 포트 생성
  - @SpringBootTest() 속성 추가 
    - ex) @SpringBootTest(webEnvirontment = SpringBootTest.WebEnvironment.RANDOM_PORT)
  - @LocalServerPort 어노테이션 사용한 int 변수 추가, 그리고 그냥 사용
  
- H2 데이터베이스 메모리 직접 접근 시 웹 콘솔 사용법 
  - H2 사용 시 메모리에서 실행되기 때문에 직접 접근하려면 웹 콘솔을 사용해야 한다.
  1. spring.h2.console.enabled=true
  2. 웹 브라우저에서 http://localhost:8080/h2-console 접속 후 JDBC URL에 jdbc:h2:mem:testdb로 작성
    - 난 jdbc:h2:~/test이렇게 되어 있었다.
  3. connect를 클릭하여 현재 프로젝트의 H2를 관리할 수 있는 페이지 이동
  4. 이동 후 @Entity 객체의 리스트가 나오는지 확인

### JPA Auditing으로 생성시간 / 수정시간 자동화하기.
- 보통 Entity에 해당 데이터의 생성 시간 / 수정 시간을 포함한다. 차후 유지보수에 있어 중요한 정보다. 이 정보들이 Entity마다 들어간다고 생각하면 굉장한 중복이다.
  - 해결책으로 JPA Auditing을 사용
    - LocalDate 사용 (Java8에서 LocalDate와 LocalDateTime이 등장 했는데, Date의 문제점을 제대로 고친 타입이라 Java8일 경우 무조건 사용)
    - Date와 Calendar 클래스의 문제점
      1. 불변 객체가 아니다. 따라서 멀티 스레드 환경에서 언제든 문제가 발생할 수 있다.
      2. Calendar는 월(Month) 값 설계가 잘못되었다. 10월을 나타내는 Calendar.OCTOBER의 숫자 값은 9...
    - LocaDate와 LocalDateTime이 데이터베이스에 제대로 매핑되지 않는 이슈가 Hibernate 5.2.10버전에서 해결되었다.
      - 스프링 부트 1.x 버전을 쓴다면 Hibernate 5.2.10 버전 이상을 사용하도록 설정이 필요
      - 스프링 부트 2.x 버전을 쓴다면 기본적으로 해당 버전을 사용 중이라 별다른 설정 없이 바로 적용 가능 
    <br/>
    
```java
@Getter
// JPA @Entity들이 BaseTimeEntity를 상속할 경우 필드들을 컬럼으로 인식하도록 설정.
@MappedSuperclass
// BaseTimeEntity 클래스에 Auditing 기능 포함
@EntityListeners(AuditingEntityListener.class)
public class BaseTimeEntity {
   // @Entity가 생성되어 저장될 때 시간이 자동 저장
   @CreatedDate
   private LocalDateTime createdDate;
   // 조회한 Entity의 값을 변경할 때 시간이 자동 저장
   @LastModifiedDate
   private LocalDateTime modifiedDate;
}

// main 메소드가 있는 클래스에 @EnableJpaAuditing 어노테이션으로 JPA Auditing 활성화를 해줘야 한다.
```