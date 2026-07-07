package org.example.webmvc.step2.controller;

import org.example.webmvc.step2.dto.FoodDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

// DispatcherServlet이 있기 때문에 이제 서블릿을 만들지 X
@Controller
@RequestMapping // (경로) <- 안넣으면 / 루트 경로가 된다
public class FoodController {
    // doGet? doPost?
    @GetMapping
    public String index(Model model) { // spring mvc가 알아서 존재 여부에 따라서 해당 메서드에 전달
        System.out.println("FoodController.index");
//        model.addAttribute("food", "나가사키짬뽕");
        model.addAttribute("food", new FoodDTO("미소라멘", 12_000));
        return "index"; // controller -> string -> 어디로 포워드를 할까
        // 접두사 - (...) - 접미사 -> 일괄처리
    }
}
