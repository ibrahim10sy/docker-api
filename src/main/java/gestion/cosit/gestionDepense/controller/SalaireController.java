package gestion.cosit.gestionDepense.controller;

import gestion.cosit.gestionDepense.Exception.NoContentException;
import gestion.cosit.gestionDepense.model.Salaire;
import gestion.cosit.gestionDepense.repository.SalaireRepository;
import gestion.cosit.gestionDepense.service.SalaireService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/Salaire")
public class SalaireController {

    @Autowired
    private SalaireService salaireService;

    @PostMapping("/create")
    @Operation(summary = "Création de salaire")
    public ResponseEntity<Salaire> createSalaire(@RequestBody Salaire salaire){
        return new ResponseEntity<>(salaireService.saveSalaire(salaire), HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    @Operation(summary = "Modification d'un salaire")
    public ResponseEntity<Salaire> updateSalaire(@RequestBody Salaire salaire,@PathVariable long id){
        return new ResponseEntity<>(salaireService.updateSalaire(salaire,id), HttpStatus.OK);
    }

    @GetMapping("/read")
    @Operation(summary = "Liste des salaires")
    public ResponseEntity<List<Salaire>> getAll(){
        return new ResponseEntity<>(salaireService.getAllSalaire(),HttpStatus.OK);
    }

    @GetMapping("/liste/{id}")
    @Operation(summary = "Liste des salaires")
    public ResponseEntity<List<Salaire>> getAllUser(@PathVariable long id){
        return new ResponseEntity<>(salaireService.getAllSalaireByUser(id),HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Suppression d'un salaire")
    public String supprimerSalaire(@PathVariable long idSalaire) {
        try {
            return salaireService.deleteSalaire(idSalaire);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Salaire non trouvée avec l'ID spécifié", e);
        } catch (NoContentException e) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, e.getMessage(), e);
        } catch (Exception e) {
            // Gérer d'autres exceptions non prévues ici
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Une erreur interne s'est produite", e);
        }
    }
}
