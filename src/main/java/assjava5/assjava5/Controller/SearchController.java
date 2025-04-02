package assjava5.assjava5.Controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import assjava5.assjava5.DAO.ProductDAO;
import assjava5.assjava5.Entity.Product;
import assjava5.assjava5.Service.SessionService;

@Controller
@RequestMapping("/product")
public class SearchController {
    @Autowired
    private ProductDAO productDAO;

    @Autowired
    private SessionService session;

    @GetMapping("/search")
    public String search(Model model, @RequestParam("keywords") Optional<String> kw) {
        String kwords = kw.orElse(session.get("keywords", ""));
        session.set("keywords", kwords);

        List<Product> products = productDAO.findByKeywords("%" + kwords + "%");
        model.addAttribute("products", products);
        model.addAttribute("keywords", kwords);
        return "home/search";
    }
}
