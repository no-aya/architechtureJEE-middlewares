package ma.enset.gestionabonnement.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;

@Entity
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Client {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nom;
    private String email;
    private String username;

    //Un client peut avoir plusieurs abonnements
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "client")
    private Collection<Abonnement> abonnements;

}
