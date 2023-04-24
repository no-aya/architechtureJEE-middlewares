package ma.enset.gestionabonnements.dao;

import ma.enset.gestionabonnements.entities.Abonnement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AbonnementRepository extends JpaRepository<Abonnement, Long> {
    Page<Abonnement> findByTypeAbonnementContains(String mc, Pageable pageable);
}
