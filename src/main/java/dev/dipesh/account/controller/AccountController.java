package dev.dipesh.account.controller;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import javax.security.auth.login.AccountNotFoundException;

import org.hibernate.EntityMode;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.dipesh.account.assembler.AccountModelAssembler;
import dev.dipesh.account.business.AccountService;
import dev.dipesh.account.models.Account;
import dev.dipesh.account.models.Amount;

//for linkTo method is hateaos
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {
    private AccountService accountService;
    //for refactoring hateoas repeated codes
    private AccountModelAssembler assembler;

    public AccountController(AccountService accountService,AccountModelAssembler assembler) {
        this.accountService = accountService;
        this.assembler = assembler;
    }
    
    
    //returns a collection of account model with links
    @GetMapping
    public  ResponseEntity<CollectionModel<EntityModel<Account>>> getAllAccounts(){
        List<Account> listAccounts = accountService.listAllAccounts();
        if(listAccounts.isEmpty()){
            return ResponseEntity.noContent().build();
        }

        // //for each individual account in listAccounts collection create links
        // for(Account account : listAccounts){
        //     // hateos Representation (ControllerClass, method) generates links for self relation
        //     // account.add(linkTo(methodOn(AccountController.class).getAccountById(account.getId())).withSelfRel());
        //     // generates collection relation links
        //     // account.add(linkTo(methodOn(AccountController.class).getAllAccounts()).withRel(IanaLinkRelations.COLLECTION));
        //     // generate link to deposit operation
        //     // account.add(linkTo(methodOn(AccountController.class).deposit(account.getId(),null)).withRel("deposits"));
        //     // generate link to withdraw operation
        //     // account.add(linkTo(methodOn(AccountController.class).withdraw(account.getId(),null)).withRel("withdraw"));        
        // }
        
        //generate HATEOAS links using assembler entitymodel
        //create collection model of lists
        CollectionModel<EntityModel<Account>> collectionModel = assembler.toCollectionModel(listAccounts);
        
        return new ResponseEntity<>(collectionModel,HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Account>> getAccountById(@PathVariable("id") Integer id){
        try{
            Account account = accountService.getAccountById(id);
            
            //generate HATEOAS links using assembler entitymodel
            EntityModel<Account> entityModel = assembler.toModel(account);   
            return new ResponseEntity<>(entityModel, HttpStatus.OK);
        } catch(NoSuchElementException ex) {
            return ResponseEntity.notFound().build();
        }
    } 

    @PostMapping
    public ResponseEntity<EntityModel<Account>> addAccount(@RequestBody Account account){
        Account savedAccount = accountService.addAccount(account);

        //generate HATEOAS links using assembler entitymodel
        EntityModel<Account> entityModel = assembler.toModel(account);                 

        //??not sure maybe puts header link reference to loaction wrt id
        return ResponseEntity.created(linkTo(methodOn(AccountController.class)
        .getAccountById(savedAccount.getId()))
        .toUri())
        .body(entityModel);
    }

    @PutMapping
    public ResponseEntity<EntityModel<Account>> updateAccount(@RequestBody Account account){
        Account updatedAccount = accountService.addAccount(account);
        //generate HATEOAS links using assembler entitymodel
        EntityModel<Account> entityModel = assembler.toModel(updatedAccount);           
        
        return new ResponseEntity<>(entityModel, HttpStatus.OK);
    }

    //deposit an amount to a specific account by id {PARTIAL UPDATE}

    @PatchMapping("/{id}/deposit")
    public ResponseEntity<EntityModel<Account>> deposit(@PathVariable("id") Integer id, @RequestBody Amount amount) {
        Account updatedAccount = accountService.depositToAccount(amount.getAmount(),id);
        
        //generate HATEOAS links using assembler entitymodel
        EntityModel<Account> entityModel = assembler.toModel(updatedAccount); 

        return new ResponseEntity<>(entityModel, HttpStatus.OK);
    }

    @PatchMapping("/{id}/withdraw")
    public ResponseEntity<EntityModel<Account>> withdraw(@PathVariable("id") Integer id, @RequestBody Amount amount) {
        Account updatedAccount = accountService.withdrawFromAccount(amount.getAmount(),id);
        
        //generate HATEOAS links using assembler entitymodel
        EntityModel<Account> entityModel = assembler.toModel(updatedAccount); 

        return new ResponseEntity<>(entityModel, HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteAccount(@PathVariable("id") Integer id) {
        try{
            accountService.deleteAccount(id);
            return ResponseEntity.noContent().build();
        }catch(AccountNotFoundException ex){
            return ResponseEntity.notFound().build();
        }

    }

}
