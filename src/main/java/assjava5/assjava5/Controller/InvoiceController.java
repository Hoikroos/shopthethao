package assjava5.assjava5.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import assjava5.assjava5.Entity.Order;
import assjava5.assjava5.Service.OrderService;
import assjava5.assjava5.Service.SessionService;

@Controller
@RequestMapping("/invoices")
public class InvoiceController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private SessionService sessionService;

    @GetMapping("/my-orders")
    public String showMyOrders(Model model) {
        String username = (String) sessionService.get("username", null);

        if (username == null) {
            return "redirect:/login?error=notLoggedIn";
        }

        List<Order> orders = orderService.findOrdersByUsername(username);
        model.addAttribute("orders", orders != null ? orders : List.of());

        return "home/check-invoice";
    }

    @DeleteMapping("/delete-order/{id}")
    public ResponseEntity<?> deleteOrder(@PathVariable Long id) {
        try {
            orderService.deleteOrderById(id);
            return ResponseEntity.ok("Xóa đơn hàng thành công!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Lỗi khi xóa đơn hàng: " + e.getMessage());
        }
    }
}
