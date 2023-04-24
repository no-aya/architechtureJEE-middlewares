package ma.enset.gestionabonnement.dao;

import ma.enset.gestionabonnement.entities.Client;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Long> {
    Page<Client> findByNomContains(String mc, Pageable pageable);

}
