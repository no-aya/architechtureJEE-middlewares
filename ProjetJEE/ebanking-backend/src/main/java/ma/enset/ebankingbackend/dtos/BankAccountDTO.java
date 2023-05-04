package ma.enset.ebankingbackend.dtos;


import lombok.Data;
import ma.enset.ebankingbackend.enums.AccountStatus;

import java.util.Date;


@Data
public class BankAccountDTO {
    private String ID;
    private String RIB;
    private double balance;
    private Date createdAt;
    private AccountStatus status;
    private CustomerDTO customer;
    private String type;
}
