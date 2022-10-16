package dev.dipesh.account;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import dev.dipesh.account.models.Account;
import dev.dipesh.account.repositories.AccountRepository;
//managed by spring using following annotations
@Configuration
public class DatabaseLoader {
    private AccountRepository accountRepository;

    public DatabaseLoader(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Bean
    public CommandLineRunner initDatabase(){
        return args -> {
            Account account1 = new Account("123456", 100);
            Account account2 = new Account("234567", 300);
            Account account3 = new Account("345678", 1000);
            
            accountRepository.saveAll(List.of(account1,account2,account3));
            System.out.println("Database initialized!");
        };
    }

}
