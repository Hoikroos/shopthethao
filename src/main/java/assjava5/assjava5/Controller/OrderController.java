package assjava5.assjava5.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import assjava5.assjava5.DAO.OrderDAO;
import assjava5.assjava5.Entity.Order;

@Controller
public class OrderController {
    @Autowired
    private OrderDAO orderDAO;

    @GetMapping("/bill")
    public String getBill(Model model) {
        List<Order> orders = orderDAO.findAll();
        model.addAttribute("orders", orders);
        return "admin/billManage";
    }
}
