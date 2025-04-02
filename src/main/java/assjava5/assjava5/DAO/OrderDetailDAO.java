package assjava5.assjava5.DAO;

import org.springframework.data.jpa.repository.JpaRepository;

import assjava5.assjava5.Entity.OrderDetail;

public interface OrderDetailDAO extends JpaRepository<OrderDetail, Long> {

}
