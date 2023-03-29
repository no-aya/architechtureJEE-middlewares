package ma.enset.hospital.repositories;

import ma.enset.hospital.entities.Patient;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;

public interface PatientRepository extends JpaRepository<Patient, Long> {

    Patient findByNom(String nom); //On suppose que le nom est unique
    Page<Patient> findByNomContains(String mc, Pageable pageable);
}
