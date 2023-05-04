package ma.enset.ebankingbackend.dtos;

import lombok.Data;

@Data
public class CurrentAccountDTO extends BankAccountDTO {
    private double overDraft;
}
