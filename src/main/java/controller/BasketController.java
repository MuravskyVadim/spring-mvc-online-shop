package controller;

import model.Product;
import model.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import service.interfaces.BasketService;
import service.interfaces.ProductService;
import service.interfaces.UserService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@Controller
public class BasketController {

    private BasketService basketService;
    private ProductService productService;
    private UserService userService;

    public BasketController(BasketService basketService,
                            ProductService productService, UserService userService) {
        this.basketService = basketService;
        this.productService = productService;
        this.userService = userService;
    }

    @GetMapping(value = "/user/product/by")
    public void byProduct(
            Model model,
            @RequestParam(value = "userId") String userId,
            @RequestParam(value = "productId") String productId,
            HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (productId != null && userId != null) {
            Optional<Product> product = productService.getProductById(Long.parseLong(productId));
            Optional<User> user = userService.getUserById(Long.parseLong(userId));
            if (product.isPresent() && user.isPresent()) {
                basketService.addProduct(product.get(), user.get());
                request.setAttribute("message", product.get().getName() + " added to basket");
                request.getRequestDispatcher("/user/products").forward(request, response);
            } else {
                request.setAttribute("message", "Such product or user not exist.");
                request.getRequestDispatcher("/user/products").forward(request, response);
            }
        } else {
            request.setAttribute("message", "Incorrect data.");
            request.getRequestDispatcher("/user/products").forward(request, response);
        }
    }
}
