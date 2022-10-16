package dev.dipesh.account.controller;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.dipesh.account.business.AccountService;
import dev.dipesh.account.models.Account;

@RestController
@RequestMapping("/api/accounts")
public class AccountApi {
    private AccountService accountService;

    public AccountApi(AccountService accountService) {
        this.accountService = accountService;
    }
    
    @GetMapping
    public ResponseEntity<List<Account>> getAllAccounts(){
        List<Account> listAccounts = accountService.listAllAccounts();
        if(listAccounts.isEmpty()){
            return ResponseEntity.noContent().build();
        }else{
            return new ResponseEntity<>(listAccounts,HttpStatus.OK);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Account> getAccountById(@PathVariable("id") Integer id){
        try{
            Account account = accountService.geAccountById(id);
            return new ResponseEntity<>(account, HttpStatus.OK);
        } catch(NoSuchElementException ex) {
            return ResponseEntity.notFound().build();
        }
    } 

}
