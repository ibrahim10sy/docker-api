package gestion.cosit.gestionDepense.service;

import gestion.cosit.gestionDepense.Exception.NoContentException;
import gestion.cosit.gestionDepense.model.*;
import gestion.cosit.gestionDepense.repository.DemandeRepository;
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
    @Autowired
    private DemandeRepository demandeRepository;

    @Value("ibrahim20000sy@gmail.com")
    String sender;

    public void sendDemande(Demande demande) throws BadRequestException {
        String msg;
        SendNotification sendNotif = new SendNotification();
        Utilisateur utilisateur = demande.getUtilisateur();
//        Demande demande1 = demande;
        System.out.println("User dans send service"+utilisateur);
        Admin admin = demande.getAdmin();
        System.out.println("Admin dans send "+admin);

        SimpleMailMessage mailMessage = new SimpleMailMessage();

        msg = "Bonjour Mr "+ admin.getNom().toUpperCase()  + " " + admin.getPrenom().toUpperCase()  + ".\n Vous avez reçus une nouvelle demande de la part de  Mr" + utilisateur.getNom().toUpperCase()  + " "+ utilisateur.getPrenom().toUpperCase() ;

        try {
            // Sauvegarder l'entité Demande d'abord
            demandeRepository.save(demande);

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
            sendNotif.setDemande(demande);
            System.out.println("Fin de l'envoie dans le servive");

            sendNotifRepository.save(sendNotif);
        }catch (Exception e){
            throw new BadRequestException(e.getMessage());
        }
    }

    public void sendNotificationForApproval(Demande demande) throws Exception {
        try {
            // Créer le message de notification
            String msg = "Bonjour Mr " + demande.getUtilisateur().getNom().toUpperCase() + " " +
                    demande.getUtilisateur().getPrenom().toUpperCase() +
                    ".\n Votre demande a été approuvée par Mr " + demande.getAdmin().getNom().toUpperCase() +
                    " " + demande.getAdmin().getPrenom().toUpperCase();

            // Configurer l'email
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom(sender);
            mailMessage.setTo(demande.getUtilisateur().getEmail());
            mailMessage.setText(msg);
            mailMessage.setSubject("Alerte demande ");

            // Envoyer l'email
            javaMailSender.send(mailMessage);

            // Sauvegarder la notification
            SendNotification sendNotif = new SendNotification();
            sendNotif.setMessage(msg);
            sendNotif.setUtilisateur(demande.getUtilisateur());
            sendNotif.setAdmin(demande.getAdmin());
            sendNotif.setDate(new Date());
            sendNotif.setDemande(demande);
            sendNotifRepository.save(sendNotif);
        } catch (Exception e) {
            throw new Exception("Erreur lors de l'envoi de notification par e-mail : " + e.getMessage());
        }
    }
    public List<SendNotification> getAllNotif(){
        List<SendNotification>  notifListe = sendNotifRepository.findAll();
        if(notifListe.isEmpty()){
            throw new EntityNotFoundException("Aucune notifiaction  trouvé");
        }

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
