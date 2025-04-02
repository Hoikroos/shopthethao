package assjava5.assjava5.Service;

import java.util.Optional;
import java.util.Random;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import assjava5.assjava5.DAO.AccountDAO;
import assjava5.assjava5.Entity.Account;

@Service
public class AccountService {
    @Autowired
    AccountDAO accountDAO;

    @Autowired
    EmailService emailService;

    public boolean existsByUsername(String username) {
        return accountDAO.findById(username).isPresent();
    }

    public void save(Account account) {
        accountDAO.save(account);
    }

    public void register(Account account) {
        account.setActivated(false);
        String activationCode = UUID.randomUUID().toString();
        account.setActivationCode(activationCode);

        save(account);
        emailService.sendActivationEmail(account.getEmail(), activationCode);
    }

    public boolean activateAccount(String code) {
        Optional<Account> optionalAccount = accountDAO.findByActivationCode(code);
        if (optionalAccount.isPresent()) {
            Account account = optionalAccount.get();
            account.setActivated(true);
            account.setActivationCode(null);
            save(account);
            return true;
        }
        return false;
    }

    private String generateRandomPassword() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789@#$%&*!";
        StringBuilder password = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < 10; i++) {
            password.append(chars.charAt(random.nextInt(chars.length())));
        }
        return password.toString();
    }

    public boolean resetPassword(String username, String email) {
        Account account = accountDAO.findByUsernameOrEmail(username, email);

        if (account != null) {
            String newPassword = generateRandomPassword();
            account.setPassword(newPassword);
            accountDAO.save(account);
            emailService.sendNewPasswordEmail(email, newPassword);
            return true;
        }
        return false;
    }

    public Account findByUsername(String username) {
        Optional<Account> accountOpt = accountDAO.findByUsername(username);
        return accountOpt.orElse(null);
    }

}
