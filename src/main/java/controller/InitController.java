package controller;

import model.Product;
import model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import service.interfaces.ProductService;
import service.interfaces.UserService;
import utils.HashUtil;

@Controller
public class InitController {

    private UserService userService;
    private ProductService productService;

    @Autowired
    public InitController(UserService userService, ProductService productService) {
        this.userService = userService;
        this.productService = productService;
    }

    @RequestMapping(path = {"/init"}, method = RequestMethod.GET)
    private void init() {
        String salt1 = HashUtil.getSalt();
        String salt2 = HashUtil.getSalt();
        String salt3 = HashUtil.getSalt();
        User admin = new User("admin@gmail.com", HashUtil.getHash("123", salt1), "admin");
        User user = new User("user@gmail.com", HashUtil.getHash("123", salt2), "user");
        User user2 = new User("test@gmail.com", HashUtil.getHash("123", salt3), "user");
        admin.setSalt(salt1);
        user.setSalt(salt2);
        user2.setSalt(salt3);
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
