package assjava5.assjava5.Controller;

import assjava5.assjava5.Entity.CartItem;
import assjava5.assjava5.Service.ShoppingCartService;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    @GetMapping("/view")
    public String viewCart(Model model) {
        Collection<CartItem> cartItems = shoppingCartService.getItems();
        double totalAmount = shoppingCartService.getAmount();

        model.addAttribute("cartItems", cartItems);
        model.addAttribute("totalAmount", totalAmount);

        return "home/cartProduct";
    }

    @ModelAttribute("cartQuantity")
    public int getCartQuantity() {
        return shoppingCartService.getTotalQuantity();
    }

    @PostMapping("/add")
    public String addToCart(@RequestParam("productId") Integer productId,
            @RequestParam("quantity") int quantity,
            Model model,
            HttpSession session) {
        String username = (String) session.getAttribute("username");
        if (username == null) {
            model.addAttribute("error", "Bạn cần đăng nhập để thêm sản phẩm vào giỏ hàng.");
            return "redirect:/login";
        }
        try {
            CartItem cartItem = shoppingCartService.add(productId);
            if (cartItem != null) {
                cartItem.setQuantity(quantity);
            }
        } catch (RuntimeException e) {
            model.addAttribute("error", "Không thể thêm sản phẩm vào giỏ hàng.");
        }

        return "redirect:/cart/view";
    }

    @GetMapping("/update")
    public String updateCart(@RequestParam("productId") Integer productId,
            @RequestParam("quantity") Integer quantity) {
        if (quantity > 0) {
            shoppingCartService.update(productId, quantity);
        }
        return "redirect:/cart/view";
    }

    @GetMapping("/remove")
    public String removeItem(@RequestParam("productId") Integer productId) {
        shoppingCartService.remove(productId);
        return "redirect:/cart/view";
    }
}
