package assjava5.assjava5.Controller;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import assjava5.assjava5.DAO.AccountDAO;
import assjava5.assjava5.Entity.Account;

@Controller
@RequestMapping("/userManage")
public class AccountController {
    @Autowired
    AccountDAO accountDAO;

    @GetMapping("")
    public String getCategory(Model model) {
        model.addAttribute("account", new Account());
        model.addAttribute("accounts", accountDAO.findAll());
        return "admin/userManage";
    }

    @GetMapping("/edit/{username}")
    public String edit(Model model, @PathVariable("username") String username) {
        Optional<Account> optionalAccount = accountDAO.findById(username);
        if (optionalAccount.isPresent()) {
            model.addAttribute("account", optionalAccount.get());
        } else {
            model.addAttribute("account", new Account());
        }
        model.addAttribute("accounts", accountDAO.findAll());
        return "admin/userManage";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute Account item, @RequestParam("photoFile") MultipartFile photoFile) {
        if (!photoFile.isEmpty()) {
            String uploadDir = "H:\\FPT Polytechnic\\SPRING_25\\SOF3022_JAVA5\\ASSJAVA5_PD10303\\assjava5\\src\\main\\resources\\static\\photo\\";
            String fileName = System.currentTimeMillis() + "_" + photoFile.getOriginalFilename();
            File saveFile = new File(uploadDir + fileName);
            try {
                photoFile.transferTo(saveFile);
                item.setPhoto(fileName);
            } catch (IOException e) {
                e.printStackTrace();
                return "redirect:/userManage?error=upload";
            }
        }
        accountDAO.save(item);
        return "redirect:/userManage";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute Account account,
            @RequestParam(value = "photoFile", required = false) MultipartFile photoFile,
            @RequestParam(value = "newPassword", required = false) String newPassword) {
        Optional<Account> optionalAccount = accountDAO.findById(account.getUsername());
        if (optionalAccount.isPresent()) {
            Account existingAccount = optionalAccount.get();
            if (newPassword == null || newPassword.isEmpty()) {
                account.setPassword(existingAccount.getPassword());
            } else {
                account.setPassword(newPassword);
            }
            if (photoFile == null || photoFile.isEmpty()) {
                account.setPhoto(existingAccount.getPhoto());
            } else {
                String uploadDir = "H:\\FPT Polytechnic\\SPRING_25\\SOF3022_JAVA5\\ASSJAVA5_PD10303\\assjava5\\src\\main\\resources\\static\\photo\\";
                String fileName = System.currentTimeMillis() + "_" + photoFile.getOriginalFilename();
                File saveFile = new File(uploadDir + fileName);
                try {
                    photoFile.transferTo(saveFile);
                    account.setPhoto(fileName);
                } catch (IOException e) {
                    e.printStackTrace();
                    return "redirect:/userManage?error=upload";
                }
            }

            accountDAO.save(account);
        }
        return "redirect:/userManage";
    }

    @GetMapping("/delete/{username}")
    public String delete(@PathVariable("username") String username) {
        if (accountDAO.existsById(username)) {
            accountDAO.deleteById(username);
        }
        return "redirect:/userManage";
    }
}
