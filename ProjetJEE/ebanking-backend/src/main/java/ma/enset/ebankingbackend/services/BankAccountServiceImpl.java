package ma.enset.ebankingbackend.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.enset.ebankingbackend.entities.*;
import ma.enset.ebankingbackend.enums.AccountStatus;
import ma.enset.ebankingbackend.enums.OperationType;
import ma.enset.ebankingbackend.exceptions.BankAccountNotFoundException;
import ma.enset.ebankingbackend.exceptions.CustomerNotFoundException;
import ma.enset.ebankingbackend.exceptions.InsufficientBalanceException;
import ma.enset.ebankingbackend.repositories.AccountOperationRepository;
import ma.enset.ebankingbackend.repositories.BankAccountRepository;
import ma.enset.ebankingbackend.repositories.CustomerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
@AllArgsConstructor // Inject all the fields
// Inject a logger
@Slf4j //Equivalent to Logger log = LoggerFactory.getLogger(BankAccountServiceImpl.class);
public class BankAccountServiceImpl implements BankAccountService{
    private CustomerRepository customerRepository;
    private BankAccountRepository bankAccountRepository;
    private AccountOperationRepository accountOperationRepository;

    @Override
    public Customer saveCustomer(Customer customer) {
        log.info("Saving customer {}", customer);
        // Insert all the testing code here
        return customerRepository.save(customer);
    }

    @Override
    public CurrentAccount saveCurrentBankAccount(double balance, double overDraft, Long customerID) throws CustomerNotFoundException {
        log.info("Saving Current bank account with balance {}", balance);
        CurrentAccount bankAccount=new CurrentAccount();
        Customer customer = customerRepository.findById(customerID).orElse(null);
        if (customer == null) throw new CustomerNotFoundException("Customer not found");

        bankAccount.setID(UUID.randomUUID().toString());
        bankAccount.setRIB("00012"+customerID+"0001");
        bankAccount.setBalance(balance);
        bankAccount.setCreatedAt(new java.util.Date());
        bankAccount.setStatus(AccountStatus.CREATED);
        bankAccount.setOverDraft(overDraft);
        bankAccount.setCustomer(customerRepository.findById(customerID).orElse(null));
        return bankAccountRepository.save(bankAccount);
    }
    @Override
    public SavingAccount saveSavingBankAccount(double balance, double interest, Long customerID) throws CustomerNotFoundException {
        log.info("Saving Saving bank account with balance {}", balance);
        SavingAccount bankAccount=new SavingAccount();

        Customer customer = customerRepository.findById(customerID).orElse(null);
        if (customer == null) throw new CustomerNotFoundException("Customer not found");

        bankAccount.setID(UUID.randomUUID().toString());
        bankAccount.setRIB("00012"+customerID+"0001");
        bankAccount.setBalance(balance);
        bankAccount.setCreatedAt(new java.util.Date());
        bankAccount.setStatus(AccountStatus.CREATED);
        bankAccount.setInterestRate(interest);
        bankAccount.setCustomer(customerRepository.findById(customerID).orElse(null));
        return bankAccountRepository.save(bankAccount);
    }

    @Override
    public List<Customer> listCustomers() {
        return customerRepository.findAll();
    }

    @Override
    public BankAccount getBankAccount(String ID) throws BankAccountNotFoundException {
        BankAccount bankAccount=bankAccountRepository.findById(ID)
                .orElseThrow(()->new BankAccountNotFoundException("Bank account not found"));
        return bankAccount;
    }

    @Override
    public void deposit(String ID, double amount, String description) throws BankAccountNotFoundException {
        BankAccount bankAccount=bankAccountRepository.findById(ID)
                .orElseThrow(()->new BankAccountNotFoundException("Bank account not found"));

        //Create an operation<credit>
        AccountOperation accountOperation=new AccountOperation();
        accountOperation.setAmount(amount);
        accountOperation.setType(OperationType.CREDIT);
        accountOperation.setBankAccount(bankAccount);
        accountOperation.setDescription(description);
        accountOperation.setOperationDate(new java.util.Date());
        //Update the balance
        bankAccount.setBalance(bankAccount.getBalance()+amount);
        //Save the operation and the bank account
        accountOperationRepository.save(accountOperation);
        bankAccountRepository.save(bankAccount);

    }

    @Override
    public void withdraw(String ID, double amount, String description) throws BankAccountNotFoundException, InsufficientBalanceException {
        BankAccount bankAccount=bankAccountRepository.findById(ID)
                .orElseThrow(()->new BankAccountNotFoundException("Bank account not found"));
        if (bankAccount.getBalance()<amount)
           throw new InsufficientBalanceException("Insufficient balance");

        //Create an operation<debit>
        AccountOperation accountOperation=new AccountOperation();
        accountOperation.setAmount(amount);
        accountOperation.setType(OperationType.DEBIT);
        accountOperation.setBankAccount(bankAccount);
        accountOperation.setDescription(description);
        accountOperation.setOperationDate(new java.util.Date());
        //Update the balance
        bankAccount.setBalance(bankAccount.getBalance()-amount);
        //Save the operation and the bank account
        accountOperationRepository.save(accountOperation);
        bankAccountRepository.save(bankAccount);
    }

    @Override
    public void transfer(String IDSource, String IDDestination, double amount) throws BankAccountNotFoundException, InsufficientBalanceException {

        //Debit the source account
        deposit(IDSource,amount,"Transfer to "+IDDestination);
        //Credit the destination account
        withdraw(IDDestination,amount,"Transfer from "+IDSource);

    }
    @Override
    public List<BankAccount> listBankAccounts() {
        return bankAccountRepository.findAll();
    }
}
