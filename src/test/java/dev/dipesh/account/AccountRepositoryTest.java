package dev.dipesh.account;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import dev.dipesh.account.models.Account;
import dev.dipesh.account.repositories.AccountRepository;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Rollback(false)
public class AccountRepositoryTest {
    @Autowired private AccountRepository repository;

    @Test
    public void testAddAccount(){
        Account account = new Account("1234566", 100);
        Account savedAccount = repository.save(account);

        //assertions
        assertThat(savedAccount).isNotNull();
        assertThat(savedAccount.getId()).isGreaterThan(0);

    }

    @Test
    public void testDepositAmount(){
        Account account = new Account("1234566", 100);
        Account savedAccount = repository.save(account);
        repository.updateBalance(50, savedAccount.getId());
        
        //assertions
        Account updatedAccount = repository.findById(savedAccount.getId()).get();
        assertThat(updatedAccount.getBalance()).isEqualTo(150);
    }
}
