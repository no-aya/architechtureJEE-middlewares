package ma.enset.gestionabonnements;

import ma.enset.gestionabonnements.dao.AbonnementRepository;
import ma.enset.gestionabonnements.dao.ClientRepository;
import ma.enset.gestionabonnements.entities.Abonnement;
import ma.enset.gestionabonnements.entities.Client;
import ma.enset.gestionabonnements.enums.TypeAbonnement;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Date;
import java.util.stream.Stream;

@SpringBootApplication
public class GestionAbonnementsApplication {
	@Bean
	CommandLineRunner start(ClientRepository clientRepository, AbonnementRepository abonnementRepository) {
		return args -> {

			Stream.of("Abdelhami", "Benmalek", "Charssi", "Danblok").forEach(nom -> {
				Client c1 = new Client();
				c1.setNom(nom);
				c1.setEmail(nom.toLowerCase() + "@gmail.com");
				c1.setUsername(nom.toLowerCase()+"123");
				clientRepository.save(c1);
			});

			Stream.of(1000.0,20000.0,30000.0).forEach(montant -> {
				Abonnement a1 = new Abonnement();
				a1.setClient(clientRepository.findById(1L).get());
				a1.setSolde(1000.0);
				a1.setMontantMensuel(montant);
				a1.setDateAbonnement(new Date());
				a1.setTypeAbonnement(TypeAbonnement.GSM);
				abonnementRepository.save(a1);
			});
		};
	}

	public static void main(String[] args) {
		SpringApplication.run(GestionAbonnementsApplication.class, args);
	}

}
