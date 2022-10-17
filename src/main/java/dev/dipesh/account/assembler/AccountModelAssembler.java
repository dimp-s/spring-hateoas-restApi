package dev.dipesh.account.assembler;

import org.springframework.context.annotation.Configuration;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.server.RepresentationModelAssembler;

import dev.dipesh.account.controller.AccountController;
import dev.dipesh.account.models.Account;
//important
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

//for refactoring HATEOS CODE
@Configuration
public class AccountModelAssembler implements RepresentationModelAssembler<Account, EntityModel<Account>>{

    //return an entity model of account and replace repeated HATEOAS codes from AccountController
    @Override
    public EntityModel<Account> toModel(Account account) {
        EntityModel<Account> accountModel = EntityModel.of(account);

        //hateos Representation (ControllerClass, method) generates links for self relation
        accountModel.add(linkTo(methodOn(AccountController.class).getAccountById(account.getId())).withSelfRel());
        //generates collection relation links
        accountModel.add(linkTo(methodOn(AccountController.class).getAllAccounts()).withRel(IanaLinkRelations.COLLECTION));
         //generate link to deposit operation
        accountModel.add(linkTo(methodOn(AccountController.class).deposit(account.getId(),null)).withRel("deposits"));
        //generate link to withdraw operation
        accountModel.add(linkTo(methodOn(AccountController.class).withdraw(account.getId(),null)).withRel("withdraw"));

        return accountModel;
    }
    

}
