package gestion.cosit.gestionDepense.service;

import gestion.cosit.gestionDepense.Exception.NoContentException;
import gestion.cosit.gestionDepense.model.Admin;
import gestion.cosit.gestionDepense.model.Demande;
import gestion.cosit.gestionDepense.model.Utilisateur;
import gestion.cosit.gestionDepense.repository.AdminRepository;
import gestion.cosit.gestionDepense.repository.DemandeRepository;
import gestion.cosit.gestionDepense.repository.UtilisateurRepository;
import jakarta.persistence.EntityNotFoundException;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;
import java.util.List;

@Service
public class DemandeService {

    @Autowired
    private DemandeRepository demandeRepositroy;
    @Autowired
    private UtilisateurRepository utilisateurRepository;
    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private SendNotifService sendNotifService;

    public Demande saveDemande(Demande demande){
        Utilisateur utilisateur = utilisateurRepository.findByIdUtilisateur(demande.getUtilisateur().getIdUtilisateur());
        if(utilisateur == null)
            throw new NoContentException("utilisateur non trouvé");
       Admin admin = adminRepository.findByIdAdmin(demande.getAdmin().getIdAdmin());
       if(admin==null)
           throw new NoContentException("Admin non trouvé");

       Date dateEnvoie = new Date();
       Instant instant = dateEnvoie.toInstant();
       ZonedDateTime zonedDateTime = instant.atZone(ZoneId.systemDefault());
       demande.setDateDemande(dateEnvoie);

        try {
            System.out.println("Debut de l'envoie dans le servive demande");
            sendNotifService.sendDemande(demande);
        } catch (BadRequestException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Fin de l'envoie dans le servive demande");

        return demandeRepositroy.save(demande);
    }

    public Demande updateDemande(Demande demande, long id){
        Demande demandes = demandeRepositroy.findById(id).orElseThrow(()-> new EntityNotFoundException("Demande non trouvé"));

        demandes.setMotif(demande.getMotif());
        demandes.setMontantDemande(demandes.getMontantDemande());
        demandes.setDateDemande(demande.getDateDemande());

        return demandeRepositroy.save(demandes);
    }

    public List<Demande> getAllDemande(){
        List<Demande> demandeList = demandeRepositroy.findAll();
        if(demandeList.isEmpty())
            throw new NoContentException("Aucun demande trouvé");
        return demandeList;
    }

    public List<Demande> getAllDemandeByIdUtilisateur(long IdUtilisateur){
        List<Demande> demandeList = demandeRepositroy.findByUtilisateur_IdUtilisateur(IdUtilisateur);
        if(demandeList.isEmpty())
            throw new NoContentException("Aucun demande trouvé ");
        return demandeList;
    }
    public Demande approuveByDirecteur(Demande demande, long id) {
        Utilisateur utilisateur = utilisateurRepository.findByIdUtilisateur(demande.getUtilisateur().getIdUtilisateur());
        if (utilisateur == null)
            throw new NoContentException("Utilisateur non trouvé");

        Demande demandes = demandeRepositroy.findById(id).orElseThrow(() -> new EntityNotFoundException("Demande non trouvé"));
        demandes.setAutorisationDirecteur(true);

        return demandeRepositroy.save(demandes);
    }

    public Demande approuveByAdmin(Demande demande, long id) throws BadRequestException {
        Admin admin = adminRepository.findByIdAdmin(demande.getAdmin().getIdAdmin());
        if (admin == null)
            throw new NoContentException("Admin non trouvé");

        Demande demandes = demandeRepositroy.findById(id).orElseThrow(() -> new EntityNotFoundException("Demande non trouvé"));
        demandes.setAutorisationAdmin(true);

        sendNotifService.sendApprouveDemande(demande);
        return demandeRepositroy.save(demandes);
    }


    public String deleteDemande(long id){
        Demande demande = demandeRepositroy.findById(id).orElseThrow(() -> new NoContentException("Demande non trouvé"));
        demandeRepositroy.delete(demande);
        return "Supprimé avec succèss";
    }
}
