package gestion.cosit.gestionDepense.controller;

import gestion.cosit.gestionDepense.model.SendNotification;
import gestion.cosit.gestionDepense.service.SendNotifService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api-depense/send")
public class NotifController {

    @Autowired
    private SendNotifService sendNotifService;

    @GetMapping("/list")
    public ResponseEntity<List<SendNotification>> listNotif(){
        return new ResponseEntity<>(sendNotifService.getAllNotif(), HttpStatus.OK);
    }

    @GetMapping("/read/{id}")
    public ResponseEntity<List<SendNotification>> listNotifByUser(@PathVariable long id){
        return new ResponseEntity<>(sendNotifService.getAllNotifByUser(id), HttpStatus.OK);
    }
    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Suppression d'une notification")
    public  String delete(@PathVariable long id){
        return sendNotifService.supprimer(id);
    }
//    /* @Autowired
}
