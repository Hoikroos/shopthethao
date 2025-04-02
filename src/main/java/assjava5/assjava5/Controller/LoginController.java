package assjava5.assjava5.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import assjava5.assjava5.DAO.AccountDAO;
import assjava5.assjava5.Entity.Account;
import assjava5.assjava5.Service.CookieService;
import assjava5.assjava5.Service.ParamService;
import assjava5.assjava5.Service.SessionService;

@Controller
public class LoginController {
    @Autowired
    CookieService cookieService;

    @Autowired
    ParamService paramService;

    @Autowired
    SessionService sessionService;

    @Autowired
    AccountDAO accountDAO;

    @GetMapping("/login")
    public String loginForm(Model model) {
        String rememberedUser = cookieService.getValue("user");
        model.addAttribute("rememberedUser", rememberedUser);
        return "home/login";
    }

    @PostMapping("/login")
    public String loginProcess(Model model) {
        String usernameOrEmail = paramService.getString("username", "");
        String password = paramService.getString("password", "");
        boolean remember = paramService.getBoolean("remember", false);
        Account account = accountDAO.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail);

        if (account != null) {
            if (!account.isActivated()) {
                model.addAttribute("error", "Tài khoản chưa được kích hoạt. Vui lòng kiểm tra email.");
                return "home/login";
            }
            if (account.getPassword().equals(password)) {
                sessionService.set("username", account.getUsername());
                sessionService.set("admin", account.isAdmin());
                sessionService.set("photo", account.getPhoto());
                sessionService.set("fullname", account.getFullname());
                System.out.println(
                        "🔹 Đăng nhập thành công: " + account.getUsername() + ", isAdmin: " + account.isAdmin());

                if (remember) {
                    cookieService.add("user", account.getUsername(), 10);
                } else {
                    cookieService.remove("user");
                }
                return "redirect:/home";
            }
        }
        model.addAttribute("error", "Tên đăng nhập hoặc mật khẩu sai, vui lòng nhập lại.");
        return "home/login";
    }

    @GetMapping("/logout")
    public String logout() {
        sessionService.remove("username");
        sessionService.remove("admin");
        sessionService.remove("role");
        cookieService.remove("user");
        return "redirect:/home";
    }
}
