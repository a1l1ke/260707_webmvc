package org.example.webmvc.step4;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/scope")
public class ScopeController {
    @GetMapping
    public String scope() {
        return "scope";
    }
}
