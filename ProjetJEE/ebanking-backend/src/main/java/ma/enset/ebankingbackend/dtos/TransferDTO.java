package ma.enset.ebankingbackend.dtos;

import lombok.Data;

@Data
public class TransferDTO {
    private String accountIDSource;
    private String accountIDDestination;
    private double amount;
    private String description;
}
