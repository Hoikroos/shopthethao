package assjava5.assjava5.DAO;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import assjava5.assjava5.Entity.Product;
import assjava5.assjava5.Entity.Report;

import java.util.List;

public interface ProductDAO extends JpaRepository<Product, Integer> {
    @Query("SELECT p FROM Product p WHERE p.category.id = :categoryId")
    List<Product> findByCategory(@Param("categoryId") String categoryId);

    @Query("FROM Product o WHERE o.name LIKE ?1")
    List<Product> findByKeywords(String keywords);

    @Query("SELECT new assjava5.assjava5.Entity.ReportImpl( " +
            "p.category.id, SUM(p.price), COUNT(p.id), " +
            "MAX(p.price), MIN(p.price), AVG(p.price)) " +
            "FROM Product p WHERE p.category.id = :categoryId " +
            "GROUP BY p.category.id")
    List<Report> getStatisticsByCategoryId(@Param("categoryId") String categoryId);

}