package ma.enset.ebankingbackend.services;

import ma.enset.ebankingbackend.dtos.BankAccountDTO;
import ma.enset.ebankingbackend.dtos.CustomerDTO;
import ma.enset.ebankingbackend.entities.BankAccount;
import ma.enset.ebankingbackend.entities.CurrentAccount;
import ma.enset.ebankingbackend.entities.Customer;
import ma.enset.ebankingbackend.entities.SavingAccount;
import ma.enset.ebankingbackend.exceptions.BankAccountNotFoundException;
import ma.enset.ebankingbackend.exceptions.CustomerNotFoundException;
import ma.enset.ebankingbackend.exceptions.InsufficientBalanceException;

import java.util.List;

public interface BankAccountService {
    CustomerDTO saveCustomer(CustomerDTO customer);
    CurrentAccount saveCurrentBankAccount(double balance, double overDraft, Long customerID) throws CustomerNotFoundException;
    SavingAccount saveSavingBankAccount(double balance, double interestRate, Long customerID) throws CustomerNotFoundException;
    List<CustomerDTO> listCustomers();
    BankAccount getBankAccount(String id) throws BankAccountNotFoundException;
    void deposit(String id, double amount, String description) throws BankAccountNotFoundException;
    void withdraw(String id, double amount, String description) throws BankAccountNotFoundException, InsufficientBalanceException;
    void transfer(String idSource, String idDestination, double amount) throws BankAccountNotFoundException, InsufficientBalanceException;

    List<BankAccountDTO> listBankAccounts();

    CustomerDTO getCustomer(Long id) throws CustomerNotFoundException;
}
