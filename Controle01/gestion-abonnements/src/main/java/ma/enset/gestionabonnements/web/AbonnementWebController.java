package ma.enset.gestionabonnements.web;

import ma.enset.gestionabonnements.dao.AbonnementRepository;
import ma.enset.gestionabonnements.entities.Abonnement;
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
public class AbonnementWebController {
    private final AbonnementRepository abonnementRepository;

    public AbonnementWebController(AbonnementRepository abonnementRepository) {
        this.abonnementRepository = abonnementRepository;
    }
    @GetMapping("/abonnements")
    public String chercher(Model model, @RequestParam(name="page",defaultValue = "0") int page, @RequestParam(name="motCle",defaultValue = "") String motCle){
        Page<Abonnement> abonnements = abonnementRepository.findAll(PageRequest.of(page,5));
        model.addAttribute("abonnements",abonnements.getContent());
        model.addAttribute("pages",new int[abonnements.getTotalPages()]);
        model.addAttribute("currentPage",page);
        model.addAttribute("motCle",motCle);
        return "abonnements";
    }
    @GetMapping("/deleteAbonnement")
    public String deleteAbonnement(@RequestParam(name = "id") Long id){
        abonnementRepository.deleteById(id);
        return "redirect:/abonnements";
    }



}
