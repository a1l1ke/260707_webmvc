package org.example.webmvc.step3.controller;

import jakarta.servlet.http.HttpSession;
import org.example.webmvc.step3.dto.ShirtDTO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
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
    public String page(
    ) {
        return "shirt"; // view name
        // 1. 포워드할 파일 이름 (접두사-접미사가 붙을)
        // 2. 리다이렉트할 URL
    }

    @PostMapping
    public String form(
            // model.getParameter -> dispatcherServlet이 받아서
            // @RequestParam -> name 값이 일치하는 것에 뿌려줌 -> 타입변환도 알아서 잘.
//            @RequestParam("name") String name,
//            @RequestParam("price") int price
            @ModelAttribute ShirtDTO shirtDTO,
            // form에 있는 데이터의 구조를 전달받은 클래스(DTO)와 일치하는지 보고, 전달해줌.
            HttpSession session
    ) {
//        System.out.println("name = " + name);
//        System.out.println("price = " + price);
//        return "shirt"; // jsp
        System.out.println("shirtDTO = " + shirtDTO);
        session.setAttribute("shirt", shirtDTO);
        return "redirect:/shirt"; // 리다이렉트 시켜줌
    }
    // 뷰 리졸버 - 일반 텍스트 입력 시 -> 알아서 접두사/접미사를 붙인 다음 템플릿을 찾아서 이동 (포워드)
    // - 'redirect' -> 해당 경로(path)로 이동 - 다른 컨트롤러 등으로 구현이 된 상태의 경로로 리다이렉트
}
