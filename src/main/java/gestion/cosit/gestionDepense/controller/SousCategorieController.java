package gestion.cosit.gestionDepense.controller;


import gestion.cosit.gestionDepense.Exception.NoContentException;
import gestion.cosit.gestionDepense.model.CategorieDepense;
import gestion.cosit.gestionDepense.model.SousCategorie;
import gestion.cosit.gestionDepense.service.SousCategorieService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/SousCategorie")
public class SousCategorieController {

    @Autowired
    private SousCategorieService sousCategorieService;

    @PostMapping("/create")
    @Operation(summary = "Création de sous catégorie")
    public ResponseEntity<SousCategorie> createSousCategorie(@RequestBody SousCategorie sousCategorie){
        return new ResponseEntity<>(sousCategorieService.saveSousCategorie(sousCategorie), HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    @Operation(summary = "Modification de sous categorie")
    public ResponseEntity<SousCategorie> updateSousCategorie(@RequestBody SousCategorie sousCategorie, @PathVariable long id){
        return new ResponseEntity<>(sousCategorieService.modifierSousCategorie(sousCategorie,id), HttpStatus.OK);
    }

    @GetMapping("/liste/{idCategorieDepense}")
    @Operation(summary = "affichage des categories à travers l'id utilisateur")
    public ResponseEntity<List<SousCategorie>> listeSousCategorie(@PathVariable long idCategorieDepense){
        return  new ResponseEntity<>(sousCategorieService.getAllSousCategorieByUser(idCategorieDepense), HttpStatus.OK);
    }
    public String supprimerSousCategorie(@PathVariable long idSousCategorie) {
        try {
            return sousCategorieService.supprimer(idSousCategorie);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Catégorie non trouvée avec l'ID spécifié", e);
        } catch (NoContentException e) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, e.getMessage(), e);
        } catch (Exception e) {
            // Gérer d'autres exceptions non prévues ici
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Une erreur interne s'est produite", e);
        }
    }

}