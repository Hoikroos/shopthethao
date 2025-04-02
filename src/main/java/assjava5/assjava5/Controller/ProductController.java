package assjava5.assjava5.Controller;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import assjava5.assjava5.DAO.CategoryDAO;
import assjava5.assjava5.DAO.ProductDAO;
import assjava5.assjava5.Entity.Category;
import assjava5.assjava5.Entity.Product;
import jakarta.transaction.Transactional;

@Controller
public class ProductController {

    @Autowired
    ProductDAO productDAO;

    @Autowired
    CategoryDAO categoryDAO;

    // Hiển thị trang quản lý sản phẩm
    @GetMapping("/admin")
    public String getAdmin(Model model) {
        Product product = new Product();
        model.addAttribute("product", product);
        List<Product> products = productDAO.findAll();
        model.addAttribute("products", products);
        List<Category> categories = categoryDAO.findAll();
        model.addAttribute("categories", categories);
        return "admin/adminPage";
    }

    // API lấy thông tin sản phẩm theo ID (dùng cho nút "Sửa" và "Xem")
    @GetMapping("/admin/getProduct/{id}")
    @ResponseBody
    public ResponseEntity<Product> getProduct(@PathVariable("id") int id) {
        Optional<Product> productOpt = productDAO.findById(id);
        if (productOpt.isPresent()) {
            return ResponseEntity.ok(productOpt.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Tạo mới sản phẩm
    @Transactional
    @RequestMapping(value = "/admin/create", method = RequestMethod.POST)
    public String create(@ModelAttribute("product") Product item,
            @RequestParam("photoFile") MultipartFile photoFile) {
        System.out.println("Received product: " + item);
        System.out.println("Received createDate: " + item.getCreateDate());

        // Xử lý upload ảnh
        if (!photoFile.isEmpty()) {
            String uploadDir = "H:\\FPT Polytechnic\\SPRING_25\\SOF3022_JAVA5\\ASSJAVA5_PD10303\\assjava5\\src\\main\\resources\\static\\photo\\";
            String fileName = System.currentTimeMillis() + "_" + photoFile.getOriginalFilename();
            File saveFile = new File(uploadDir + fileName);
            try {
                saveFile.getParentFile().mkdirs();
                photoFile.transferTo(saveFile);
                item.setImage(fileName);
            } catch (IOException e) {
                e.printStackTrace();
                return "redirect:/admin?error=upload";
            }
        } else {
            item.setImage("placeholder.jpg"); // Ảnh mặc định nếu không upload
        }

        productDAO.save(item);
        return "redirect:/admin";
    }

    // Cập nhật sản phẩm
    @Transactional
    @RequestMapping(value = "/admin/update", method = RequestMethod.POST)
    public String update(@ModelAttribute("product") Product item,
            @RequestParam("photoFile") MultipartFile photoFile) {
        Optional<Product> existingProductOpt = productDAO.findById(item.getId());
        if (existingProductOpt.isEmpty()) {
            return "redirect:/admin?error=notfound";
        }

        Product existingProduct = existingProductOpt.get();
        item.setCreateDate(existingProduct.getCreateDate()); // Giữ nguyên ngày tạo

        // Xử lý upload ảnh
        if (!photoFile.isEmpty()) {
            String uploadDir = "H:\\FPT Polytechnic\\SPRING_25\\SOF3022_JAVA5\\ASSJAVA5_PD10303\\assjava5\\src\\main\\resources\\static\\photo\\";
            String fileName = System.currentTimeMillis() + "_" + photoFile.getOriginalFilename();
            File saveFile = new File(uploadDir + fileName);
            try {
                saveFile.getParentFile().mkdirs();
                photoFile.transferTo(saveFile);
                item.setImage(fileName);
            } catch (IOException e) {
                e.printStackTrace();
                return "redirect:/admin?error=upload";
            }
        } else {
            item.setImage(existingProduct.getImage()); // Giữ ảnh cũ nếu không upload
        }

        productDAO.save(item);
        return "redirect:/admin"; // Chuyển về trang danh sách
    }

    // Xóa sản phẩm
    @RequestMapping(value = "/admin/delete", method = RequestMethod.POST)
    public String delete(@RequestParam("id") int id) {
        Optional<Product> productOpt = productDAO.findById(id);
        if (productOpt.isPresent()) {
            productDAO.deleteById(id);
            return "redirect:/admin";
        }
        return "redirect:/admin?error=notfound";
    }

    // Xóa mapping không cần thiết
    // @RequestMapping "“/admin/edit/{id}” đã bị xóa vì dùng modal
}