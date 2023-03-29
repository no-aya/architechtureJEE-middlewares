package ma.enset.hospital.web;

import ma.enset.hospital.entities.Patient;
import ma.enset.hospital.repositories.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PatientRestContoller {
    @Autowired
    private PatientRepository patientRepository;
/*    @GetMapping("/index")
    public List<Patient> listPatients(){
        return patientRepository.findAll();
    }*/
    @GetMapping("/restpatients")
    public String chercher(Model model){
        List<Patient> patients = patientRepository.findAll();
        //On stock les patients dans le model
        model.addAttribute("patients", patients);
        return "patients";
    }
}

