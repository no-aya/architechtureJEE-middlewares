package ma.enset.gestionabonnements.web;

import ma.enset.gestionabonnements.dao.ClientRepository;
import ma.enset.gestionabonnements.entities.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ClientWebController {
    private final ClientRepository clientRepository;

    public ClientWebController(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }
    @GetMapping("/index")
    public String chercher(Model model, @RequestParam(name="page",defaultValue = "0") int page, @RequestParam(name="motCle",defaultValue = "") String motCle){
        Page<Client> clients = clientRepository.findByNomContains(motCle, PageRequest.of(page,5));
        model.addAttribute("clients",clients.getContent());
        model.addAttribute("pages",new int[clients.getTotalPages()]);
        model.addAttribute("currentPage",page);
        model.addAttribute("motCle",motCle);
        return "clients";
    }
    @PostMapping("/saveClient")
    public String saveClient(@Validated Client client, BindingResult bindingResult){
        clientRepository.save(client);
        return "redirect:/index";
    }

    @GetMapping("/edit")
    public String editClient(@RequestParam(name = "id") Long id, Model model){
        Client client=clientRepository.findById(id).get();
        model.addAttribute("client",client);
        return "edit";
    }

    @GetMapping("/delete")
    public String deleteClient(@RequestParam(name = "id") Long id){
        clientRepository.deleteById(id);
        return "redirect:/index";
    }



}
