package org.example.webmvc.step3.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

// 1. 스프링 컨테이너에 Bean으로 등록 - Component - Spring Web MVC - Controller
@Controller
// 2. WebServlet과 유사하게 path. -> (1) 클래스 단위 (2) 메서드 단위
@RequestMapping("/shirt") // class는 request mapping
// /shirt/* -> 이 클래스와 연결
// method는 request, get, post... HTTP 메서드라고 하는 것과 대응
public class ShirtController {
    //    @RequestMapping(method = RequestMethod.GET)
    // GET (/shirt/)
    @GetMapping
    public String page() {
        return "shirt"; // view name
        // 1. 포워드할 파일 이름 (접두사-접미사가 붙을)
        // 2. 리다이렉트할 URL
    }
}
