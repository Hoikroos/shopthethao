package assjava5.assjava5.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import assjava5.assjava5.DAO.AccountDAO;
import assjava5.assjava5.Entity.Account;
import assjava5.assjava5.Service.SessionService;

import java.util.Optional;

@Controller
public class ChangePassController {

    @Autowired
    AccountDAO accountDAO;

    @Autowired
    SessionService sessionService;

    @GetMapping("/changePassword")
    public String changePasswordForm(Model model) {
        String username = sessionService.get("username", "");
        if (username == null || username.isEmpty()) {
            return "redirect:/login";
        }

        Optional<Account> accountOpt = accountDAO.findByUsername(username);
        if (accountOpt.isEmpty()) {
            return "redirect:/login";
        }

        model.addAttribute("username", accountOpt.get().getUsername());
        return "home/changePassword";
    }

    @PostMapping("/changePassword")
    public String changePassword(
            @RequestParam String currentPassword,
            @RequestParam String newPassword,
            @RequestParam String confirmPassword,
            Model model) {

        String username = sessionService.get("username", "");
        if (username == null || username.isEmpty()) {
            model.addAttribute("error", "Bạn chưa đăng nhập.");
            return "redirect:/login";
        }

        Optional<Account> accountOpt = accountDAO.findByUsername(username);
        if (accountOpt.isEmpty()) {
            model.addAttribute("error", "Tài khoản không tồn tại.");
            return "home/changePassword";
        }

        Account account = accountOpt.get();

        if (!account.getPassword().equals(currentPassword)) {
            model.addAttribute("error", "Mật khẩu hiện tại không đúng.");
            return "home/changePassword";
        }

        if (!newPassword.equals(confirmPassword)) {
            model.addAttribute("error", "Mật khẩu xác nhận không khớp.");
            return "home/changePassword";
        }

        account.setPassword(newPassword);
        accountDAO.save(account);

        sessionService.remove("username");
        model.addAttribute("success", "Đổi mật khẩu thành công! Vui lòng đăng nhập lại.");

        return "redirect:/login";
    }
}
