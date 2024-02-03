package gestion.cosit.gestionDepense.service;

import gestion.cosit.gestionDepense.Exception.NoContentException;
import gestion.cosit.gestionDepense.model.*;
import gestion.cosit.gestionDepense.repository.DemandeRepository;
import gestion.cosit.gestionDepense.repository.DepenseRepository;
import gestion.cosit.gestionDepense.repository.SendNotifRepository;
import gestion.cosit.gestionDepense.repository.UtilisateurRepository;
import jakarta.persistence.EntityNotFoundException;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.concurrent.CompletableFuture;


@Service
public class SendNotifService {
    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private SendNotifRepository sendNotifRepository;
    @Autowired
    private DepenseRepository depenseRepository;

    @Value("cosit162@gmail.com")
    String sender;


    public CompletableFuture<Void> sendDemandeAsync(Depense depense) {
        return CompletableFuture.runAsync(() -> {
            String msg;
            SendNotification sendNotif = new SendNotification();
            Utilisateur utilisateur = depense.getUtilisateur();
            Budget budget = depense.getBudget();
            System.out.println("User dans send service" + utilisateur);
            Admin admin = budget.getAdmin();
            System.out.println("Admin dans send " + admin);

            SimpleMailMessage mailMessage = new SimpleMailMessage();

            msg = "Bonjour M/Mme " + admin.getNom().toUpperCase() + " " + admin.getPrenom().toUpperCase() +
                    ".\n Vous avez reçu une nouvelle demande de la part de  Mr " +
                    utilisateur.getNom().toUpperCase() + " " + utilisateur.getPrenom().toUpperCase();

            try {
                // Sauvegarder l'entité Demande d'abord
                depenseRepository.save(depense);

                System.out.println("Debut de l'envoie dans le service demande");
                sendNotif.setMessage(msg);
                mailMessage.setFrom(sender);
                mailMessage.setTo(admin.getEmail());
                mailMessage.setText(sendNotif.getMessage());
                mailMessage.setSubject("Alerte demande");

                javaMailSender.send(mailMessage);

                Date dateEnvoie = new Date();
                Instant instant = dateEnvoie.toInstant();
                ZonedDateTime zonedDateTime = instant.atZone(ZoneId.systemDefault());
                sendNotif.setUtilisateur(utilisateur);
                sendNotif.setAdmin(admin);
                sendNotif.setDate(dateEnvoie);
                sendNotif.setDepense(depense);
                System.out.println("Fin de l'envoie dans le service demande");

                sendNotifRepository.save(sendNotif);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    public CompletableFuture<Void> sendDepenseAsync(Depense depense) {
        return CompletableFuture.runAsync(() -> {
            String msg1;
            SendNotification sendNotif = new SendNotification();
            Utilisateur utilisateur = depense.getUtilisateur();
            Budget budget = depense.getBudget();
            System.out.println("User dans send service" + utilisateur);
            Admin admin = budget.getAdmin();
            System.out.println("Admin dans send " + admin);

            SimpleMailMessage mailMessage = new SimpleMailMessage();

            msg1 = "Nouvelle dépense d'une somme de " + depense.getMontantDepense() +
                    " de la part de  M/Mme " + utilisateur.getNom().toUpperCase() +
                    " " + utilisateur.getPrenom().toUpperCase();

            try {
                // Sauvegarder l'entité Demande d'abord
                depenseRepository.save(depense);

                System.out.println("Debut de l'envoie ");
                sendNotif.setMessage(msg1);
                mailMessage.setFrom(sender);
                mailMessage.setTo(admin.getEmail());
                mailMessage.setText(sendNotif.getMessage());
                mailMessage.setSubject("Alerte dépense");

                javaMailSender.send(mailMessage);

                Date dateEnvoie = new Date();
                Instant instant = dateEnvoie.toInstant();
                ZonedDateTime zonedDateTime = instant.atZone(ZoneId.systemDefault());
                sendNotif.setUtilisateur(utilisateur);
                sendNotif.setAdmin(admin);
                sendNotif.setDate(dateEnvoie);
                sendNotif.setDepense(depense);
                System.out.println("Fin de l'envoie");

                sendNotifRepository.save(sendNotif);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    //    public void sendDemande(Depense depense) throws BadRequestException {
//        String msg;
//        SendNotification sendNotif = new SendNotification();
//        Utilisateur utilisateur = depense.getUtilisateur();
//        Budget budget = depense.getBudget();
//        System.out.println("User dans send service"+utilisateur);
//        Admin admin = budget.getAdmin();
//        System.out.println("Admin dans send "+admin);
//
//        SimpleMailMessage mailMessage = new SimpleMailMessage();
//
//        msg = "Bonjour M/Mme "+ admin.getNom().toUpperCase()  + " " + admin.getPrenom().toUpperCase()  + ".\n Vous avez reçus une nouvelle demande de la part de  Mr " + utilisateur.getNom().toUpperCase()  + " "+ utilisateur.getPrenom().toUpperCase() ;
//
//        try {
//            // Sauvegarder l'entité Demande d'abord
//            depenseRepository.save(depense);
//
//            System.out.println("Debut de l'envoie dans le servive");
//            sendNotif.setMessage(msg);
//            mailMessage.setFrom(sender);
//            mailMessage.setTo(admin.getEmail());
//            mailMessage.setText(sendNotif.getMessage());
//            mailMessage.setSubject("Alerte demande");
//
//            javaMailSender.send(mailMessage);
//
//            Date dateEnvoie = new Date();
//            Instant instant = dateEnvoie.toInstant();
//            ZonedDateTime zonedDateTime = instant.atZone(ZoneId.systemDefault());
////            demande.setDateDemande(dateEnvoie);
//            sendNotif.setUtilisateur(utilisateur);
//            sendNotif.setAdmin(admin);
//            sendNotif.setDate(dateEnvoie);
//            sendNotif.setDepense(depense);
//            System.out.println("Fin de l'envoie dans le servive");
//
//            sendNotifRepository.save(sendNotif);
//        }catch (Exception e){
//            throw new BadRequestException(e.getMessage());
//        }
//    }
//
//    public void sendDepense(Depense depense) throws BadRequestException {
//        String msg1;
//        SendNotification sendNotif = new SendNotification();
//        Utilisateur utilisateur = depense.getUtilisateur();
//        Budget budget = depense.getBudget();
//        System.out.println("User dans send service"+utilisateur);
//        Admin admin = budget.getAdmin();
//        System.out.println("Admin dans send "+admin);
//
//        SimpleMailMessage mailMessage = new SimpleMailMessage();
//
//        msg1 = "Nouvelle dépense d'une somme de " +depense.getMontantDepense()+ " de la part de  M/Mme " + utilisateur.getNom().toUpperCase()  + " "+ utilisateur.getPrenom().toUpperCase() ;
//
//        try {
//            // Sauvegarder l'entité Demande d'abord
//            depenseRepository.save(depense);
//
//            System.out.println("Debut de l'envoie ");
//            sendNotif.setMessage(msg1);
//            mailMessage.setFrom(sender);
//            mailMessage.setTo(admin.getEmail());
//            mailMessage.setText(sendNotif.getMessage());
//            mailMessage.setSubject("Alerte dépense");
//
//            javaMailSender.send(mailMessage);
//
//            Date dateEnvoie = new Date();
//            Instant instant = dateEnvoie.toInstant();
//            ZonedDateTime zonedDateTime = instant.atZone(ZoneId.systemDefault());
////            demande.setDateDemande(dateEnvoie);
//            sendNotif.setUtilisateur(utilisateur);
//            sendNotif.setAdmin(admin);
//            sendNotif.setDate(dateEnvoie);
//            sendNotif.setDepense(depense);
//            System.out.println("Fin de l'envoie");
//
//            sendNotifRepository.save(sendNotif);
//        }catch (Exception e){
//            throw new BadRequestException(e.getMessage());
//        }
//    }
    @Transactional
    public void approuverDemandeByAdmin(Depense depense) throws BadRequestException {
        String msg;
        SendNotification sendNotif = new SendNotification();
        Utilisateur utilisateur = depense.getUtilisateur();
        Budget budget = depense.getBudget();
        Admin admin = budget.getAdmin();
        msg = "Bonjour M/Mme " + utilisateur.getNom().toUpperCase() + " " + utilisateur.getPrenom().toUpperCase() +
                ".\n Votre demande de " + depense.getMontantDepense() +" a été validée par M. " + admin.getNom().toUpperCase() + " " + admin.getPrenom().toUpperCase();

        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            sendNotif.setMessage(msg);
            sendNotif.setUtilisateur(utilisateur);
            sendNotif.setAdmin(admin);
            sendNotif.setDate(new Date());
            sendNotif.setDepense(depense);

            mailMessage.setFrom(sender);
            mailMessage.setTo(utilisateur.getEmail());
            mailMessage.setSubject("Alert validation");
            mailMessage.setText(msg);

            javaMailSender.send(mailMessage);
            sendNotifRepository.save(sendNotif);
        } catch (Exception e) {
            throw new BadRequestException(e.getMessage());
        }
    }

//    public void approuverDemandeByAdmin(Depense depense) throws BadRequestException {
//        String msg;
//        SendNotification sendNotif = new SendNotification();
//        Utilisateur utilisateur = depense.getUtilisateur();
////        Budget budget = depense.getBudget();
//        System.out.println("User dans send service"+utilisateur);
////        Admin admin = budget.getAdmin();
////        System.out.println("Admin dans send "+admin);
//
//        SimpleMailMessage mailMessage = new SimpleMailMessage();
//
//        msg = "Bonjour Mr "+  utilisateur.getNom().toUpperCase()  + " "+ utilisateur.getPrenom().toUpperCase() + ".\n Votre demande a été valider par M. " ;
//
//        try {
//            // Sauvegarder l'entité Demande d'abord
////            depenseRepository.save(depense);
//
//            System.out.println("Debut de l'envoie dans le servive");
//            sendNotif.setMessage(msg);
//            mailMessage.setFrom(sender);
//            mailMessage.setTo(utilisateur.getEmail());
//            mailMessage.setText(sendNotif.getMessage());
//            mailMessage.setSubject("Alerte demande");
//
//            javaMailSender.send(mailMessage);
//
//            Date dateEnvoie = new Date();
//            Instant instant = dateEnvoie.toInstant();
//            ZonedDateTime zonedDateTime = instant.atZone(ZoneId.systemDefault());
//            sendNotif.setUtilisateur(utilisateur);
////            sendNotif.setAdmin(admin);
//            sendNotif.setDate(dateEnvoie);
////            sendNotif.setDepense(depense);
//            System.out.println("Fin de l'envoie dans le servive");
//
//            sendNotifRepository.save(sendNotif);
//        }catch (Exception e){
//            throw new BadRequestException(e.getMessage());
//        }
//    }

    public List<SendNotification> getAllNotifByUser(long id){
        List<SendNotification>  notifListe = sendNotifRepository.findByUtilisateurIdUtilisateur(id);
        if(notifListe.isEmpty()){
            throw new EntityNotFoundException("Aucune notifiaction  trouvé");
        }
        notifListe = notifListe
                .stream().sorted((d1, d2) -> d2.getMessage().compareTo(d1.getMessage()))
                .collect(Collectors.toList());
        return notifListe;
    }
    public List<SendNotification> getAllNotif(){
        List<SendNotification>  notifListe = sendNotifRepository.findAll();
        if(notifListe.isEmpty()){
            throw new EntityNotFoundException("Aucune notifiaction  trouvé");
        }

        notifListe = notifListe
                .stream().sorted((d1, d2) -> d2.getMessage().compareTo(d1.getMessage()))
                .collect(Collectors.toList());

        return notifListe;
    }

    public String supprimer(long id){
       SendNotification sendNotification = sendNotifRepository.findByIdNotification(id);
       if(sendNotification == null)
           throw  new NoContentException("Aucun notif trouvé");
       sendNotifRepository.delete(sendNotification);
        return "supprimer avec succèss";
    }


}
