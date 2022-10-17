package dev.dipesh.account.controller;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.dipesh.account.business.AccountService;
import dev.dipesh.account.models.Account;
//for linkTo method is hateaos
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;;

@RestController
@RequestMapping("/api/accounts")
public class AccountApi {
    private AccountService accountService;

    public AccountApi(AccountService accountService) {
        this.accountService = accountService;
    }
    
    
    //returns a collection of account model with links
    @GetMapping
    public  ResponseEntity<CollectionModel<Account>> getAllAccounts(){
        List<Account> listAccounts = accountService.listAllAccounts();
        if(listAccounts.isEmpty()){
            return ResponseEntity.noContent().build();
        }

        //for each individual account in collection
        for(Account account : listAccounts){
            //hateos Representation (ControllerClass, method) generates links for self relation
            account.add(linkTo(methodOn(AccountApi.class).getAccountById(account.getId())).withSelfRel());
            //generates collection relation links
            account.add(linkTo(methodOn(AccountApi.class).getAllAccounts()).withRel(IanaLinkRelations.COLLECTION));
        }

        //create collection model of lists
        CollectionModel<Account> accountModel = CollectionModel.of(listAccounts); 
        accountModel.add(linkTo(methodOn(AccountApi.class).getAllAccounts()).withSelfRel());
        
        return new ResponseEntity<>(accountModel,HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Account> getAccountById(@PathVariable("id") Integer id){
        try{
            Account account = accountService.geAccountById(id);
            
            //hateos Representation (ControllerClass, method) generates links for self relation
            account.add(linkTo(methodOn(AccountApi.class).getAccountById(account.getId())).withSelfRel());
            //generates collection relation links
            account.add(linkTo(methodOn(AccountApi.class).getAllAccounts()).withRel(IanaLinkRelations.COLLECTION));


            return new ResponseEntity<>(account, HttpStatus.OK);
        } catch(NoSuchElementException ex) {
            return ResponseEntity.notFound().build();
        }
    } 

}
