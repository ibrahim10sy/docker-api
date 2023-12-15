package gestion.cosit.gestionDepense.service;

import gestion.cosit.gestionDepense.model.CategorieDepense;
import gestion.cosit.gestionDepense.model.Demande;
import gestion.cosit.gestionDepense.model.Depense;
import gestion.cosit.gestionDepense.model.Utilisateur;
import gestion.cosit.gestionDepense.repository.CategorieDepenseRepository;
import gestion.cosit.gestionDepense.repository.DemandeRepository;
import gestion.cosit.gestionDepense.repository.DepenseRepository;
import gestion.cosit.gestionDepense.repository.UtilisateurRepository;
import jakarta.persistence.EntityNotFoundException;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class DepenseService {

         @Autowired
        DepenseRepository depenseRepository;
        @Autowired
        UtilisateurRepository utilisateurRepository;
        @Autowired
        CategorieDepenseRepository categorieRepository;
        @Autowired
        DemandeRepository demandeRepositroy;

    public Depense saveDepense(Depense depense) throws BadRequestException {
        Utilisateur utilisateur = utilisateurRepository.findByIdUtilisateur(depense.getUtilisateur().getIdUtilisateur());
        CategorieDepense categorieDepense = categorieRepository.findByIdCategoriedepense(depense.getCategorieDepense().getIdCategoriedepense());

        if(categorieDepense == null)
            throw new BadRequestException("Desolé cette catégorie n'existe pas");

        if (utilisateur == null)
            throw new BadRequestException("User invalid");

        LocalDate dateDepense = LocalDate.now();
        double montantDepense = depense.getMontantDepense();
        double montantAutorise = 100000;
        depense.setMontantPlafond(montantAutorise);
        double plafondRestant = 0;

        // Récupérer les dépenses du personnel pour le mois en cours depuis la base de données
        List<Depense> depensesDuMois = depenseRepository.findByUtilisateurAndDateDepenseBetween(utilisateur,
                dateDepense.withDayOfMonth(1), dateDepense.withDayOfMonth(dateDepense.lengthOfMonth()));

        double montantTotalDuMois = depensesDuMois.stream().mapToDouble(Depense::getMontantDepense).sum();
        System.out.println("Le montant des depenses total recuperer"+montantTotalDuMois);

        if (dateDepense.isAfter(depense.getDateDepense()) || (montantTotalDuMois + montantDepense) > montantAutorise) {
            // Si le montant plafond est atteint, On associe une demande à la dépense
            Demande demande = demandeRepositroy.findByIdDemande(depense.getDemande().getIdDemande());
            if(depense.getMontantDepense() > depense.getDemande().getMontantDemande())
                throw new BadRequestException("Le montant du dépense ne doit pas etre supérieur à celle du montant demandé");
            depense.setDemande(demande);
        } else if (depense.getMontantDepense() > depense.getMontantPlafondRestant()) {
            throw new BadRequestException("Le montant du dépense ne doit pas etre supérieur à celle du montant resatant");
        }
        double montantVerif = montantTotalDuMois + montantDepense;
        // Calculer le montant restant du plafond
        plafondRestant = montantAutorise - montantVerif;
        depense.setMontantPlafondRestant(plafondRestant);
        System.out.println("Montant restant est "+depense.getMontantPlafondRestant());
        // Enregistrer la dépense dans la base de données
        System.out.println("La depense service "+depense);
        return depenseRepository.save(depense);
    }

    public Depense updateDepense(Depense depense, long id){
        Depense isDepenseExist = depenseRepository.findById(id).orElseThrow(()-> new EntityNotFoundException("Depense non trouvé"));

        isDepenseExist.setLibelle(depense.getLibelle());
        isDepenseExist.setDateDepense(depense.getDateDepense());
        isDepenseExist.setDescription(depense.getDescription());
        isDepenseExist.setMontantDepense(depense.getMontantDepense());

        return depenseRepository.save(isDepenseExist);
    }

    public List<Depense> allDepense(){
        List<Depense> DepenseList = depenseRepository.findAll();

        if(DepenseList.isEmpty())
            throw  new EntityNotFoundException("Aucun dépense trouvé");
        return DepenseList;
    }
    public List<Depense> allDepenseByIdUtilisateur(long idUtilisateur){
        List<Depense> depenseList = depenseRepository.findByUtilisateurIdUtilisateur(idUtilisateur);

        if(depenseList.isEmpty())
            throw new EntityNotFoundException("Aucun depense trouvé en fonction de la demande");
        return depenseList;
    }
    public List<Depense> allDepenseByIdDemande(long idDemande){
        List<Depense> depenseList = depenseRepository.getAllDepenseByDemande_IdDemande(idDemande);

        if(depenseList.isEmpty())
            throw new EntityNotFoundException("Aucun depense trouvé en fonction de la demande");
        return depenseList;
    }
    public String deleteDepende(long id){
        Depense isDepenseExist = depenseRepository.findByIdDepense(id);
        if(isDepenseExist == null)
            throw  new EntityNotFoundException("cette depenses n'existe pas");

        depenseRepository.delete(isDepenseExist);
        return "Dépense supprimer avec succèss";
    }
}
