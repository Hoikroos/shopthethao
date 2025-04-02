package assjava5.assjava5.Controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Collection;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import assjava5.assjava5.Entity.Order;
import assjava5.assjava5.Entity.Account;
import assjava5.assjava5.Entity.CartItem;
import assjava5.assjava5.Service.ShoppingCartService;
import assjava5.assjava5.Service.OrderService;
import assjava5.assjava5.Service.SessionService;
import assjava5.assjava5.DAO.AccountDAO;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/pay")
public class OrderDetailController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private AccountDAO accountDAO;

    @Autowired
    private SessionService sessionService;

    @GetMapping
    public String showPaymentPage(Model model, HttpServletRequest request) {
        Collection<CartItem> cartItems = shoppingCartService.getItems();

        if (cartItems.isEmpty()) {
            model.addAttribute("error", "Giỏ hàng của bạn đang trống.");
            return "redirect:/cart/view";
        }

        String username = getUsername(request);
        if (username == null) {
            model.addAttribute("error", "Bạn cần đăng nhập để thanh toán!");
            return "redirect:/login";
        }

        Optional<Account> accountOpt = accountDAO.findByUsername(username);
        if (accountOpt.isEmpty()) {
            model.addAttribute("error", "Tài khoản không hợp lệ!");
            return "redirect:/login";
        }
        Account account = accountOpt.get();
        double totalPrice = cartItems.stream()
                .mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity())
                .sum();

        model.addAttribute("cartItems", cartItems);
        model.addAttribute("totalPrice", totalPrice);
        model.addAttribute("order", new Order());

        return "home/pay";
    }

    @PostMapping("/submit")
    public String handlePaymentSubmission(@ModelAttribute Order order, Model model, HttpServletRequest request) {
        String username = getUsername(request);
        if (username == null) {
            model.addAttribute("error", "Bạn cần đăng nhập để thanh toán!");
            return "redirect:/login";
        }

        if (order.getAddress() == null || order.getAddress().trim().isEmpty()) {
            model.addAttribute("error", "Vui lòng nhập địa chỉ giao hàng!");
            return "home/pay";
        }

        Optional<Account> accountOpt = accountDAO.findByUsername(username);
        if (accountOpt.isEmpty()) {
            model.addAttribute("error", "Tài khoản không hợp lệ!");
            return "redirect:/login";
        }
        Account account = accountOpt.get();

        Map<Integer, Integer> cartItems = new HashMap<>();
        for (CartItem item : shoppingCartService.getItems()) {
            cartItems.put(item.getProduct().getId(), item.getQuantity());
        }

        try {
            Order savedOrder = orderService.createOrder(username, cartItems, order.getAddress());
            shoppingCartService.clearCart();

            model.addAttribute("order", savedOrder);
            model.addAttribute("message", "Thanh toán thành công!");
            return "home/order-success";
        } catch (Exception e) {
            model.addAttribute("error", "Đã xảy ra lỗi khi xử lý thanh toán: " + e.getMessage());
            return "home/pay";
        }
    }
    @GetMapping("/success")
    public String showSuccessPage(Model model) {
        model.addAttribute("message", "Thanh toán của bạn đã được xử lý thành công!");
        return "home/order-success";
    }

    private String getUsername(HttpServletRequest request) {
        String sessionUser = sessionService.get("username", null);
        if (sessionUser != null) {
            return sessionUser;
        }
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("user".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
