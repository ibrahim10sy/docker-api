package gestion.cosit.gestionDepense.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import gestion.cosit.gestionDepense.Exception.NoContentException;
import gestion.cosit.gestionDepense.model.CategorieDepense;
import gestion.cosit.gestionDepense.model.SendNotification;
import gestion.cosit.gestionDepense.model.Utilisateur;
import gestion.cosit.gestionDepense.service.UtilisateurService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@AllArgsConstructor
@RestController
@CrossOrigin
@RequestMapping("/api-depense/utilisateur")
public class UtilisateurController {

    private UtilisateurService utilisateurService;





    @PostMapping("/create")
    @Operation(summary = "Création d'un utilisateur")
    public ResponseEntity<Utilisateur> createUtilisateur(
            @Valid @RequestParam("utilisateur") String utilisateurString,
            @RequestPart(value = "images", required = false) MultipartFile multipartFile) throws Exception {

        Utilisateur utilisateur = new Utilisateur();
        try{
            utilisateur = new JsonMapper().readValue(utilisateurString, Utilisateur.class);
        }catch(JsonProcessingException e){
            throw new Exception(e.getMessage());
        }

        Utilisateur savedUser = utilisateurService.createUser(utilisateur,multipartFile);
        System.out.println("User controller"+savedUser);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }

    @PutMapping("update/{id}")
    @Operation(summary = "Mise à jour d'un utilisateur")
    public ResponseEntity<Utilisateur> updateUser( @PathVariable Long id,@Valid @RequestParam("utilisateur") String utilisateurString,
                                                   @RequestParam(value ="images", required=false) MultipartFile multipartFile) throws Exception{
        Utilisateur utilisateur = new Utilisateur();
        try {
            utilisateur = new JsonMapper().readValue(utilisateurString, Utilisateur.class);
        } catch (JsonProcessingException e) {
            throw new Exception(e.getMessage());
        }

        Utilisateur updateUser = utilisateurService.updateUtilisateur(utilisateur, id, multipartFile);

        return new ResponseEntity<>(updateUser, HttpStatus.OK);
    }

//    @GetMapping("/read")
//    @Operation(summary = "Affichages utilisateurs")
//    public  ResponseEntity<List<Utilisateur>> getUtilisateur(){
//        return  new ResponseEntity<>(utilisateurService.getAllUtisateur(), HttpStatus.OK);
//    }
@GetMapping("/liste")
@Operation(summary = "affichage des users")
public List<Utilisateur> listUser(){
    return utilisateurService.lire();
}
    @GetMapping("/read/{id}")
    @Operation(summary = "Affichage d'un utilisateurs")
    public ResponseEntity<Utilisateur> getUtilisateurById(@Valid @PathVariable long id){
        return new ResponseEntity<>(utilisateurService.getUtilisateurById(id),HttpStatus.OK) ;
    }
    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Suppression d'un utilisateur")
    public ResponseEntity<String> deleteUtilisateur(@PathVariable long id){
        return new ResponseEntity<>(utilisateurService.deleteUserById(id), HttpStatus.OK);
    }

    @PostMapping("/login")
    @Operation(summary = "Connexion d'un utilisateur")
    public Object connexion(@RequestParam("email") String email,
                            @RequestParam("passWord") String passWord) {
        return utilisateurService.connection(email, passWord);
    }

    @PostMapping("/newPassWord")
    @Operation(summary = "reunitialiser un mot de passe d'un utilisateur")
    public Object reuinitialiser(@RequestParam("email") String email,
                            @RequestParam("passWord") String passWord) {
        return utilisateurService.reinitialiserMotDePasse(email,passWord);
    }
}