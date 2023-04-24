package ma.enset.gestionabonnements.web;

import ma.enset.gestionabonnements.dao.ClientRepository;
import ma.enset.gestionabonnements.entities.Client;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api")
public class ClientRestController {
    private ClientRepository clientRepository;

    public ClientRestController(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @GetMapping("/clients")
    public List<Client> clients() {
        return clientRepository.findAll();
    }

    @GetMapping("/clients/{id}")
    public Client clients(Long id) {
        return clientRepository.findById(id).orElseThrow(() -> new RuntimeException("Client not found!"));
    }

    /*En utilisant la projection
    @PostMapping("/clients")
    public Client save(@RequestBody Client client) {
        return clientRepository.save(client);
    }

    @PutMapping("/clients/{id}")
    public Client update(Long id, @RequestBody Client client) {
        Client clientR = clientRepository.findById(id).orElseThrow();
        if (client.getNom() != null) clientR.setNom(client.getNom());
        if (client.getEmail() != null) clientR.setEmail(client.getEmail());

        return clientRepository.save(client);
    }
    */


}