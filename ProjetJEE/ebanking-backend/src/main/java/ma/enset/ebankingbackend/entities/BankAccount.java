package ma.enset.ebankingbackend.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.enset.ebankingbackend.enums.AccountStatus;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Data
@Inheritance(strategy = InheritanceType.JOINED)
//@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
// @DiscriminatorColumn(name = "account_type", discriminatorType = DiscriminatorType.STRING, length = 2)
@NoArgsConstructor @AllArgsConstructor
public class BankAccount {
    @Id
    private String ID;
    private String RIB;
    private double balance;
    private Date createdAt;
    @Enumerated(EnumType.STRING)
    private AccountStatus status;

    @ManyToOne
    private Customer customer;
    @OneToMany(mappedBy = "bankAccount", fetch = FetchType.LAZY)
    //LAZY: les opérations ne seront chargées que si on les demande
    //EAGER: les opérations seront chargées automatiquement
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<AccountOperation> accountOperations;

}
