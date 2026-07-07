package org.example.webmvc.step4;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/scope")
@RequiredArgsConstructor // final로 되어 있는 멤버변수를 포함한 생성자를 생성
// -> 생성자 주입에 쓰이게 됨
public class ScopeController {
    private final RequestScopeBean requestScopeBean;
    private final SessionScopeBean sessionScopeBean; // 주입이 안되어서

    @GetMapping
    public String scope(Model model) {
        model.addAttribute("rs_uuid", requestScopeBean.getUuid());
        model.addAttribute("rs_ct", requestScopeBean.getCreationTime());
        model.addAttribute("ss_uuid", sessionScopeBean.getUuid());
        model.addAttribute("ss_ct", sessionScopeBean.getCreationTime());
        sessionScopeBean.increment();
        model.addAttribute("ss_count", sessionScopeBean.getCount());
        return "scope";
    }
}
