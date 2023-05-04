package ma.enset.ebankingbackend.mappers;

import ma.enset.ebankingbackend.dtos.*;
import ma.enset.ebankingbackend.entities.*;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

//We can use Mapstruct
// Mapstruct is a code generator that greatly simplifies the implementation of mappings between Java bean types based on a convention over configuration approach.
//https://mapstruct.org/


@Service
public class BanKAccountMapperImpl {
    public CustomerDTO fromCustomerToCustomerDTO(Customer customer) {
        CustomerDTO customerDTO = new CustomerDTO();
        /*customerDTO.setID(customer.getID());
        customerDTO.setFirstName(customer.getFirstName());
        customerDTO.setLastName(customer.getLastName());
        customerDTO.setEmail(customer.getEmail());
        customerDTO.setPhoneNumber(customer.getPhoneNumber());*/
        BeanUtils.copyProperties(customer, customerDTO);
        return customerDTO;
    }
    public Customer fromCustomerDTOToCustomer(CustomerDTO customerDTO) {
        Customer customer = new Customer();
        BeanUtils.copyProperties(customerDTO, customer);
        return customer;
    }

    public BankAccount fromCustomerDTOToBankAccount(CustomerDTO customerDTO) {
        BankAccount bankAccount = new BankAccount();
        BeanUtils.copyProperties(customerDTO, bankAccount);
        return bankAccount;
    }

    public BankAccountDTO fromBankAccountToBankAccountDTO(BankAccount bankAccount) {
        BankAccountDTO bankAccountDTO = new BankAccountDTO();
        BeanUtils.copyProperties(bankAccount, bankAccountDTO);
        return bankAccountDTO;
    }

    public CurrentAccountDTO fromCurrentAccountToCurrentAccountDTO(CurrentAccount bankAccount) {
        CurrentAccountDTO currentAccountDTO = new CurrentAccountDTO();
        BeanUtils.copyProperties(bankAccount, currentAccountDTO);
        currentAccountDTO.setCustomer(fromCustomerToCustomerDTO(bankAccount.getCustomer()));
        currentAccountDTO.setType(bankAccount.getClass().getSimpleName());
        return currentAccountDTO;
    }

    public CurrentAccount fromCurrentAccountDTOToCurrentAccount(CurrentAccountDTO currentAccountDTO) {
        CurrentAccount currentAccount = new CurrentAccount();
        BeanUtils.copyProperties(currentAccountDTO, currentAccount);
        currentAccount.setCustomer(fromCustomerDTOToCustomer(currentAccountDTO.getCustomer()));
        return currentAccount;
    }

    public SavingAccountDTO fromSavingAccountToSavingAccountDTO(SavingAccount bankAccount) {
        SavingAccountDTO savingAccountDTO = new SavingAccountDTO();
        BeanUtils.copyProperties(bankAccount, savingAccountDTO);
        savingAccountDTO.setCustomer(fromCustomerToCustomerDTO(bankAccount.getCustomer()));
        savingAccountDTO.setType(bankAccount.getClass().getSimpleName());
        return savingAccountDTO;
    }

    public BankAccount fromSavingAccountDTOToSavingAccount(SavingAccountDTO savingAccountDTO) {
        BankAccount savingAccount = new BankAccount();
        BeanUtils.copyProperties(savingAccountDTO, savingAccount);
        savingAccount.setCustomer(fromCustomerDTOToCustomer(savingAccountDTO.getCustomer()));
        return savingAccount;
    }

    public AccountOperationDTO fromAccountOperationToAccountOperationDTO(AccountOperation accountOperation) {
        AccountOperationDTO accountOperationDTO = new AccountOperationDTO();
        BeanUtils.copyProperties(accountOperation, accountOperationDTO);
        return accountOperationDTO;
    }

    public AccountOperation fromAccountOperationDTOToAccountOperation(AccountOperationDTO accountOperationDTO) {
        AccountOperation accountOperation = new AccountOperation();
        BeanUtils.copyProperties(accountOperationDTO, accountOperation);
        return accountOperation;
    }


}
