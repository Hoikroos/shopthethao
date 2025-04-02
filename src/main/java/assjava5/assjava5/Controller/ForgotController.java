package assjava5.assjava5.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import assjava5.assjava5.Service.AccountService;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ForgotController {

    @Autowired
    private AccountService accountService;

    @GetMapping("/forgetPassword")
    public String getForget() {
        return "home/forgetPassword";
    }

    @PostMapping("/forgetPassword")
    public String processForgotPassword(
            @RequestParam("username") String username,
            @RequestParam("email") String email,
            Model model) {

        boolean success = accountService.resetPassword(username, email);

        if (success) {
            model.addAttribute("message", "Mật khẩu mới đã được gửi qua email!");
            model.addAttribute("messageType", "success");
        } else {
            model.addAttribute("message", "Không tìm thấy tài khoản với thông tin này.");
            model.addAttribute("messageType", "error");
        }

        return "redirect:/forgetPassword";
    }
}