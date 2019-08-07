package controller;

import model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;
import service.interfaces.UserService;
import utils.HashUtil;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping(path = "/admin")
@SessionAttributes("user")
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(path = {"/users"})
    public String allUsers(Model model) {
        List<User> allUsers = userService.getAllUsers();
        model.addAttribute("users", allUsers);
        return "users";
    }

    @GetMapping(path = "/user")
    public String setUser(Model model, @RequestParam(value = "id") String userId) {
        if (userId != null) {
            Optional<User> user = userService.getUserById(Long.parseLong(userId));
            if (user.isPresent()) {
                model.addAttribute("user", user.get());
            }
        }
        return "user";
    }

    @PostMapping(path = "/user")
    public String editUser(
            Model model,
            @RequestParam(value = "id") String userId,
            @RequestParam(value = "email") String email,
            @RequestParam(value = "password") String password,
            @RequestParam(value = "confirmPassword") String confirmPassword,
            @RequestParam(value = "role") String role) throws IOException {
        Optional<User> userById = userService.getUserById(Long.parseLong(userId));
        if (!email.isEmpty() && !password.isEmpty()
                && !confirmPassword.isEmpty() && userById.isPresent()) {
            if (password.equals(confirmPassword)) {
                String salt = HashUtil.getSalt();
                String hashPassword = HashUtil.getHash(password, salt);
                User newUser = new User(userById.get().getId(), email, hashPassword, role);
                newUser.setSalt(salt);
                userService.updateUser(newUser);
            } else {
                model.addAttribute("user", userById.get());
                model.addAttribute("message", "Passwords not equals! Try again.");
                return "user";
            }
        } else {
            model.addAttribute("user", userById.get());
            model.addAttribute("message", "All fields must be filled!!!");
            return "user";
        }
        return "redirect:/admin/users";
    }

    @GetMapping(path = "/user/delete")
    public String deleteUser(@RequestParam(value = "id") String userId) {
        if (userId != null) {
            Optional<User> user = userService.getUserById(Long.parseLong(userId));
            if (user.isPresent()) {
                userService.deleteUser(user.get());
            }
        }
        return "redirect:/admin/users";
    }

    @GetMapping(path = "/register")
    public String register() {
        return "/register";
    }

    @PostMapping(path = "/register")
    public ModelAndView registerUser(
            Model model,
            @RequestParam(value = "email") String email,
            @RequestParam(value = "password") String password,
            @RequestParam(value = "confirmPassword") String confirmPassword,
            @RequestParam(value = "role") String role) {
        if (!email.isEmpty() && !password.isEmpty()
                && !confirmPassword.isEmpty() && !role.isEmpty()) {
            if (!userService.isUserExist(email)) {
                if (password.equals(confirmPassword)) {
                    String salt = HashUtil.getSalt();
                    User user = new User(email, HashUtil.getHash(password, salt), role);
                    user.setSalt(salt);
                    userService.addUser(user);
                    return new ModelAndView("redirect:/admin/users");
                } else {
                    model.addAttribute("message", "Passwords not equals! Try again...");
                    return new ModelAndView("redirect:/admin/register");
                }
            } else {
                model.addAttribute("message", "User " + email + " is already registered.");
                return new ModelAndView("redirect:/admin/register");
            }
        } else {
            model.addAttribute("message", "All fields must be filled!!!");
            return new ModelAndView("redirect:/admin/register");
        }
    }
}
