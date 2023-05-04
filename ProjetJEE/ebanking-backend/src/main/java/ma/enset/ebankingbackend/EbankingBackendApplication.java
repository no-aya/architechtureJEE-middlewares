package ma.enset.ebankingbackend;

import ma.enset.ebankingbackend.dtos.CustomerDTO;
import ma.enset.ebankingbackend.entities.*;
import ma.enset.ebankingbackend.enums.AccountStatus;
import ma.enset.ebankingbackend.enums.OperationType;
import ma.enset.ebankingbackend.exceptions.BankAccountNotFoundException;
import ma.enset.ebankingbackend.exceptions.InsufficientBalanceException;
import ma.enset.ebankingbackend.mappers.BanKAccountMapperImpl;
import ma.enset.ebankingbackend.repositories.AccountOperationRepository;
import ma.enset.ebankingbackend.repositories.BankAccountRepository;
import ma.enset.ebankingbackend.repositories.CustomerRepository;
import ma.enset.ebankingbackend.services.BankAccountService;
import ma.enset.ebankingbackend.services.BankAccountServiceImpl;
import ma.enset.ebankingbackend.services.BankService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.UUID;
import java.util.stream.Stream;

@SpringBootApplication
public class EbankingBackendApplication {

    private BanKAccountMapperImpl dtoMapper=new BanKAccountMapperImpl();

    public static void main(String[] args) {
        SpringApplication.run(EbankingBackendApplication.class, args);
    }
/*Tests
     CommandLineRunner start(AccountOperationRepository accountOperationRepository,
                             BankAccountRepository bankAccountRepository,
                             CustomerRepository customerRepository) {
        return args -> {
            //Client : ID, firstName, lastName, email, phoneNumber, bankAccounts
            Stream.of("Hassan", "Youssef", "Mohamed", "Abdellah", "Fatima", "Amina", "Sara", "Najat", "Hajar", "Naima")
                    .forEach(name -> {
                        Customer customer = new Customer();
                        customer.setFirstName(name);
                        customer.setLastName("EL KADIRI");
                        customer.setEmail(name + "@gmail.com");
                        customer.setPhoneNumber("06 00 00 00 00");
                        customerRepository.save(customer);
                    });
            //CurrentAccount : ID, RIB, balance, createdAt, status, customer, accountOperations
            customerRepository.findAll().forEach(customer -> {
                CurrentAccount currentAccount = new CurrentAccount();
                currentAccount.setID(UUID.randomUUID().toString());
                currentAccount.setRIB("0012" + customer.getID());
                currentAccount.setBalance(Math.random() * 9000);
                currentAccount.setCreatedAt(new java.util.Date());
                currentAccount.setStatus(AccountStatus.CREATED);
                currentAccount.setCustomer(customer);
                currentAccount.setOverDraft(1000);
                bankAccountRepository.save(currentAccount);

                SavingAccount savingAccount = new SavingAccount();
                savingAccount.setID(UUID.randomUUID().toString());
                savingAccount.setRIB("0022" + customer.getID());
                savingAccount.setBalance(Math.random() * 9000);
                savingAccount.setCreatedAt(new java.util.Date());
                savingAccount.setStatus(AccountStatus.CREATED);
                savingAccount.setCustomer(customer);
                savingAccount.setInterestRate(3.5);
                bankAccountRepository.save(savingAccount);
            });


            //AccountOperation : ID, amount, date, type, bankAccount
            bankAccountRepository.findAll().forEach(bankAccount -> {
                for (int i=0; i<10; i++) {
                    AccountOperation accountOperation = new AccountOperation();
                    accountOperation.setAmount(Math.random() * 9000);
                    accountOperation.setOperationDate(new java.util.Date());
                    accountOperation.setType(Math.random() > 0.5 ? OperationType.DEBIT : OperationType.CREDIT);
                    accountOperation.setBankAccount(bankAccount);
                    accountOperationRepository.save(accountOperation);
                }
            });
        };
    }
    */
    @Bean
    CommandLineRunner start(BankAccountService bankAccountService) {
            return args -> {
                Stream.of("Hassan", "Youssef", "Mohamed", "Abdellah", "Fatima", "Amina", "Sara", "Najat", "Hajar", "Naima")
                        .forEach(name -> {
                            CustomerDTO customer = new CustomerDTO();
                            customer.setFirstName(name);
                            customer.setLastName("EL KADIRI");
                            customer.setEmail(name + "@gmail.com");
                            customer.setPhoneNumber("06 00 00 00 00");
                            bankAccountService.saveCustomer(customer);
                        });
                bankAccountService.listCustomers().forEach(customer -> {
                    try {
                        bankAccountService.saveCurrentBankAccount(Math.random() * 9000, 1000, customer.getID());
                        bankAccountService.saveSavingBankAccount(Math.random() * 9000, 3.5, customer.getID());
                        bankAccountService.listBankAccounts().forEach(bankAccount -> {
                            for (int i=0; i<10; i++) {
                                try {
                                    bankAccountService.deposit(bankAccount.getID(),Math.random() * 109000, "Deposit of today no " + i);
                                } catch (BankAccountNotFoundException e) {
                                    throw new RuntimeException(e);
                                }
                                try {
                                    bankAccountService.withdraw(bankAccount.getID(),Math.random() * 9000, "Withdraw of today no " + i);
                                } catch (BankAccountNotFoundException | InsufficientBalanceException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });

            };
    }
}
