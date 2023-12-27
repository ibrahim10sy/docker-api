package gestion.cosit.gestionDepense.service;

import gestion.cosit.gestionDepense.model.Admin;
import gestion.cosit.gestionDepense.model.Demande;
import gestion.cosit.gestionDepense.model.SendNotification;
import gestion.cosit.gestionDepense.model.Utilisateur;
import gestion.cosit.gestionDepense.repository.SendNotifRepository;
import gestion.cosit.gestionDepense.repository.UtilisateurRepository;
import jakarta.persistence.EntityNotFoundException;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Service
public class SendNotifService {
    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private SendNotifRepository sendNotifRepository;

    @Value("ibrahim20000sy@gmail.com")
    String sender;

    public void sendDemande(Demande demande) throws BadRequestException {
        String msg;
        SendNotification sendNotif = new SendNotification();
        Utilisateur utilisateur = demande.getUtilisateur();
        System.out.println("User dans send service"+utilisateur);
        Admin admin = demande.getAdmin();
        System.out.println("Admin dans send "+admin);

        SimpleMailMessage mailMessage = new SimpleMailMessage();

        msg = "Bonjour Mr "+ admin.getNom().toUpperCase()  + " " + admin.getPrenom().toUpperCase()  + ".\n Vous avez reçus une nouvelle demande de la part de  Mr" + utilisateur.getNom().toUpperCase()  + " "+ utilisateur.getPrenom().toUpperCase() ;

        try {
            System.out.println("Debut de l'envoie dans le servive");
            sendNotif.setMessage(msg);
            mailMessage.setFrom(sender);
            mailMessage.setTo(admin.getEmail());
            mailMessage.setText(sendNotif.getMessage());
            mailMessage.setSubject("Alerte demande");

            javaMailSender.send(mailMessage);

            Date dateEnvoie = new Date();
            Instant instant = dateEnvoie.toInstant();
            ZonedDateTime zonedDateTime = instant.atZone(ZoneId.systemDefault());
            demande.setDateDemande(dateEnvoie);
            sendNotif.setUtilisateur(utilisateur);
            sendNotif.setAdmin(admin);
            sendNotif.setDate(dateEnvoie);
            System.out.println("Fin de l'envoie dans le servive");

            sendNotifRepository.save(sendNotif);
        }catch (Exception e){
            throw new BadRequestException(e.getMessage());
        }
    }

    public void sendApprouveDemande(Demande demande) throws BadRequestException {
        String msg;
        SendNotification sendNotif = new SendNotification();
        Utilisateur utilisateur = demande.getUtilisateur();
        System.out.println("User dans send service"+utilisateur);
        Admin admin = demande.getAdmin();
        System.out.println("Admin dans send "+admin);

        SimpleMailMessage mailMessage = new SimpleMailMessage();

        msg = "Bonjour Mr "+  utilisateur.getNom().toUpperCase() + " "+ utilisateur.getPrenom().toUpperCase()+ ".\n Votre demande de a été approuver par Mr "+admin.getNom().toUpperCase()  + " " + admin.getPrenom().toUpperCase() ;

        try {
            sendNotif.setMessage(msg);
            mailMessage.setFrom(sender);
            mailMessage.setTo(utilisateur.getEmail());
            mailMessage.setText(sendNotif.getMessage());
            mailMessage.setSubject("Alerte demande ");

            javaMailSender.send(mailMessage);

            Date dateEnvoie = new Date();
            Instant instant = dateEnvoie.toInstant();
            ZonedDateTime zonedDateTime = instant.atZone(ZoneId.systemDefault());
            demande.setDateDemande(dateEnvoie);
            sendNotif.setUtilisateur(utilisateur);
            sendNotif.setAdmin(admin);
            sendNotif.setDate(dateEnvoie);

            sendNotifRepository.save(sendNotif);
        }catch (Exception e){
            throw new BadRequestException(e.getMessage());
        }
    }
    public List<SendNotification> getAllNotif(){
        List<SendNotification>  notifListe = sendNotifRepository.findAll();
        if(notifListe.isEmpty()){
            throw new EntityNotFoundException("Aucune notifiaction  trouvé");
        }

        return notifListe;
    }
}
