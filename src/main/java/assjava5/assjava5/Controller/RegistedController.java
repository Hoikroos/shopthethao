package assjava5.assjava5.Controller;

import assjava5.assjava5.Entity.Account;
import assjava5.assjava5.Service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class RegistedController {

    @Autowired
    private AccountService accountService;

    @GetMapping("/register")
    public String showRegisterForm() {
        return "home/registed";
    }

    @PostMapping("/register")
    public String register(
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam String fullname,
            @RequestParam String email,
            Model model) {

        if (accountService.existsByUsername(username)) {
            model.addAttribute("error", "Tên người dùng đã tồn tại!");
            return "home/registed";
        }

        Account account = Account.builder()
                .username(username)
                .password(password)
                .fullname(fullname)
                .email(email)
                .activated(false)
                .admin(false)
                .build();

        accountService.register(account);
        model.addAttribute("message", "Đăng ký thành công! Vui lòng kiểm tra email để kích hoạt tài khoản của bạn.");
        return "home/registed";
    }

    @GetMapping("/activate")
    public String activate(@RequestParam("code") String code, Model model) {
        boolean activated = accountService.activateAccount(code);
        if (activated) {
            model.addAttribute("message", "Tài khoản đã được kích hoạt thành công");
        } else {
            model.addAttribute("error", "Mã kích hoạt không hợp lệ hoặc đã hết hạn.");
        }
        return "home/login";
    }
}
