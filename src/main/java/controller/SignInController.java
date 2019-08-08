package controller;

import model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import service.interfaces.UserService;
import utils.HashUtil;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Optional;

@Controller
public class SignInController {

    private UserService userService;

    @Autowired
    public SignInController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public String signIn() {
        return "index";
    }

    @PostMapping(path = "/login", params = {"email", "password"})
    public String login(
            Model model,
            @RequestParam(value = "email") String email,
            @RequestParam(value = "password") String password,
            HttpSession session) throws IOException {
        if (!email.isEmpty() && !password.isEmpty()) {
            Optional<User> user = userService.getUserByEmail(email);
            if (user.isPresent()) {
                String hashPassword = HashUtil.getHash(password, user.get().getSalt());
                if (user.get().getPassword().equals(hashPassword)) {
                    session.setAttribute("user", user.get());
                    return "redirect:/user/products";
                } else {
                    model.addAttribute("message", "Wrong email or password");
                    return "index";
                }
            } else {
                model.addAttribute("message", "User " + email + " not exist. Please register.");
                return "index";
            }
        } else {
            model.addAttribute("message", "All fields must be filled!!!");
        }
        return "index";
    }
}
