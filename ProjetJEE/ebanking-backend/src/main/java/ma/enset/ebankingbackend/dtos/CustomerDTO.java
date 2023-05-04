package ma.enset.ebankingbackend.dtos;

import lombok.Data;


@Data
public class CustomerDTO {
    //ID, firstName, lastName, email, phoneNumber, bankAccounts
    private Long ID;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
}
