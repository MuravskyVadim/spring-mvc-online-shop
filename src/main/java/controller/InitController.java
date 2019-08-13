package controller;

import model.Product;
import model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import service.interfaces.ProductService;
import service.interfaces.UserService;

import javax.annotation.PostConstruct;

@Controller
public class InitController {

    private UserService userService;
    private ProductService productService;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public InitController(UserService userService, ProductService productService,
                          BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userService = userService;
        this.productService = productService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @PostConstruct
    private void init() {
        User admin = new User("admin@gmail.com", bCryptPasswordEncoder.encode("123"), "ROLE_ADMIN");
        User user = new User("user@gmail.com", bCryptPasswordEncoder.encode("123"), "ROLE_USER");
        User user2 = new User("test@gmail.com", bCryptPasswordEncoder.encode("123"), "ROLE_USER");
        userService.addUser(admin);
        userService.addUser(user);
        userService.addUser(user2);

        Product product1 = new Product("Phone", "iPhone X 128G", 699.0);
        Product product2 = new Product("Watch", "wrist watch", 399.0);
        Product product3 = new Product("MacBook", "laptop", 2699.0);
        Product product4 = new Product("iPad", "tablet", 1100.0);
        productService.addProduct(product1);
        productService.addProduct(product2);
        productService.addProduct(product3);
        productService.addProduct(product4);
    }
}
