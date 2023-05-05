package ma.enset.ebankingbackend.services;

import ma.enset.ebankingbackend.dtos.*;
import ma.enset.ebankingbackend.entities.BankAccount;
import ma.enset.ebankingbackend.exceptions.BankAccountNotFoundException;
import ma.enset.ebankingbackend.exceptions.CustomerNotFoundException;
import ma.enset.ebankingbackend.exceptions.InsufficientBalanceException;

import java.util.List;

public interface BankAccountService {
    CustomerDTO saveCustomer(CustomerDTO customer);
    CurrentAccountDTO saveCurrentBankAccount(double balance, double overDraft, Long customerID) throws CustomerNotFoundException;
    SavingAccountDTO saveSavingBankAccount(double balance, double interestRate, Long customerID) throws CustomerNotFoundException;
    List<CustomerDTO> listCustomers();
    BankAccountDTO getBankAccount(String id) throws BankAccountNotFoundException;
    void deposit(String id, double amount, String description) throws BankAccountNotFoundException;
    void withdraw(String id, double amount, String description) throws BankAccountNotFoundException, InsufficientBalanceException;
    void transfer(String idSource, String idDestination, double amount) throws BankAccountNotFoundException, InsufficientBalanceException;

    List<BankAccountDTO> listBankAccounts();

    CustomerDTO getCustomer(Long id) throws CustomerNotFoundException;

    CustomerDTO updateCustomer(CustomerDTO customerDTO);

    void deleteCustomer(Long id) throws CustomerNotFoundException;
    List<AccountOperationDTO> accountOperationsHistory(String ID) throws BankAccountNotFoundException;

    AccountHistoryDTO accountOperationsHistory(String id, int page, int size) throws BankAccountNotFoundException;

    List<CustomerDTO> searchCustomers(String keyword);

    List<BankAccountDTO> getCustomerBankAccounts(Long id) throws CustomerNotFoundException;
}
