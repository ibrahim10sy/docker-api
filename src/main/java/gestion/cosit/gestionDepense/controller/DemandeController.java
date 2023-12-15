package gestion.cosit.gestionDepense.controller;

import gestion.cosit.gestionDepense.Exception.NoContentException;
import gestion.cosit.gestionDepense.model.Demande;
import gestion.cosit.gestionDepense.service.DemandeService;
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
@RequestMapping("/Demande")
public class DemandeController {

    @Autowired
    private DemandeService demandeService;

    @PostMapping("/create")
    @Operation(summary = "Création de la demande")
    public ResponseEntity<Demande> createDemande(@RequestBody Demande demande){
        return new ResponseEntity<>(demandeService.saveDemande(demande), HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    @Operation(summary = "Mis à jours de la demande")
    public ResponseEntity<Demande> updateDemandes(@RequestBody Demande demande, @PathVariable long id){
        return new ResponseEntity<>(demandeService.updateDemande(demande,id), HttpStatus.OK);
    }

    @PutMapping("/approuveDemandeByDirecteur/{id}")
    @Operation(summary = "Autorisation de la demande par le directeur")
    public ResponseEntity<Demande> approuveDemandes(@RequestBody Demande demande, @PathVariable long id){
        return new ResponseEntity<>(demandeService.approuveByDirecteur(demande,id), HttpStatus.OK);
    }

    @PutMapping("/approuveDemandeByAdmin/{id}")
    @Operation(summary = "Autorisation de la demande par l'admin")
    public ResponseEntity<Demande> approuveDemandesByAdmin(@RequestBody Demande demande, @PathVariable long id){
        return new ResponseEntity<>(demandeService.approuveByAdmin(demande,id), HttpStatus.OK);
    }

    @GetMapping("read/{idPersonnel}")
    @Operation(summary = "Liste des demande ")
    public ResponseEntity<List<Demande>> getAllByPersonnel(@PathVariable long idUtilisateur){
        return new ResponseEntity<>(demandeService.getAllDemandeByIdUtilisateur(idUtilisateur), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Suppression des demandes")
    @ResponseStatus(HttpStatus.OK) // Indique le code de statut HTTP pour une suppression réussie
    public String supprimerDemande(@PathVariable long idDemande) {
        try {
            return demandeService.deleteDemande(idDemande);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Demande non trouvée avec l'ID spécifié", e);
        } catch (NoContentException e) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, e.getMessage(), e);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Une erreur interne s'est produite", e);
        }
    }
}

