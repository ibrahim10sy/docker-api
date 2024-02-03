package gestion.cosit.gestionDepense.controller;

import com.fasterxml.jackson.databind.json.JsonMapper;
import gestion.cosit.gestionDepense.model.Admin;
import gestion.cosit.gestionDepense.service.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/Admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @PostMapping("/create")
    @Operation(summary = "Création de compte admin")
    public ResponseEntity<Admin> createAdmin(@Valid @RequestParam("admin") String adminString,
                                             @RequestParam(value = "images", required = false) MultipartFile multipartFile) throws Exception {

        Admin admin = new Admin();
        try{
            admin = new JsonMapper().readValue(adminString, Admin.class);
        }catch (Exception e){
            throw new Exception(e.getMessage());
        }
        Admin saveAdmin = adminService.createAdmin(admin, multipartFile);
        return new ResponseEntity<>(saveAdmin,  HttpStatus.CREATED);
    }

    @PutMapping("update/{id}")
    @Operation(summary = "Mise à jour")
    public ResponseEntity<Admin> updateAdmin(@PathVariable Long id,@Valid @RequestParam("admin") String adminString,
                                             @RequestParam(value = "images", required = false) MultipartFile multipartFile) throws Exception {
        Admin admin = new Admin();
        try{
            admin = new JsonMapper().readValue(adminString,Admin.class);
        }catch (Exception e){
            throw new Exception(e.getMessage());
        }
        Admin updateAdmin = adminService.updateAdmin(admin,id,multipartFile);
        return new ResponseEntity<>(updateAdmin, HttpStatus.OK);
    }

    @GetMapping("/read/{id}")
    @Operation(summary = "Affichage d'un admin")
    public ResponseEntity<Admin> getAdminById(@Valid @PathVariable long id){
        return new ResponseEntity<>(adminService.getAdminById(id), HttpStatus.OK);
    }
    @GetMapping("/list")
    @Operation(summary = "Affichage des admin")
    public ResponseEntity<List<Admin>> getAdminById(){
        return new ResponseEntity<>(adminService.getAllAdmin(), HttpStatus.OK);
    }
    @DeleteMapping("/delete")
    @Operation(summary = "Suppression d'un admin")
    public ResponseEntity<String> deleteAdmin(@PathVariable long id){
        return new ResponseEntity<>(adminService.deleteAdmin(id), HttpStatus.OK);
    }

    /*  @PostMapping("/login")
      @Operation(summary = "Connexion au compte admin")
      public Object connexion(@RequestParam("email") String email, @RequestParam("motDePasse") String motDePasse) throws AuthenticationException {
          return adminService.connexion(email,motDePasse);
      }*/
    @PostMapping("/login")
    @Operation(summary = "Connexion de l'admin")
    public Object connexion(@RequestParam("email") String email,
                            @RequestParam("passWord") String passWord) {
        return adminService.connection(email, passWord);
    }
}
