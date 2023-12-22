package gestion.cosit.gestionDepense.service;

import gestion.cosit.gestionDepense.model.*;
import gestion.cosit.gestionDepense.repository.*;
import jakarta.persistence.EntityNotFoundException;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
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
        @Autowired
        BudgetRepository budgetRepository;
        @Autowired
        AdminRepository adminRepository;

    public Depense saveDepenseByUser(Depense depense) throws BadRequestException {

        Utilisateur utilisateur = utilisateurRepository.findByIdUtilisateur(depense.getUtilisateur().getIdUtilisateur());
        CategorieDepense categorieDepense = categorieRepository.findByIdCategoriedepense(depense.getCategorieDepense().getIdCategoriedepense());
        Budget budget = budgetRepository.findByIdBudget(depense.getBudget().getIdBudget());

        if(budget == null)
            throw new BadRequestException("Desolé ce budget n'existe pas");

        if(categorieDepense == null)
            throw new BadRequestException("Desolé cette catégorie n'existe pas");

        if (utilisateur == null)
            throw new BadRequestException("User invalid");

        Demande demande = demandeRepositroy.findByIdDemande(depense.getDemande().getIdDemande());
        if (depense.getMontantDepense() > budget.getMontantRestant() ) {
            System.out.println("Condition vérifier");
            if (demande == null) {
                throw new BadRequestException("La demande associée à la dépense n'existe pas");
            }

            System.out.println("La demande est " + demande);

//            if (depense.getMontantDepense() > demande.getMontantDemande()) {
//                throw new BadRequestException("Le montant de la dépense ne doit pas être supérieur à celui du montant demandé " + demande.getMontantDemande());
//            }
        }

        // Mettre à jour le montant restant du budget
        budget.setMontantRestant(budget.getMontantRestant() - depense.getMontantDepense());
        System.out.println(depense);
        budgetRepository.save(budget);

        // Enregistrer la dépense mise à jour
        return depenseRepository.save(depense);
    }
    public Depense saveDepenseByAdmin(Depense depense) throws BadRequestException {
        Admin admin = adminRepository.findByIdAdmin(depense.getAdmin().getIdAdmin());
        CategorieDepense categorieDepense = categorieRepository.findByIdCategoriedepense(depense.getCategorieDepense().getIdCategoriedepense());
        Budget budget = budgetRepository.findByIdBudget(depense.getBudget().getIdBudget());

        if(budget == null)
            throw new BadRequestException("Desolé ce budget n'existe pas");

        if(categorieDepense == null)
            throw new BadRequestException("Desolé cette catégorie n'existe pas");

        if (admin == null)
            throw new BadRequestException("Admin invalid");

            if (depense.getMontantDepense() > budget.getMontantRestant()) {
                throw new BadRequestException("Le montant de la dépense ne doit pas être supérieur à celui du montant restant du budget " );
            } else if (budget.getMontantRestant() == 0) {
                throw new BadRequestException("Le  montant restant du budget est atteint" );

            }

        // Mettre à jour le montant restant du budget
        budget.setMontantRestant(budget.getMontantRestant() - depense.getMontantDepense());
        System.out.println(depense);
        budgetRepository.save(budget);

        // Enregistrer la dépense mise à jour
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
//    public Depense saveDepense(Depense depense , MultipartFile multipartFile) throws Exception {
//        Utilisateur utilisateur = utilisateurRepository.findByIdUtilisateur(depense.getUtilisateur().getIdUtilisateur());
//        CategorieDepense categorieDepense = categorieRepository.findByIdCategoriedepense(depense.getCategorieDepense().getIdCategoriedepense());
//
//        if(categorieDepense == null)
//            throw new BadRequestException("Desolé cette catégorie n'existe pas");
//
//        if (utilisateur == null)
//            throw new BadRequestException("User invalid");
//
//        if(multipartFile != null){
//            String location = "C:\\xampp\\htdocs\\cosit";
//            try{
//                Path rootlocation = Paths.get(location);
//                if(!Files.exists(rootlocation)){
//                    Files.createDirectories(rootlocation);
//                    Files.copy(multipartFile.getInputStream(),
//                            rootlocation.resolve(multipartFile.getOriginalFilename()));
//                    depense.setImage("cosit/"
//                            +multipartFile.getOriginalFilename());
//                }else{
//                    try {
//                        String nom = location+"\\"+multipartFile.getOriginalFilename();
//                        Path name = Paths.get(nom);
//                        if(!Files.exists(name)){
//                            Files.copy(multipartFile.getInputStream(),
//                                    rootlocation.resolve(multipartFile.getOriginalFilename()));
//                            depense.setImage("cosit/"
//                                    +multipartFile.getOriginalFilename());
//                        }else{
//                            Files.delete(name);
//                            Files.copy(multipartFile.getInputStream(),rootlocation.resolve(multipartFile.getOriginalFilename()));
//                            depense.setImage("cosit/"
//                                    +multipartFile.getOriginalFilename());
//                        }
//                    }catch (Exception e){
//                        throw new Exception("Impossible de télécharger l\'image");
//                    }
//                }
//            } catch (Exception e){
//                throw new Exception(e.getMessage());
//            }
//        }
//        LocalDate dateDepense = LocalDate.now();
//        double montantDepense = depense.getMontantDepense();
//        double montantAutorise = 100000;
//        depense.setMontantPlafond(montantAutorise);
//        double plafondRestant = 0;
//
//        // Récupérer les dépenses du personnel pour le mois en cours depuis la base de données
//        List<Depense> depensesDuMois = depenseRepository.findByUtilisateurAndDateDepenseBetween(utilisateur,
//                dateDepense.withDayOfMonth(1), dateDepense.withDayOfMonth(dateDepense.lengthOfMonth()));
//
//        double montantTotalDuMois = depensesDuMois.stream().mapToDouble(Depense::getMontantDepense).sum();
//        System.out.println("Le montant des depenses total recuperer"+montantTotalDuMois);
//
//        if (dateDepense.isAfter(depense.getDateDepense()) || (montantTotalDuMois + montantDepense) > montantAutorise) {
//            // Si le montant plafond est atteint, On associe une demande à la dépense
//            Demande demande = demandeRepositroy.findByIdDemande(depense.getDemande().getIdDemande());
//            if(depense.getMontantDepense() > depense.getDemande().getMontantDemande())
//                throw new BadRequestException("Le montant du dépense ne doit pas etre supérieur à celle du montant demandé");
//            depense.setDemande(demande);
//        } else if (depense.getMontantDepense() > depense.getMontantPlafondRestant()) {
//            throw new BadRequestException("Le montant du dépense ne doit pas etre supérieur à celle du montant resatant");
//        }
//        double montantVerif = montantTotalDuMois + montantDepense;
//        // Calculer le montant restant du plafond
//        plafondRestant = montantAutorise - montantVerif;
//        depense.setMontantPlafondRestant(plafondRestant);
//        System.out.println("Montant restant est "+depense.getMontantPlafondRestant());
//        // Enregistrer la dépense dans la base de données
//        System.out.println("La depense service "+depense);
//        return depenseRepository.save(depense);
//    }

//    public Depense updateDepense(Depense depense, long id, MultipartFile multipartFile) throws Exception {
//        Depense isDepenseExist = depenseRepository.findById(id).orElseThrow(()-> new EntityNotFoundException("Depense non trouvé"));
//
//        isDepenseExist.setLibelle(depense.getLibelle());
//        isDepenseExist.setDateDepense(depense.getDateDepense());
//        isDepenseExist.setDescription(depense.getDescription());
//        isDepenseExist.setMontantDepense(depense.getMontantDepense());
//        if(multipartFile != null){
//            String location = "C:\\xampp\\htdocs\\cosit";
//            try{
//                Path rootlocation = Paths.get(location);
//                if(!Files.exists(rootlocation)){
//                    Files.createDirectories(rootlocation);
//                    Files.copy(multipartFile.getInputStream(),
//                            rootlocation.resolve(multipartFile.getOriginalFilename()));
//                    isDepenseExist.setImage("cosit/"
//                            +multipartFile.getOriginalFilename());
//                }else{
//                    try {
//                        String nom = location+"\\"+multipartFile.getOriginalFilename();
//                        Path name = Paths.get(nom);
//                        if(!Files.exists(name)){
//                            Files.copy(multipartFile.getInputStream(),
//                                    rootlocation.resolve(multipartFile.getOriginalFilename()));
//                            isDepenseExist.setImage("cosit/"
//                                    +multipartFile.getOriginalFilename());
//                        }else{
//                            Files.delete(name);
//                            Files.copy(multipartFile.getInputStream(),rootlocation.resolve(multipartFile.getOriginalFilename()));
//                            isDepenseExist.setImage("cosit/"
//                                    +multipartFile.getOriginalFilename());
//                        }
//                    }catch (Exception e){
//                        throw new Exception("Impossible de télécharger l\'image");
//                    }
//                }
//            } catch (Exception e){
//                throw new Exception(e.getMessage());
//            }
//        }
//        return depenseRepository.save(isDepenseExist);
//    }

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
    public List<Depense> allDepenseByIdAdmin(long idAdmin){
        List<Depense> depenseList = depenseRepository.findByAdminIdAdmin(idAdmin);

        if(depenseList.isEmpty())
            throw new EntityNotFoundException("Aucun depense trouvé en fonction de la demande");
        return depenseList;
    }
    public List<Depense> getDepenseByIdBudget(long idBudget){
        List<Depense> depensesList = depenseRepository.findByBudgetIdBudget(idBudget);
        if (depensesList.isEmpty())
            throw new EntityNotFoundException("Aucune depenses trouvée");
        return depensesList;

    }
    public List<Depense> allDepenseByIdDemande(long idDemande){
        List<Depense> depenseList = depenseRepository.findAllDepenseByDemande_IdDemande(idDemande);

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
