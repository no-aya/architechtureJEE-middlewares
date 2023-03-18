package ma.enset.gestionpatients.repositories;
import ma.enset.gestionpatients.entities.Patient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;


public interface PatientRepository extends JpaRepository<Patient, Long>{
     List<Patient> findByMalade(boolean malade);
     void deleteByNom(String nom);

    //Page<Patient> findByMalade(Pageable pageable);
    List<Patient> findByMaladeAndScoreLessThan(boolean malade, int score);
    List<Patient> findByMaladeIsTrueOrScoreLessThan(int score);
    List<Patient> findByNomContains(String nom);
    List<Patient> findByDateNaissanceBetweenAndMaladeIsTrueOrNomLike(Date date1, Date date2, String nom);
    //Mais ce n'est pas une bonne pratique

    //Requête personnalisée
    @Query("select p from Patient p where p.nom like :x and p.score > :y")
    List<Patient> chercherPatients(@Param("x") String nom, @Param("y") int scoreMin);





}
