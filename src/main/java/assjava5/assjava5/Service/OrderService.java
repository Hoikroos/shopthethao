package assjava5.assjava5.Service;

import assjava5.assjava5.DAO.OrderDAO;
import assjava5.assjava5.DAO.OrderDetailDAO;
import assjava5.assjava5.DAO.ProductDAO;
import assjava5.assjava5.DAO.AccountDAO;
import assjava5.assjava5.Entity.Order;
import assjava5.assjava5.Entity.OrderDetail;
import assjava5.assjava5.Entity.Product;
import assjava5.assjava5.Entity.Account;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class OrderService {

    @Autowired
    private OrderDAO orderDAO;

    @Autowired
    private OrderDetailDAO orderDetailDAO;

    @Autowired
    private ProductDAO productDAO;

    @Autowired
    private AccountDAO accountDAO;

    @Transactional
    public Order createOrder(String username, Map<Integer, Integer> cartItems, String address) {
        if (cartItems.isEmpty()) {
            throw new RuntimeException("Lỗi: Giỏ hàng trống, không thể đặt hàng!");
        }

        Optional<Account> accountOpt = accountDAO.findByUsername(username);
        if (accountOpt.isEmpty()) {
            throw new RuntimeException("Lỗi: Không tìm thấy tài khoản với username: " + username);
        }
        Account account = accountOpt.get();

        Order order = new Order();
        order.setAccount(account);
        order.setAddress(address);
        order.setCreateDate(LocalDateTime.now());

        orderDAO.save(order);

        double totalAmount = 0.0;

        for (Map.Entry<Integer, Integer> entry : cartItems.entrySet()) {
            Integer productId = entry.getKey();
            Integer quantity = entry.getValue();

            Optional<Product> productOpt = productDAO.findById(productId);
            if (productOpt.isEmpty()) {
                throw new RuntimeException("Lỗi: Không tìm thấy sản phẩm có ID: " + productId);
            }

            Product product = productOpt.get();
            double price = product.getPrice() * quantity;

            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrder(order);
            orderDetail.setProduct(product);
            orderDetail.setQuantity(quantity);
            orderDetail.setPrice(price);

            order.getOrderDetails().add(orderDetail);
            totalAmount += price;
        }

        orderDetailDAO.saveAll(order.getOrderDetails());
        order.setTotalAmount(totalAmount);
        return orderDAO.save(order);
    }

    public List<Order> findOrdersByUsername(String username) {
        return orderDAO.findByAccountUsername(username);
    }

    @Transactional
    public void deleteOrderById(Long id) {
        Optional<Order> orderOpt = orderDAO.findById(id);
        if (orderOpt.isPresent()) {
            Order order = orderOpt.get();
            orderDetailDAO.deleteAll(order.getOrderDetails());
            orderDAO.delete(order);
        } else {
            throw new RuntimeException("Không tìm thấy đơn hàng có ID: " + id);
        }
    }

}
