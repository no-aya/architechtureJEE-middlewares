package ma.enset.gestionabonnement.dao;

import ma.enset.gestionabonnement.entities.Abonnement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AbonnementRepository extends JpaRepository<Abonnement, Long> {
}
