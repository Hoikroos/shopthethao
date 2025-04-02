package assjava5.assjava5.DAO;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import assjava5.assjava5.Entity.Account;

public interface AccountDAO extends JpaRepository<Account, String> {
    Account findByUsernameOrEmail(String username, String email);

    Optional<Account> findByUsername(String username);

    Optional<Account> findByActivationCode(String activationCode);
}
