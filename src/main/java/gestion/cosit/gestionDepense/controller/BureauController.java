package gestion.cosit.gestionDepense.controller;


import gestion.cosit.gestionDepense.model.Bureau;
import gestion.cosit.gestionDepense.service.BureauService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/Bureau")
public class BureauController {

    @Autowired
    private BureauService bureauService;

    @PostMapping("/create")
    @Operation(summary = "Création de bureau")
    public ResponseEntity<Bureau> createBureau(@RequestBody Bureau bureau){
        return new ResponseEntity<>(bureauService.saveBureau(bureau), HttpStatus.CREATED);
    }

    @GetMapping("/read")
    @Operation(summary = "Liste des bureaux")
    public ResponseEntity<List<Bureau>> listeBureau(){
        return new ResponseEntity<>(bureauService.getAllBureau(), HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    @Operation(summary = "Modification des bureaux")
    public ResponseEntity<Bureau> updateBureau(@RequestBody Bureau bureau, @PathVariable long id){
        return new ResponseEntity<>(bureauService.modifier(bureau,id), HttpStatus.OK);
    }

    @DeleteMapping("/Delete/{id}")
    @Operation(summary = "Suppression des bureaux")
    public ResponseEntity<String> deleteBureau(@PathVariable long id){
        return new ResponseEntity<>(bureauService.supprimer(id), HttpStatus.OK);
    }
}

