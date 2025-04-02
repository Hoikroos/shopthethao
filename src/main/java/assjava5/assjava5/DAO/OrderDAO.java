package assjava5.assjava5.DAO;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import assjava5.assjava5.Entity.Order;
import assjava5.assjava5.Entity.VipCustomerReport;

public interface OrderDAO extends JpaRepository<Order, Long> {
    List<Order> findByAccountUsername(String username);

    @Query(value = "SELECT a.fullname AS customerName, " +
            "SUM(o.totalAmount) AS totalSpent, " +
            "MIN(o.createDate) AS firstPurchaseDate, " +
            "MAX(o.createDate) AS lastPurchaseDate " +
            "FROM orders o INNER JOIN accounts a ON o.username = a.username " +
            "GROUP BY a.fullname " +
            "ORDER BY SUM(o.totalAmount) DESC LIMIT 10", nativeQuery = true)
    List<VipCustomerReport> getTop10VipCustomers();
}
