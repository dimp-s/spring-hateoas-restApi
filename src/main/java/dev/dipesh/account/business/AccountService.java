package dev.dipesh.account.business;

import java.util.List;

import javax.security.auth.login.AccountNotFoundException;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import dev.dipesh.account.models.Account;
import dev.dipesh.account.repositories.AccountRepository;

//for business logic
@Service
@Transactional
public class AccountService {
    private AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }
    

    public List<Account> listAllAccounts() {
        return accountRepository.findAll();
    }

    public Account getAccountById(Integer id){
        return accountRepository.findById(id).get();
    }

    public Account addAccount(Account account){
        return accountRepository.save(account);
    }

    public Account depositToAccount(float amount , Integer id){
        accountRepository.updateBalance(amount, id);
        return accountRepository.findById(id).get();
    }

    public Account withdrawFromAccount(float amount , Integer id){
        accountRepository.updateBalance(-amount, id);
        return accountRepository.findById(id).get();
    }

    public void deleteAccount(Integer id) throws AccountNotFoundException{
        if(!accountRepository.existsById(id))
            throw new AccountNotFoundException();
        accountRepository.deleteById(id);
    }

}
