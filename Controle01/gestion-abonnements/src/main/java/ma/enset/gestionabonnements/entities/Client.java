package ma.enset.gestionabonnements.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.Collection;

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

