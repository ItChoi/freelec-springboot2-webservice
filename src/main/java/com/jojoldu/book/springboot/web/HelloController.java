package com.jojoldu.book.springboot.web;

import com.jojoldu.book.springboot.web.dto.HelloResponseDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

// JSON을 반환하는 컨트롤러로 만들어준다.
// 메소드 마다 @ResponseBody를 선언해주었던 것을 한번에 해준다.
@RestController // 1
public class HelloController {

    // Http Method인 Get의 요청을 받을 수 있는 API를 만들어준다.
    @GetMapping("/hello") // 2
    public String hello() {
        return "hello";
    }

    // @RequestParam: 외부에서 API로 넘긴 파라미터를 가져오는 어노테이션
    @GetMapping("/hello/dto")
    public HelloResponseDto helloDto(@RequestParam("name") String name,
                                     @RequestParam("amount") int amount) {

        return new HelloResponseDto(name, amount);
    }

}
