package gestion.cosit.gestionDepense.controller;

import gestion.cosit.gestionDepense.Exception.NoContentException;
import gestion.cosit.gestionDepense.model.CategorieDepense;
import gestion.cosit.gestionDepense.model.SousCategorie;
import gestion.cosit.gestionDepense.service.CategorieService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/Categorie")
public class CategorieDepenseController {

    @Autowired
    private CategorieService categorieService;

    @PostMapping("/create")
    @Operation(summary = "Création de catégorie")
    public ResponseEntity<CategorieDepense> createCategorie(@RequestBody CategorieDepense categorieDepense){
        return new ResponseEntity<>(categorieService.saveCategorie(categorieDepense), HttpStatus.CREATED);
    }

    @GetMapping("/lire")
    @Operation(summary = "affichage des categories")
    public List<CategorieDepense> lire(){
        return categorieService.lire();
    }

    @GetMapping("/listeByUser/{idUtilisateur}")
    @Operation(summary = "affichage des categories à travers l'id utilisateur")
    public ResponseEntity<List<CategorieDepense>> listeCategorieByUser(@PathVariable long idUtilisateur){
        return  new ResponseEntity<>(categorieService.getAllCategorieDepenseByUser(idUtilisateur), HttpStatus.OK);
    }

    @GetMapping("/list/{idCategorieDepense}")
    @Operation(summary = "affichage des sous categories à travers l'id du catégorie")
    public ResponseEntity<List<SousCategorie>> listeSousCategorie(@PathVariable long idCategorieDepense){
        return  new ResponseEntity<>(categorieService.listSousCategorie(idCategorieDepense), HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    @Operation(summary = "Modification d'un dépense")
    public ResponseEntity<CategorieDepense> updateDepenses(@Valid @RequestBody CategorieDepense categorieDepense, @PathVariable long id){
        return new ResponseEntity<>(categorieService.modifierCategorieDepense(categorieDepense,id) , HttpStatus.OK);
    }
    @DeleteMapping("/SupprimerCategorie/{idCategorieDepense}")
    @Operation(summary = "suppression d'une catégorie")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> supprimerCategories(@PathVariable long idCategorieDepense) {
      return  new ResponseEntity<>(categorieService.deleteCategorieById(idCategorieDepense),HttpStatus.OK);
    }

//    @DeleteMapping("/Supprimer/{idCategorie}")
//    @Operation(summary = "suppression d'une catégorie")
//    @ResponseStatus(HttpStatus.OK) // Indique le code de statut HTTP pour une suppression réussie
//    public String supprimer(@PathVariable long idCategorieDepense) {
//        try {
//            return categorieService.supprimer(idCategorieDepense);
//        } catch (EntityNotFoundException e) {
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Catégorie non trouvée avec l'ID spécifié", e);
//        } catch (NoContentException e) {
//            throw new ResponseStatusException(HttpStatus.NO_CONTENT, e.getMessage(), e);
//        } catch (Exception e) {
//            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Une erreur interne s'est produite", e);
//        }
//    }
}
