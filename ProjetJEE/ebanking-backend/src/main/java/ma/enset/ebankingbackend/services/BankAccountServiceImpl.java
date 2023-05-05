package ma.enset.ebankingbackend.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.enset.ebankingbackend.dtos.*;
import ma.enset.ebankingbackend.entities.*;
import ma.enset.ebankingbackend.enums.AccountStatus;
import ma.enset.ebankingbackend.enums.OperationType;
import ma.enset.ebankingbackend.exceptions.BankAccountNotFoundException;
import ma.enset.ebankingbackend.exceptions.CustomerNotFoundException;
import ma.enset.ebankingbackend.exceptions.InsufficientBalanceException;
import ma.enset.ebankingbackend.mappers.BanKAccountMapperImpl;
import ma.enset.ebankingbackend.repositories.AccountOperationRepository;
import ma.enset.ebankingbackend.repositories.BankAccountRepository;
import ma.enset.ebankingbackend.repositories.CustomerRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor // Inject all the fields
// Inject a logger
@Slf4j //Equivalent to Logger log = LoggerFactory.getLogger(BankAccountServiceImpl.class);
public class BankAccountServiceImpl implements BankAccountService{
    private CustomerRepository customerRepository;
    private BankAccountRepository bankAccountRepository;
    private AccountOperationRepository accountOperationRepository;

    private BanKAccountMapperImpl dtoMapper;
    @Override
    public CustomerDTO saveCustomer(CustomerDTO customerDTO) {
        log.info("Saving customer {}", customerDTO);
        Customer customer = dtoMapper.fromCustomerDTOToCustomer(customerDTO);
        customerRepository.save(customer);
        return dtoMapper.fromCustomerToCustomerDTO(customer);
    }

    @Override
    public List<CustomerDTO> searchCustomers(String keyword) {
        log.info("Searching customers with keyword {}", keyword);
        return customerRepository.findByFirstNameContainsOrLastNameContains(keyword,keyword).stream().map(dtoMapper::fromCustomerToCustomerDTO).collect(Collectors.toList());
    }

    @Override
    public CurrentAccountDTO saveCurrentBankAccount(double balance, double overDraft, Long customerID) throws CustomerNotFoundException {
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
        return dtoMapper.fromCurrentAccountToCurrentAccountDTO(bankAccountRepository.save(bankAccount));
    }
    @Override
    public SavingAccountDTO saveSavingBankAccount(double balance, double interest, Long customerID) throws CustomerNotFoundException {
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

        return dtoMapper.fromSavingAccountToSavingAccountDTO(bankAccountRepository.save(bankAccount));
    }

    @Override
    public List<CustomerDTO> listCustomers() {
        List<Customer> customers=customerRepository.findAll();
        /*Programmation impérative
        List<CustomerDTO> customerDTOS=new ArrayList<>();
        for (Customer customer:customers){
            customerDTOS.add(dtoMapper.fromCustomerToCustomerDTO(customer));
        }
        */
         //Programmation fonctionnelle
        return customers.stream().map(dtoMapper::fromCustomerToCustomerDTO).collect(Collectors.toList());
    }

    @Override
    public BankAccountDTO getBankAccount(String ID) throws BankAccountNotFoundException {
        BankAccount bankAccount=getBankAccountEntity(ID);
        bankAccount.setAccountOperations(accountOperationRepository.findByBankAccount(bankAccount));
        if (bankAccount instanceof CurrentAccount)
            return dtoMapper.fromCurrentAccountToCurrentAccountDTO((CurrentAccount) bankAccount);
        else
            return dtoMapper.fromSavingAccountToSavingAccountDTO((SavingAccount) bankAccount);
    }

    //Get the bank account entity from the database
    private BankAccount getBankAccountEntity(String ID) throws BankAccountNotFoundException {
        return bankAccountRepository.findById(ID)
                .orElseThrow(()->new BankAccountNotFoundException("Bank account not found"));
    }

    @Override
    public void deposit(String ID, double amount, String description) throws BankAccountNotFoundException {
        BankAccount bankAccount=getBankAccountEntity(ID);

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
        BankAccount bankAccount=getBankAccountEntity(ID);

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
        withdraw(IDSource,amount,"Transfer to "+IDDestination);
        //Credit the destination account
        deposit(IDDestination,amount,"Transfer from "+IDSource);

    }
    @Override
    public List<BankAccountDTO> listBankAccounts() {
        List<BankAccount> bankAccounts=bankAccountRepository.findAll();
        return bankAccounts.stream().map(bankAccount -> {
            if (bankAccount instanceof CurrentAccount)
                return dtoMapper.fromCurrentAccountToCurrentAccountDTO((CurrentAccount) bankAccount);
            else
                return dtoMapper.fromSavingAccountToSavingAccountDTO((SavingAccount) bankAccount);
        }).collect(Collectors.toList());
    }

    @Override
    public CustomerDTO getCustomer(Long id) throws CustomerNotFoundException {
        Customer customer=customerRepository.findById(id).orElseThrow(()->new CustomerNotFoundException("Customer not found"));
        return dtoMapper.fromCustomerToCustomerDTO(customer);
    }

    @Override
    public CustomerDTO updateCustomer(CustomerDTO customerDTO) {
        log.info("Updating customer {}", customerDTO);
        Customer customer = dtoMapper.fromCustomerDTOToCustomer(customerDTO);
        customerRepository.save(customer);
        return dtoMapper.fromCustomerToCustomerDTO(customer);
    }

    @Override
    public void deleteCustomer(Long id) throws CustomerNotFoundException {
        Customer customer=customerRepository.findById(id).orElseThrow(()->new CustomerNotFoundException("Customer not found"));
        customerRepository.delete(customer);
    }

    @Override
    public List<AccountOperationDTO> accountOperationsHistory(String ID) throws BankAccountNotFoundException {
        BankAccount bankAccount=getBankAccountEntity(ID);
        List<AccountOperation> accountOperations=accountOperationRepository.findByBankAccountID(ID);
        return accountOperations.stream().map(dtoMapper::fromAccountOperationToAccountOperationDTO).collect(Collectors.toList());
    }

    @Override
    public AccountHistoryDTO accountOperationsHistory(String ID, int page, int size) throws BankAccountNotFoundException {
        BankAccount bankAccount = getBankAccountEntity(ID);
        //Page<AccountOperation> accountOperations = accountOperationRepository.findByBankAccountID(ID, PageRequest.of(page, size));
        //Order by date
        Page<AccountOperation> accountOperations = accountOperationRepository.findByBankAccountIDOrderByOperationDateDesc(ID, PageRequest.of(page, size));

        AccountHistoryDTO accountHistoryDTO = new AccountHistoryDTO();
        List<AccountOperationDTO>accountOperationsDTOs = accountOperations.getContent().stream().map(dtoMapper::fromAccountOperationToAccountOperationDTO).collect(Collectors.toList());
        accountHistoryDTO.setOperations(accountOperationsDTOs);
        accountHistoryDTO.setID(bankAccount.getID());
        accountHistoryDTO.setRIB(bankAccount.getRIB());
        accountHistoryDTO.setBalance(bankAccount.getBalance());
        accountHistoryDTO.setType(bankAccount instanceof CurrentAccount ? "Current Account" : "Saving Account");

        //pages
        accountHistoryDTO.setTotalPages(accountOperations.getTotalPages());
        accountHistoryDTO.setCurrentPage(page);
        accountHistoryDTO.setSize(size);


        return accountHistoryDTO;

        }

    @Override
    public List<BankAccountDTO> getCustomerBankAccounts(Long id) throws CustomerNotFoundException {
        Customer customer=customerRepository.findById(id).orElseThrow(()->new CustomerNotFoundException("Customer not found"));
        List<BankAccount> bankAccounts=bankAccountRepository.findByCustomerID(id);
        return bankAccounts.stream().map(bankAccount -> {
            if (bankAccount instanceof CurrentAccount)
                return dtoMapper.fromCurrentAccountToCurrentAccountDTO((CurrentAccount) bankAccount);
            else
                return dtoMapper.fromSavingAccountToSavingAccountDTO((SavingAccount) bankAccount);
        }).collect(Collectors.toList());
    }

}
