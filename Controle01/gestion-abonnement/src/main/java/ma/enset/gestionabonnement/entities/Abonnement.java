package ma.enset.gestionabonnement.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;
import ma.enset.gestionabonnement.enums.TypeAbonnement;

import java.util.Date;

@Entity
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Abonnement {
    //Un abonnement est défini par : son id, la date d’abonnement, le type d’abonnement
    //(GSM, INTERNET, TELEPHONE_FIXE), son solde, et le montant mensue
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Temporal(TemporalType.DATE)
    private Date dateAbonnement;
    @Enumerated(EnumType.STRING)
    private TypeAbonnement typeAbonnement;
    private double solde;
    private double montantMensuel;
    //Un abonnement est associé à un client

    @ManyToOne
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Client client;
}
