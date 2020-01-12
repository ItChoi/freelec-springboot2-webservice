package com.jojoldu.book.springboot.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

// 머스테치 컨트롤러로 URL 매핑하기.
@Controller
public class IndexController {

    @GetMapping("/")
    public String index() {

        // 머스테치 스타터 덕분에 문자열 반환 시 앞의 경로와 뒤의 파일 확장자는 자동으로 지정된다.
        return "index";
    }

}
