package gestion.cosit.gestionDepense.service;

import gestion.cosit.gestionDepense.Exception.NoContentException;
import gestion.cosit.gestionDepense.model.Salaire;
import gestion.cosit.gestionDepense.model.Utilisateur;
import gestion.cosit.gestionDepense.repository.SalaireRepository;
import gestion.cosit.gestionDepense.repository.UtilisateurRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class SalaireService {

    @Autowired
    private SalaireRepository salaireRepository;
    @Autowired
    private UtilisateurRepository utilisateurRepository;
    public Salaire saveSalaire(Salaire salaire) {
        Utilisateur utilisateur = utilisateurRepository.findByIdUtilisateur(salaire.getUtilisateur().getIdUtilisateur());

        if (utilisateur == null) {
            throw new NoContentException("Utilisateur non trouvé");
        }

        // Assurez-vous que la date est définie dans votre objet salaire
        Date dateSalaire = salaire.getDate();

        // Vérifiez si le salaire pour cette date existe déjà
        Optional<Salaire> existingSalaire = salaireRepository.findByUtilisateurAndDate(utilisateur, dateSalaire);

        if (existingSalaire.isPresent()) {
            throw new NoContentException("Le salaire pour cette date existe déjà");
        }

        return salaireRepository.save(salaire);
    }

    public Salaire updateSalaire(Salaire salaire, long id){
        Salaire salaires = salaireRepository.findById(id).orElseThrow(()-> new EntityNotFoundException(("Aucun donné trouvé")));

        salaires.setDescription(salaire.getDescription());
        salaires.setDate(salaire.getDate());
        salaires.setMontant(salaire.getMontant());
        return salaireRepository.save(salaires);
    }

    public List<Salaire> getAllSalaire(){
        List<Salaire> salaireList = salaireRepository.findAll();
        if(salaireList.isEmpty())
            throw new NoContentException("Aucun salaire trouvé");
        return salaireList;
    }

    public List<Salaire> getAllSalaireByUser(long idUtilisateur){
        List<Salaire> salaireList = salaireRepository.findByUtilisateur_IdUtilisateur(idUtilisateur);
        if(salaireList.isEmpty())
            throw new NoContentException("Aucun salaire trouvé");
        return salaireList;
    }

    public String deleteSalaire(long id){
        Salaire salaire = salaireRepository.findById(id).orElseThrow(()-> new EntityNotFoundException("Aucun donné trouvé"));
        salaireRepository.delete(salaire);
        return "Supprimé avec succèss";
    }
}
