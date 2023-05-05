package ma.enset.ebankingbackend.dtos;

import lombok.Data;

@Data
public class WithdrawDTO {
    private String accountID;
    private double amount;
    private String description;
}
