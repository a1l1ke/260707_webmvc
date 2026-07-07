package org.example.webmvc.step2.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

// DispatcherServlet이 있기 때문에 이제 서블릿을 만들지 X
@Controller
@RequestMapping // (경로) <- 안넣으면 / 루트 경로가 된다
public class FoodController {
    // doGet? doPost?
    @GetMapping
    public String index() {
        System.out.println("FoodController.index");
        return "index"; // controller -> string -> 어디로 포워드를 할까
        // 접두사 - (...) - 접미사 -> 일괄처리
    }
}
