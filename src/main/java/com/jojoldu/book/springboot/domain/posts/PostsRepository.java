package com.jojoldu.book.springboot.domain.posts;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

// 보통 MyBatis에서 Dao라고 불리는 DB Layer 접근자로써 JPA에서는 Repository라고 부르며 인터페이스로 생성한다.
// Entitiy Class, PK 타입을 지정
// @Repository 어노테이션 사용할 필요 없으며, 주의할 점은 Entity Class와 Repository는 함께 위치해야 한다.
// 따라서 domain package에서 함께 관리한다.
public interface PostsRepository extends JpaRepository<Posts, Long> {

    // SpringDataJpa에서 제공하지 않는 메소드는 쿼리 작성, SDJ에서 제공하는 기본 메소드만으로 해결 가능하나 @Query가 훨씬 가독성이 좋으니 선택 사용
    @Query("SELECT p FROM Posts p ORDER BY p.id DESC")
    List<Posts> findAllDesc();

}
