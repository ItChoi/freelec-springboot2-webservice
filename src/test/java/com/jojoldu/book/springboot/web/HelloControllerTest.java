package com.jojoldu.book.springboot.web;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// 테스트 실행 시 SpringRunner라는 스프링 실행자를 사용한다.
// 즉, 스프링 부트 테스트와 JUnit 사이에 연결자 역할을 한다.
@RunWith(SpringRunner.class)
// 스프링 테스트 애노테이션 중, Web(Spring MVC)에 집중할 수 있는 테스트 어노테이션
// 선언 시 @Controller, @ControllerAdvice 등을 사용할 수 있다.
// @Service, @Component, @Repository 등을 사용할 수 없다.
@WebMvcTest  // JPA 기능은 작동하지 않는다. // JPA 기능 테스트 시 @SpringBootTest와 @TestRestTemplate을 같이 사용
public class HelloControllerTest {

    // 스프링이 관리하는 Bean을 주입 받는다.
    @Autowired
    private MockMvc mvc; // 웹 API 테스트 시 사용, 이 클래스를 통해 Http Get, Post 등 API 테스트를 할 수 있다.

    @Test
    public void hello가_리턴된다() throws Exception {
        String hello = "hello";

        mvc.perform(get("/hello"))           // MockMvc를 통해 hello에 대한 get 요청
                .andExpect(status().isOk())             // mvc.perform의 결과를 검증, Http header의 status 검증,
                .andExpect(content().string(hello));    // 응답 본문 내용을 검증
    }

    @Test
    public void helloDto가_리턴된다() throws Exception {
        String name = "hello";
        int amount = 1000;

        // param: API 테스트 시 요청 파라미터 설정, 값은 String만 허용
        // jsonPath: JSON 응답 값을 필드 별로 검증할 수 있는 메소드, $를 기준으로 필드명을 명시
        mvc.perform(
                get("/hello/dto")
                    .param("name", name)
                    .param("amount", String.valueOf(amount))
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(name)))
                .andExpect(jsonPath("$.amount", is(amount)));
    }

}
