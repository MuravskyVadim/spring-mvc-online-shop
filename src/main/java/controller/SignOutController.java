package controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/exit")
public class SignOutController {

    @GetMapping
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}
