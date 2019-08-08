package controller;

import model.Product;
import model.User;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import service.interfaces.BasketService;
import service.interfaces.ProductService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

@Controller
@SessionAttributes("user")
public class ProductController {

    private ProductService productService;
    private BasketService basketService;
    private static final Logger logger = Logger.getLogger(ProductController.class);

    @Autowired
    public ProductController(ProductService productService, BasketService basketService) {
        this.productService = productService;
        this.basketService = basketService;
    }

    @GetMapping(path = "/user/products")
    public String doGetAllProducts(
            HttpServletRequest request,
            @SessionAttribute("user") Optional<User> user) {
        if (user.isPresent()) {
            List<Product> allProducts = productService.getAllProducts();
            int productCountOfBasket = basketService.getAllProducts(user.get()).size();
            request.setAttribute("products", allProducts);
            request.setAttribute("productCountOfBasket", productCountOfBasket);
        }
        return "products";
    }

    @GetMapping(path = "/product")
    public String doGetEditProduct(Model model, @RequestParam(value = "id") String productId) {
        if (productId != null) {
            Optional<Product> product = productService.getProductById(Long.parseLong(productId));
            if (product.isPresent()) {
                model.addAttribute("product", product.get());
            }
        }
        return "edit_product";
    }

    @PostMapping(path = {"/product"})
    public String editProduct(
            Model model,
            @RequestParam(value = "id") String productId,
            @RequestParam(value = "name") String name,
            @RequestParam(value = "description") String description,
            @RequestParam(value = "price") String priceStr) {
        try {
            Double price = Double.parseDouble(priceStr);
            Optional<Product> productById = productService.getProductById(Long.parseLong(productId));
            if (!name.isEmpty() && !description.isEmpty() && price > 0) {
                if (productById.isPresent()) {
                    Product newProduct = new Product(
                            productById.get().getId(), name, description, price);
                    productService.updateProduct(newProduct);
                    return "redirect:/user/products";
                }
            } else {
                model.addAttribute("product", productById.get());
                model.addAttribute("message", "All fields must be filled!!!");
                return "edit_product";
            }
        } catch (NumberFormatException e) {
            logger.info("Invalid input format.");
            return "product?id=" + productId;
        }
        return "products";
    }

    @GetMapping(path = "/admin/product/delete")
    public String deleteUser(@RequestParam(value = "id") String productId) {
        if (productId != null) {
            Optional<Product> product = productService.getProductById(Long.parseLong(productId));
            if (product.isPresent()) {
                productService.removeProduct(product.get());
            }
        }
        return "redirect:/user/products";
    }

    @GetMapping(path = "/add_product")
    public String doGetAddProduct() {
        return "add_product";
    }

    @PostMapping(path = "/add_product")
    public String addProduct(
            Model model,
            @RequestParam(value = "name") String name,
            @RequestParam(value = "description") String description,
            @RequestParam(value = "price") String priceStr) {
        try {
            Double price = Double.parseDouble(priceStr);
            if (!name.isEmpty() && !description.isEmpty() && price > 0) {
                Product newProduct = new Product(name, description, price);
                productService.addProduct(newProduct);
                return "redirect:/user/products";
            } else {
                model.addAttribute("message", "All fields must be filled!!!");
                return "add_product";
            }
        } catch (NumberFormatException e) {
            logger.info("Invalid input format.");
            return "add_product";
        }
    }
}
