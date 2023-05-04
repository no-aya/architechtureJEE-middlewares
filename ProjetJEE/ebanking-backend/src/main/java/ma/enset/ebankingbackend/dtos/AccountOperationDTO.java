package ma.enset.ebankingbackend.dtos;

import lombok.Data;
import ma.enset.ebankingbackend.enums.OperationType;

import java.util.Date;


@Data
public class AccountOperationDTO {
    private Long ID;
    private Date operationDate;
    private double amount;
    private String description;
    private OperationType type;
}
