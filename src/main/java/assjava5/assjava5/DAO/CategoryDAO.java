package assjava5.assjava5.DAO;

import assjava5.assjava5.Entity.Category; 
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryDAO extends JpaRepository<Category, String> {

}

