package ma.enset.gestionpatients;

import ma.enset.gestionpatients.entities.Patient;
import ma.enset.gestionpatients.repositories.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.Date;
import java.util.List;

@SpringBootApplication
public class GestionPatientsApplication implements CommandLineRunner {

    @Autowired
    private PatientRepository patientRepository;

    public static void main(String[] args) {
        SpringApplication. run(GestionPatientsApplication.class, args);
    }
    @Override
    public void run(String... args) throws Exception {
        for(int i=0;i<100;i++) {
            patientRepository.save(new Patient(null, "Patient "+i, new Date(), false, (int)Math.random()+10));
        }
        /*patientRepository.save(new Patient(null, "Mohamed", new Date(), false, 4));
        patientRepository.save(new Patient(null, "Ahmed", new Date(), true, 3));
        patientRepository.save(new Patient(null, "Ali", new Date(), false, 2));
        patientRepository.save(new Patient(null, "Youssef", new Date(), true, 1));*/


        System.out.println("Liste des patients");
        /*patientRepository.findAll().forEach(p->{
            System.out.println(p.getNom()+" "+p.getScore()+" "+p.isMalade()+" "+p.getDateNaissance());
        });*/


        // Pagination
        Page<Patient> patients = patientRepository.findAll(PageRequest.of(0, 5));
        System.out.println("Nombre de pages: "+patients.getTotalPages());
        System.out.println("Nombre de patients: "+patients.getTotalElements());
        System.out.println("Numéro de la page courante: "+patients.getNumber());
        patients.forEach(patient -> System.out.println(patient.getNom()+" "+patient.getScore()+" "+patient.isMalade()+" "+patient.getDateNaissance()));



        System.out.println("Modifier le statut du patient");
        Patient p = patientRepository.findById(1L).orElse(null);

        //Changer le statut du patient
        if(p!=null) {
            p.setMalade(true);
            System.out.println(p.getNom()+" "+p.getScore()+" "+p.isMalade()+" "+p.getDateNaissance());
            patientRepository.save(p);
        }

        System.out.println("Liste des patients malades");
        List<Patient> patient= patientRepository.findByMalade(true);;
        patient.forEach(p1->{
            System.out.println(p1.getNom()+" "+p1.getScore()+" "+p1.isMalade()+" "+p1.getDateNaissance());
        });

        System.out.println("Liste des patients malades par page");
        /*Page<Patient> patients1 = patientRepository.findByMalade(PageRequest.of(0, 5));
        System.out.println("Nombre de pages: "+patients1.getTotalPages());
        System.out.println("Nombre de patients: "+patients1.getTotalElements());
        System.out.println("Numéro de la page courante: "+patients1.getNumber());
        patients1.forEach(temp -> System.out.println(temp.getNom()+" "+temp.getScore()+" "+temp.isMalade()+" "+temp.getDateNaissance()));
*/

        System.out.println("Liste des patients malades et score < 5");
        List<Patient> patient2= patientRepository.chercherPatients("%1%", 5);
        //Affichage
        patient2.forEach(temp0 -> System.out.println(temp0.getId()+" "+ temp0.getScore()+" "+temp0.getNom()) );

        //Supprimer un patient
        System.out.println("Supprimer un patient");
        patientRepository.deleteById(1L);
        patientRepository.deleteByNom("Ali");
    }

}
