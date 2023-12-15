package gestion.cosit.gestionDepense.controller;

import gestion.cosit.gestionDepense.model.Depense;
import gestion.cosit.gestionDepense.service.DepenseService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/Depenses")
public class DepenseController {

    @Autowired
    private DepenseService depenseService;

    @PostMapping("/create")
    @Operation(summary = "création de dépense")
    public ResponseEntity<Depense> createDepense(@Valid @RequestBody Depense depense) throws BadRequestException {
        return new ResponseEntity<>(depenseService.saveDepense(depense), HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    @Operation(summary = "Modification d'un dépense")
    public ResponseEntity<Depense> updateDepenses(@Valid @RequestBody Depense depense, @PathVariable long id){
        return new ResponseEntity<>(depenseService.updateDepense(depense,id) , HttpStatus.OK);
    }

    @GetMapping("/listDemandeByUser/{id}")
    @Operation(summary = "Affichage  des dépenses par en fonction des  demande")
    public ResponseEntity<List<Depense>> getAllDepenseByDemande(@PathVariable long id){
        return  new ResponseEntity<>(depenseService.allDepenseByIdDemande(id), HttpStatus.OK);
    }

    @GetMapping("/listDepenseByUser/{id}")
    @Operation(summary = "Affichage  des dépenses par en fonction de user")
    public ResponseEntity<List<Depense>> getAllDepenseByUser(@PathVariable long id){
        return  new ResponseEntity<>(depenseService.allDepenseByIdUtilisateur(id), HttpStatus.OK);
    }

    @GetMapping("/read")
    @Operation(summary = "Affichage  des dépenses par en fonction des  demande")
    public ResponseEntity<List<Depense>> getAllDepenseByDemande(){
        return  new ResponseEntity<>(depenseService.allDepense(), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Suppression d'un dépense")
    public String deleteDepense(@PathVariable long id){
        return depenseService.deleteDepende(id);
    }
}
