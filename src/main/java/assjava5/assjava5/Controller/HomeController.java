package assjava5.assjava5.Controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import assjava5.assjava5.DAO.AccountDAO;
import assjava5.assjava5.DAO.CategoryDAO;
import assjava5.assjava5.DAO.OrderDAO;
import assjava5.assjava5.DAO.OrderDetailDAO;
import assjava5.assjava5.DAO.ProductDAO;
import assjava5.assjava5.Entity.Category;
import assjava5.assjava5.Entity.Product;
import assjava5.assjava5.Service.SessionService;

@Controller
@RequestMapping("/")
public class HomeController {
    @Autowired
    ProductDAO productDAO;
    @Autowired
    CategoryDAO categoryDAO;
    @Autowired
    AccountDAO accountDAO;
    @Autowired
    OrderDAO orderDAO;
    @Autowired
    OrderDetailDAO orderDetailDAO;
    @Autowired
    SessionService sessionService;

    @GetMapping("/home")
    public String index(Model model) {
        List<Category> categories = categoryDAO.findAll();
        sessionService.setCategories(categories);

        int cartQuantity = sessionService.getCartQuantity();
        model.addAttribute("categories", categories);
        model.addAttribute("cartQuantity", cartQuantity);

        return "home/home";
    }

    @GetMapping("/category/{id}")
    public String getProductsByCategory(@PathVariable("id") String categoryId, Model model) {
        List<Category> categories = categoryDAO.findAll();
        Category category = categoryDAO.findById(categoryId).orElse(null);
        List<Product> products = productDAO.findByCategory(categoryId);

        int cartQuantity = sessionService.getCartQuantity();

        model.addAttribute("categories", categories);
        model.addAttribute("category", category);
        model.addAttribute("products", products);
        model.addAttribute("cartQuantity", cartQuantity);

        return "home/category";
    }

    @GetMapping("/product/{id}")
    public String getProduct(@PathVariable("id") int productId, Model model) {
        List<Category> categories = categoryDAO.findAll();
        model.addAttribute("categories", categories);
        Optional<Product> optionalProduct = productDAO.findById(productId);
        if (optionalProduct.isEmpty()) {
            return "redirect:/home";
        }
        model.addAttribute("product", optionalProduct.get());
        return "home/chitietsp";
    }

    private int getCartQuantity() {
        List<Product> cart = sessionService.getCart();
        return (cart == null) ? 0 : cart.size();
    }
}
