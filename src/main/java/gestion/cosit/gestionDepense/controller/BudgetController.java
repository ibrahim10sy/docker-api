package gestion.cosit.gestionDepense.controller;

import gestion.cosit.gestionDepense.model.Budget;
import gestion.cosit.gestionDepense.model.Bureau;
import gestion.cosit.gestionDepense.model.CategorieDepense;
import gestion.cosit.gestionDepense.repository.BudgetRepository;
import gestion.cosit.gestionDepense.service.BudgetService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/Budget")
public class BudgetController {

    @Autowired
    BudgetService budgetService;

    @Autowired
    BudgetRepository budgetRepository;

//    @PostMapping("/ajouter")
//    @Operation(summary = "Creation d'un budget")
//    public ResponseEntity<Budget> ajouterBudget(@Valid @RequestBody Budget budget) throws BadRequestException {
//        System.out.println("Budget controller "+budget);
//        return new ResponseEntity<>(budgetService.createBudget(budget), HttpStatus.OK);
//    }
    @PostMapping("/ajouter")
    public ResponseEntity<Budget> ajouterBudget(@Valid @RequestBody Budget budget) throws BadRequestException {
    System.out.println("Budget controller " + budget);

    Budget createdBudget = budgetService.createBudget(budget);
    return new ResponseEntity<>(createdBudget, HttpStatus.OK);
}
    @PostMapping("/allouer-mensuellement/{budgetId}")
    public ResponseEntity<String> allocateBudgetMonthlyManually(@PathVariable long budgetId) {
        try {
            budgetService.allocateBudgetMonthlyById(budgetId);
            return new ResponseEntity<>("Allocation mensuelle effectuée avec succès.", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace(); // Enregistrez l'exception
            return new ResponseEntity<>("Erreur lors de l'allocation mensuelle.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/modifier/{id}")
    @Operation(summary = "Modification d'un  d'un budget")
    public ResponseEntity<Budget> modifierBudget(@Valid @RequestBody Budget budget, @PathVariable long id){
        return  new ResponseEntity<>(budgetService.updateBudget(budget,id), HttpStatus.OK);
    }

    @GetMapping("/list")
    @Operation(summary = "Affichage la liste  des budgets")
    public ResponseEntity<List<Budget>> listeBudget(){
        return  new ResponseEntity<>(budgetService.allBudget(),HttpStatus.OK);
    }

    @GetMapping("/listeByUser/{idUtilisateur}")
    @Operation(summary = "affichage des budgets à travers l'id utilisateur")
    public ResponseEntity<List<Budget>> listeBudgetByUser(@PathVariable long idUtilisateur){
        return  new ResponseEntity<>(budgetService.getAllBudgetByUser(idUtilisateur), HttpStatus.OK);
    }

    @GetMapping("/listeByAdmin/{idAdmin}")
    @Operation(summary = "affichage des budgets à travers l'id admin")
    public ResponseEntity<List<Budget>> listeBudgetByAdmin(@PathVariable long idAdmin){
        return  new ResponseEntity<>(budgetService.getAllBudgetByAdmin(idAdmin), HttpStatus.OK);
    }
//    @GetMapping("/readByIdAdmin/{idAdmin}")
//    @Operation(summary = "Affichage la liste  des budgets par id user")
//    public ResponseEntity<List<Budget>> listeBudgetByAdmin(long idAdmin){
//        return  new ResponseEntity<>(budgetService.allBudgetByAdmin(idAdmin),HttpStatus.OK);
//    }
//    @GetMapping("")
//    @Operation(summary = "Liste des bureaux")
//    public ResponseEntity<List<Bureau>> listeBureau(){
//        return new ResponseEntity<>(bure auService.getAllBureau(), HttpStatus.OK);
//    }
    @GetMapping("/search")
    @Operation(summary = "Rechercher un budget par sa description")
    public ResponseEntity<List<Budget>> searchBudgetByDesc( @RequestParam("desc") String desc){
        return  new ResponseEntity<>(budgetService.searchBudget(desc),HttpStatus.OK);
    }

    @GetMapping("/trie")
    @Operation(summary = "Trier les budget par mois et annÃ©e")
    public ResponseEntity<List<Budget>> sortByMonthAndYear(@RequestParam("date") String date){
        return  new ResponseEntity<>(budgetService.sortBudgetByMonthAndYear(date),HttpStatus.OK);
    }
    @GetMapping("/sommeByUser/{idUtilisateur}")
    @Operation(summary = "Retourne la somme total de l'ensemble des budget")
    public ResponseEntity<HashMap<String,Object>> sommeTotalByUser(@PathVariable long idUtilisateur){
        return  new ResponseEntity<>(budgetService.sommeOfAllBudgetNotFinishByUtilisateur(idUtilisateur),HttpStatus.OK);
    }

    @GetMapping("/sommeByAdmin/{idAdmin}")
    @Operation(summary = "Retourne la somme total de l'ensemble des budget")
    public ResponseEntity<HashMap<String,Object>> sommeTotalByAdmin(@PathVariable long idAdmin){
        return  new ResponseEntity<>(budgetService.sommeOfAllBudgetNotFinishByAdmins(idAdmin),HttpStatus.OK);
    }
    @DeleteMapping("/supprimer/{idBudget}")
    @Operation(summary = "suppression d'un  budget")
    public ResponseEntity<String> supprimerBudget(@PathVariable long idBudget) throws BadRequestException {
        return new ResponseEntity<>(budgetService.deleteBudget(idBudget),HttpStatus.OK);
    }
}
