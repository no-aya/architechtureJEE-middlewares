package ma.enset.hospital;

import ma.enset.hospital.entities.*;
import ma.enset.hospital.repositories.ConsultationRepository;
import ma.enset.hospital.repositories.MedecinRepository;
import ma.enset.hospital.repositories.PatientRepository;
import ma.enset.hospital.repositories.RendezVousRepository;
import ma.enset.hospital.services.IHospitalService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Date;
import java.util.stream.Stream;

@SpringBootApplication
public class HospitalApplication {

    public static void main(String[] args) {
        SpringApplication.run(HospitalApplication.class, args);
    }

    @Bean
    CommandLineRunner start(IHospitalService hospitalService,
                            PatientRepository patientRepository, MedecinRepository medecinRepository, RendezVousRepository rendezVousRepository, ConsultationRepository consultationRepository) {
        return args -> {
            Stream.of("Abdelhami", "Benmalek", "Charssi", "Danblok","Abdeljalil", "Baraka", "Hanane", "Samir","Samira","Fakir","Fatiha").forEach(nom -> {
                Patient p1 = new Patient();
                p1.setNom(nom);
                p1.setDateNaissance(new Date());
                p1.setMalade(false);
                hospitalService.savePatient(p1);
            });

            Stream.of("Abdeljalil", "Baraka", "Hanane", "Samir","Samira","Fakir","Fatiha").forEach(nom -> {
                Medecin m1 = new Medecin();
                m1.setNom(nom);
                m1.setEmail(nom.toLowerCase() + "@gmail.com");
                m1.setSpecialite(Math.random() > 0.5 ? "Cardiologue" : "Dentiste");
                hospitalService.saveMedecin(m1);
            });


            Patient p1 = patientRepository.findById(1L).get();
            Patient p2 = patientRepository.findByNom("Benmalek");

            Medecin m1 = medecinRepository.findById(1L).get();
            Medecin m2 = medecinRepository.findByNom("Baraka");

            RendezVous rdv1 = new RendezVous();
            rdv1.setDate(new Date());
            rdv1.setPatient(p1);
            rdv1.setMedecin(m1);
            rdv1.setStatus(StatusRDV.DONE);
            hospitalService.saveRendezVous(rdv1);

            Consultation c1 = new Consultation();
            c1.setDateConsultation(new Date());
            c1.setDiagnostic("Diagnostic 1");
            c1.setRendezVous(rdv1);
            hospitalService.saveConsultation(c1);
        };
    }
}
