package controller;

import model.Code;
import model.Order;
import model.Product;
import model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;
import service.interfaces.BasketService;
import service.interfaces.MailService;
import service.interfaces.OrderService;
import service.interfaces.ProductService;
import service.interfaces.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Controller
@SessionAttributes("user")
public class OrderController {

    private OrderService orderService;
    private BasketService basketService;
    private UserService userService;
    private ProductService productService;
    private MailService mailService;

    @Autowired
    public OrderController(OrderService orderService, BasketService basketService,
                           UserService userService, ProductService productService,
                           MailService mailService) {
        this.orderService = orderService;
        this.basketService = basketService;
        this.userService = userService;
        this.productService = productService;
        this.mailService = mailService;
    }

    @GetMapping(value = "/user/checkout")
    public ModelAndView showProductsInBasket(
            @RequestParam(value = "userId") String userId, ModelMap model) {
        Optional<User> user = userService.getUserById(Long.parseLong(userId));
        if (user.isPresent()) {
            List<Product> products = basketService.getAllProducts(user.get());
            model.addAttribute("products", products);
            model.addAttribute("user", user.get());
        }
        return new ModelAndView("checkout", model);
    }

    @GetMapping(path = "/user/basket/product/delete")
    public String deleteProductFromBasket(
            @SessionAttribute("user") Optional<User> user,
            @RequestParam(value = "id") String productId,
            ModelMap model) {
        if (Objects.nonNull(productId) && user.isPresent()) {
            Optional<Product> product = productService.getProductById(Long.parseLong(productId));
            if (product.isPresent()) {
                basketService.removeProduct(product.get(), user.get());
                return "redirect:/user/checkout?userId=" + user.get().getId();
            }
        }
        return "redirect:/user/checkout";
    }

    @PostMapping("/user/checkout")
    public String sendCode(
            ModelMap model,
            @RequestParam("firstName") String firstName,
            @RequestParam("lastName") String lastName,
            @RequestParam("city") String city,
            @RequestParam("street") String street,
            @RequestParam("houseNumber") String houseNumber,
            @RequestParam("phoneNumber") String phoneNumber,
            @SessionAttribute("user") Optional<User> user,
            HttpServletRequest request) {
        try {
            if (!firstName.isEmpty() && !lastName.isEmpty() && !city.isEmpty()
                    && !street.isEmpty() && !houseNumber.isEmpty() && user.isPresent()
                    && !phoneNumber.isEmpty() && !basketService.getAllProducts(user.get()).isEmpty()) {
                user.get().setCode(new Code());
                user.get().getCode().generateCode();
                mailService.sendConfirmCode(user.get());
                Order order = new Order(user.get(), user.get().getCode(),
                        firstName, lastName, city, street, houseNumber, phoneNumber);
                HttpSession session = request.getSession();
                session.setAttribute("order", order);
                return "code";
            } else {
                model.addAttribute("message", "All fields must be filled!!!");
                return "redirect:/user/checkout";
            }
        } catch (NullPointerException e) {
            model.addAttribute("message", "Wrong data");
            return "redirect:/user/checkout";
        }
    }

    @PostMapping("/user/code")
    public ModelAndView confirmCode(
            ModelMap model,
            @RequestParam(name = "code") String userCode,
            @SessionAttribute("user") Optional<User> user,
            HttpServletRequest request) {
        Order order = (Order) request.getSession().getAttribute("order");
        try {
            if (Objects.nonNull(order) && user.isPresent()
                    && userCode.equals(user.get().getCode().getValue())) {
                Optional<Long> orderId = orderService.addOrder(order);
                if (orderId.isPresent()) {
                    model.addAttribute("message",
                            "Order #" + orderId.get() + " successfully placed");
                    basketService.clear(user.get());
                    return new ModelAndView("code");
                } else {
                    model.addAttribute("message", "Such order not exist");
                    return new ModelAndView("code");
                }
            } else {
                model.addAttribute("message", "Incorrect code!!!");
                return new ModelAndView("code");
            }
        } catch (NullPointerException e) {
            model.addAttribute("message", "Wrong data!!!");
            return new ModelAndView("code");
        }
    }
}
