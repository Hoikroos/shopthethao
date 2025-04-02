package assjava5.assjava5.Controller;

import java.io.IOException;
import java.nio.file.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import assjava5.assjava5.Entity.Account;
import assjava5.assjava5.Service.AccountService;
import assjava5.assjava5.Service.CookieService;
import jakarta.servlet.http.HttpSession;

@Controller
public class EditController {
    private final AccountService accountService;
    private final CookieService cookieService;
    private final String uploadDir = "H:\\FPT Polytechnic\\SPRING_25\\SOF3022_JAVA5\\ASSJAVA5_PD10303\\assjava5\\src\\main\\resources\\static\\photo\\";

    @Autowired
    public EditController(AccountService accountService, CookieService cookieService) {
        this.accountService = accountService;
        this.cookieService = cookieService;
    }

    @GetMapping("/editProfile")
    public String userProfile(Model model, HttpSession session) {
        String username = (String) session.getAttribute("username");
        if (username == null || username.isEmpty()) {
            username = cookieService.getValue("user");
        }
        if (username == null || username.isEmpty()) {
            System.out.println("Không tìm thấy username từ session hoặc cookie!");
            return "redirect:/login";
        }
        System.out.println("🔍 Username từ session hoặc cookie: " + username);
        Account user = accountService.findByUsername(username);

        if (user != null) {
            model.addAttribute("user", user);
        } else {
            System.out.println("Không tìm thấy tài khoản: " + username);
            model.addAttribute("error", "Không tìm thấy thông tin người dùng!");
        }
        return "home/editProfile";
    }

    @PostMapping("/updateProfile")
    public String updateProfile(
            @ModelAttribute Account updatedUser,
            @RequestParam(value = "avatar", required = false) MultipartFile file,
            RedirectAttributes redirectAttributes,
            HttpSession session) {
        try {
            Account existingUser = accountService.findByUsername(updatedUser.getUsername());
            if (existingUser == null) {
                redirectAttributes.addFlashAttribute("errorMessage", "Không tìm thấy tài khoản!");
                return "redirect:/editProfile";
            }
            existingUser.setEmail(updatedUser.getEmail());
            existingUser.setFullname(updatedUser.getFullname());
            if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
                existingUser.setPassword(updatedUser.getPassword());
            }
            if (file != null && !file.isEmpty()) {
                String filename = updatedUser.getUsername() + "_" + file.getOriginalFilename();
                Path path = Paths.get(uploadDir, filename);
                if (!Files.exists(Paths.get(uploadDir))) {
                    Files.createDirectories(Paths.get(uploadDir));
                }
                if (existingUser.getPhoto() != null && !existingUser.getPhoto().isEmpty()) {
                    Path oldPath = Paths.get(uploadDir, existingUser.getPhoto());
                    Files.deleteIfExists(oldPath);
                    System.out.println("Ảnh cũ đã được xóa: " + existingUser.getPhoto());
                }
                Files.write(path, file.getBytes());
                existingUser.setPhoto(filename);
                System.out.println("Ảnh mới đã được lưu: " + filename);
            } else {
                System.out.println("Không có ảnh mới, giữ nguyên ảnh cũ: " + existingUser.getPhoto());
            }
            accountService.save(existingUser);
            session.setAttribute("username", existingUser.getUsername());
            session.setAttribute("fullname", existingUser.getFullname());
            session.setAttribute("email", existingUser.getEmail());
            session.setAttribute("photo", existingUser.getPhoto());
            redirectAttributes.addFlashAttribute("successMessage", "Cập nhật thành công!");
        } catch (IOException e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi khi cập nhật thông tin!");
        }
        return "redirect:/editProfile";
    }
}
