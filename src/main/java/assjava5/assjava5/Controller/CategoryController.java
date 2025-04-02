package assjava5.assjava5.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import assjava5.assjava5.DAO.CategoryDAO;
import assjava5.assjava5.Entity.Category;

@Controller
public class CategoryController {
    @Autowired
    CategoryDAO categoryDAO;

    @GetMapping("/category/index")
    public String getCategory(Model model) {
        Category item = new Category();
        model.addAttribute("item", item);
        List<Category> items = categoryDAO.findAll();
        model.addAttribute("items", items);
        return "admin/category";
    }

    @RequestMapping("/category/edit/{id}")
    public String edit(Model model, @PathVariable("id") String id) {
        Category item = categoryDAO.findById(id).get();
        model.addAttribute("item", item);

        List<Category> items = categoryDAO.findAll();
        model.addAttribute("items", items);
        return "admin/category";
    }

    @RequestMapping("/category/create")
    public String create(Category item) {
        categoryDAO.save(item);
        return "redirect:/category/index";
    }

    @RequestMapping("/category/update")
    public String update(Category item) {
        categoryDAO.save(item);
        return "redirect:/category/edit/" + item.getId();
    }

    @RequestMapping(value = "/category/delete", method = RequestMethod.POST)
    public String delete(@RequestParam("id") String id) {
        categoryDAO.deleteById(id);
        return "redirect:/category/index";
    }

}
