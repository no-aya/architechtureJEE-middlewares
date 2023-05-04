package ma.enset.ebankingbackend.web;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.enset.ebankingbackend.dtos.AccountHistoryDTO;
import ma.enset.ebankingbackend.dtos.AccountOperationDTO;
import ma.enset.ebankingbackend.dtos.BankAccountDTO;
import ma.enset.ebankingbackend.exceptions.BankAccountNotFoundException;
import ma.enset.ebankingbackend.services.BankAccountService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@Slf4j
@CrossOrigin("*")
public class BankAccountRESTContoller {
    private BankAccountService bankAccountService;

    @GetMapping("/bankAccounts/{ID}")
    public BankAccountDTO getBankAccount(@PathVariable String ID) throws BankAccountNotFoundException {
        return bankAccountService.getBankAccount(ID);
    }

    @GetMapping("/bankAccounts")
    public List<BankAccountDTO> listBankAccounts() {
        return bankAccountService.listBankAccounts();
    }


    @GetMapping("/bankAccounts/{ID}/history")
    public List<AccountOperationDTO> getHistory(@PathVariable String ID) throws BankAccountNotFoundException {
        return bankAccountService.accountOperationsHistory(ID);
    }

    @GetMapping("/bankAccounts/{ID}/page/{page}")
    public AccountHistoryDTO getAccountHistory(@PathVariable String ID,
                                                     @RequestParam(name = "page",defaultValue = "0")int page,
                                                     @RequestParam(name = "size",defaultValue = "5") int size) throws BankAccountNotFoundException {
        return bankAccountService.accountOperationsHistory(ID, page, size);
    }
}
