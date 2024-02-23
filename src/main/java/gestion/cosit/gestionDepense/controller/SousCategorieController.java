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
@RequestMapping("/api-depense/SousCategorie")
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
    @GetMapping("/list/{idCategoriedepense}")
    @Operation(summary = "liste des sous categories en fonction du categorie")
    public ResponseEntity<List<SousCategorie>> getSousCategorie(@PathVariable long idCategoriedepense){
        return new ResponseEntity<>(sousCategorieService.getAllSousCategorie(idCategoriedepense), HttpStatus.OK);
    }

    @GetMapping("/liste")
    @Operation(summary = "liste des sous categories en fonction")
    public ResponseEntity<List<SousCategorie>> getAllSousCategore(){
        return new ResponseEntity<>(sousCategorieService.getAllSousCat(), HttpStatus.OK);
    }
    @DeleteMapping("/SupprimerSousCategorie/{idSousCategorie}")
    @Operation(summary = "suppression d'une sous catégorie")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> supprimerSousCategories(@PathVariable long idSousCategorie) {
        return  new ResponseEntity<>(sousCategorieService.supprimer(idSousCategorie),HttpStatus.OK);
    }

}