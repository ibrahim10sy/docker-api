package gestion.cosit.gestionDepense.controller;

import gestion.cosit.gestionDepense.model.ParametreDepense;
import gestion.cosit.gestionDepense.service.ParametreDepenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/parametre")
public class ParametreDepenseController {

    @Autowired
    private ParametreDepenseService parametreDepenseService;

    @PostMapping("/AddParametre")
    public ResponseEntity<ParametreDepense> saveParametres(@RequestBody ParametreDepense parametreDepense){
        return new ResponseEntity<>(parametreDepenseService.saveParametre(parametreDepense) , HttpStatus.OK);
    }

    @PutMapping("/Update/{id}")
    public ResponseEntity<ParametreDepense> updateParametres(@RequestBody ParametreDepense parametreDepense,@PathVariable   long id){
        return new ResponseEntity<>(parametreDepenseService.updateParametre(parametreDepense,id), HttpStatus.OK) ;
    }

    @GetMapping("/read")
    public ResponseEntity<List<ParametreDepense>> getAllParametre(){
        return new ResponseEntity<>(parametreDepenseService.getAll(), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public  String deleteParametres(@PathVariable int id){
        return parametreDepenseService.deleteParametre(id);
    }
}
