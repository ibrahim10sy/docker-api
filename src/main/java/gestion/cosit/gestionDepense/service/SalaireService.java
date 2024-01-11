package gestion.cosit.gestionDepense.service;

import gestion.cosit.gestionDepense.Exception.NoContentException;
import gestion.cosit.gestionDepense.model.Salaire;
import gestion.cosit.gestionDepense.model.SousCategorie;
import gestion.cosit.gestionDepense.model.Utilisateur;
import gestion.cosit.gestionDepense.repository.SalaireRepository;
import gestion.cosit.gestionDepense.repository.SousCategorieRepository;
import gestion.cosit.gestionDepense.repository.UtilisateurRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SalaireService {

    @Autowired
    private SalaireRepository salaireRepository;
    @Autowired
    private UtilisateurRepository utilisateurRepository;
    @Autowired
    private SousCategorieRepository sousCategorieRepository;
    public Salaire saveSalaire(Salaire salaire) {
        Utilisateur utilisateur = utilisateurRepository.findByIdUtilisateur(salaire.getUtilisateur().getIdUtilisateur());
        SousCategorie sousCategorie = sousCategorieRepository.findByIdSousCategorie(salaire.getSousCategorie().getIdSousCategorie());

        if(sousCategorie == null )
            throw new NoContentException("Sous catégorie non trouvé");
        if (utilisateur == null) {
            throw new NoContentException("Utilisateur non trouvé");
        }

        // Assurez-vous que la date est définie dans votre objet salaire
//        Date dateSalaire = salaire.getDate();
        LocalDate dateSalaire = salaire.getDate();
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

    public HashMap<String,Object> sommeOfAllSalaireNotFinish(){
        HashMap<String , Object> hashMap = new HashMap<>();
        Integer[] result = salaireRepository.getSommeOfTotalSalaireNotFinish();
        if(result[0] == null || result[0] == null){
            hashMap.put("Total",0);
        }
        else{
            hashMap.put("Total",result[0]);
        }
        return hashMap;
    }
    public List<Salaire> getAllSalaire(){
        List<Salaire> salaireList = salaireRepository.findAll();
        if(salaireList.isEmpty())
            throw new NoContentException("Aucun salaire trouvé");

        salaireList = salaireList
                .stream().sorted((d1, d2) -> d2.getDescription().compareTo(d1.getDescription()))
                .collect(Collectors.toList());
        return salaireList;
    }

    public List<Salaire> getAllSalaireByUser(long idUtilisateur){
        List<Salaire> salaireList = salaireRepository.findByUtilisateur_IdUtilisateur(idUtilisateur);
        if(salaireList.isEmpty())
            throw new NoContentException("Aucun salaire trouvé");

        salaireList = salaireList
                .stream().sorted((d1, d2) -> d2.getDate().compareTo(d1.getDate()))
                .collect(Collectors.toList());

        return salaireList;
    }

    public String supprimerSalaire(long idSalaire){
        Salaire salaire = salaireRepository.findByIdSalaire(idSalaire);
        if(salaire == null )
            throw new NoContentException("Aucun salaire trouvé avec id"+idSalaire);
        salaireRepository.delete(salaire);
        return "Salaire supprimer avec succèss";
    }
    public String deleteSalaire(long id){
        Salaire salaire = salaireRepository.findById(id).orElseThrow(()-> new EntityNotFoundException("Aucun donné trouvé"));
        salaireRepository.delete(salaire);
        return "Supprimé avec succèss";
    }
}
