package assjava5.assjava5.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import assjava5.assjava5.DAO.CategoryDAO;
import assjava5.assjava5.DAO.ProductDAO;
import assjava5.assjava5.Entity.Category;
import assjava5.assjava5.Entity.Report;
import assjava5.assjava5.Entity.ReportImpl;

import java.util.List;

@Controller
public class ReportController {

    @Autowired
    private ProductDAO productDAO;

    @Autowired
    private CategoryDAO categoryDAO;

    @RequestMapping("/statistics")
    public String showStatistics(@RequestParam(value = "category", required = false) String categoryId, Model model) {
        List<Category> categories = categoryDAO.findAll();
        model.addAttribute("categories", categories);

        if (categoryId != null && !categoryId.isEmpty()) {
            List<Report> statistics = productDAO.getStatisticsByCategoryId(categoryId);
            if (!statistics.isEmpty()) {
                model.addAttribute("selectedItem", statistics.get(0));
            } else {
                model.addAttribute("selectedItem", new ReportImpl());
            }
        } else {
            model.addAttribute("selectedItem", new ReportImpl());
        }

        return "admin/statistical";
    }

}
