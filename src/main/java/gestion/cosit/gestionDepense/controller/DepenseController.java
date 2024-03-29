package gestion.cosit.gestionDepense.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import gestion.cosit.gestionDepense.model.Budget;
import gestion.cosit.gestionDepense.model.Depense;
import gestion.cosit.gestionDepense.service.DepenseService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api-depense/Depenses")
public class DepenseController {

    @Autowired
    private DepenseService depenseService;


    @PostMapping("/createByUser")
    @Operation(summary = "création de dépense")
    public ResponseEntity<Depense> createDepenseByUser(
            @Valid @RequestParam("depense") String depenses,
            @RequestPart(value = "images", required = false) MultipartFile multipartFile
    ) throws Exception {
        Depense depense1 = new Depense();
        try {
            depense1 = new JsonMapper().readValue(depenses , Depense.class);
        }catch (JsonProcessingException e){
            throw new Exception(e.getMessage());
        }

        Depense saveDepenses = depenseService.saveDepenseByUser(depense1, multipartFile);
        return new ResponseEntity<>(saveDepenses, HttpStatus.CREATED);
    }
    @PostMapping("/createByAdmin")
    @Operation(summary = "création de dépense")
    public ResponseEntity<Depense> createDepense(
            @Valid @RequestParam("depense") String depenses,
            @RequestPart(value = "images", required = false)  MultipartFile multipartFile
            ) throws Exception {
        Depense depense1 = new Depense();
        try {
            depense1 = new JsonMapper().readValue(depenses , Depense.class);
        }catch (JsonProcessingException e){
            throw new Exception(e.getMessage());
        }

        Depense saveDepenses = depenseService.saveDepenseByAdmin(depense1, multipartFile);
        return new ResponseEntity<>(saveDepenses, HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    @Operation(summary = "Modification d'un dépense")
    public ResponseEntity<Depense> updateDepense(
            @Valid @RequestParam("depense") String depenses,
            @PathVariable long id,
            @RequestPart(value = "images", required = false)  MultipartFile multipartFile
    ) throws Exception {
        Depense depense1 = new Depense();
        try {
            depense1 = new JsonMapper().readValue(depenses , Depense.class);
        }catch (JsonProcessingException e){
            throw new Exception(e.getMessage());
        }

        Depense updateDepenses = depenseService.updateDepense(depense1,id, multipartFile);
        return new ResponseEntity<>(updateDepenses, HttpStatus.CREATED);
    }

    @PutMapping("marquer/{id}")
    @Operation(summary = "marquer comme view")
    public ResponseEntity<Depense> marquer(@PathVariable long id){
        return  new ResponseEntity<>(depenseService.marquerView(id),HttpStatus.OK);
    }
    @PutMapping("approuver/{id}")
    @Operation(summary = "marquer comme view")
    public ResponseEntity<Depense> approoverDepense(@PathVariable long id) throws Exception {
        return  new ResponseEntity<>(depenseService.validateDepenseByAdmin(id),HttpStatus.OK);
    }
    @GetMapping("/search")
    @Operation(summary = "Rechercher un budget par sa description")
    public ResponseEntity<List<Depense>> searchByDesc(@RequestParam("desc") String desc){
        return  new ResponseEntity<>(depenseService.searchDepense(desc),HttpStatus.OK);
    }

    @GetMapping("/trie")
    @Operation(summary = "Trier les budget par mois et annÃ©e")
    public ResponseEntity<List<Depense>> sortByMonthAndYear(@RequestParam("date") String date){
        return  new ResponseEntity<>(depenseService.sortBudgetByMonthAndYear(date),HttpStatus.OK);
    }
    @GetMapping("/sommeTotal/{idBudget}")
    public ResponseEntity<HashMap<String, Object>> getSommeTotalDepenses(@PathVariable long idBudget) {
        HashMap<String, Object> resultat = depenseService.sommeTotalDesDepenses(idBudget);
        return new ResponseEntity<>(resultat, HttpStatus.OK);
    }
//    @GetMapping("/somme/{idBudget}")
//    @Operation(summary = "Retourne la somme total des depenses  d'un budget")
//    public ResponseEntity<HashMap<String,Object>> sommeTotalByAdmin(@PathVariable long idBudget){
//        return  new ResponseEntity<>(depenseService.sommeOfAllDepense(idBudget),HttpStatus.OK);
//    }

    @GetMapping("/listeByBudget/{idBudget}")
    @Operation(summary = "Affichage des dépenses")
    public List<Depense> readByBudget(@PathVariable long idBudget){
        return depenseService.getDepenseByIdBudget(idBudget);
    }
//    @GetMapping("/listDemandeByUser/{idDemande}")
//    @Operation(summary = "Affichage  des dépenses  en fonction du  demande")
//    public ResponseEntity<List<Depense>> getAllDepenseByDemande(@PathVariable long idDemande){
//        return  new ResponseEntity<>(depenseService.allDepenseByIdDemande(idDemande), HttpStatus.OK);
//    }
    @GetMapping("/listByUser/{idUtilisateur}")
    @Operation(summary = "Affichage  des dépenses par en fonction de user")
    public ResponseEntity<List<Depense>> getAllDepenseByUser(@PathVariable long idUtilisateur){
        return  new ResponseEntity<>(depenseService.allDepenseByIdUser(idUtilisateur), HttpStatus.OK);
    }

    @GetMapping("/listDepenseByAdmin/{idAdmin}")
    @Operation(summary = "Affichage  des dépenses par en fonction de user")
    public ResponseEntity<List<Depense>> getAllDepenseByAdmin(@PathVariable long idAdmin){
        return  new ResponseEntity<>(depenseService.allDepenseByIdAdmin(idAdmin), HttpStatus.OK);
    }
    @GetMapping("/read")
    @Operation(summary = "Affichage  des dépenses par en fonction des  demande")
    public ResponseEntity<List<Depense>> getAllDepenseByDepense(){
        return  new ResponseEntity<>(depenseService.allDepense(), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Suppression d'un dépense")
    public String deleteDepense(@PathVariable long id){
        return depenseService.deleteDepende(id);
    }
}
