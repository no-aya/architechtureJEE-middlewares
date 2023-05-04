package ma.enset.ebankingbackend.services;

import ma.enset.ebankingbackend.entities.BankAccount;
import ma.enset.ebankingbackend.entities.CurrentAccount;
import ma.enset.ebankingbackend.entities.SavingAccount;
import ma.enset.ebankingbackend.repositories.BankAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class BankService {
    @Autowired
    private BankAccountRepository bankAccountRepository;
    public void consulter(){
        BankAccount bankAccount = bankAccountRepository.findById("1825b6db-fe95-4331-afb5-caaeaa0f650e").orElse(null);
        System.out.println("Balance :" + bankAccount.getBalance());
        System.out.println("First Name :"+bankAccount.getCustomer().getFirstName());
        if (bankAccount instanceof CurrentAccount) {
            CurrentAccount currentAccount = (CurrentAccount) bankAccount;
            System.out.println("OverDraft : "+currentAccount.getOverDraft());
        }else if (bankAccount instanceof SavingAccount) {
            SavingAccount savingAccount = (SavingAccount) bankAccount;
            System.out.println("Interest Rate :"+savingAccount.getInterestRate());
        }
        System.out.println("Amount\tType\tDate");
        System.out.println("-------------------------");
        bankAccount.getAccountOperations().forEach(accountOperation -> {
            System.out.println(accountOperation.getAmount()+"\t"+accountOperation.getType()+"\t"+accountOperation.getOperationDate());
        });
    }
}
