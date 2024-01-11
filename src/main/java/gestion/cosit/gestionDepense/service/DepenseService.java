package gestion.cosit.gestionDepense.service;

import gestion.cosit.gestionDepense.Exception.NoContentException;
import gestion.cosit.gestionDepense.model.*;
import gestion.cosit.gestionDepense.repository.*;
import jakarta.persistence.EntityNotFoundException;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.webjars.NotFoundException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

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
        @Autowired
        SousCategorieRepository sousCategorieRepository;
        @Autowired
        ParametreDepenseRepository parametreDepenseRepository;
    @Autowired
    SendNotifRepository sendNotifRepository;
    @Autowired
    SendNotifService sendNotifService;

public Depense saveDepenseByUser(Depense depense , MultipartFile multipartFile) throws Exception {
    Utilisateur utilisateur = utilisateurRepository.findByIdUtilisateur(depense.getUtilisateur().getIdUtilisateur());
    SousCategorie sousCategorie = sousCategorieRepository.findByIdSousCategorie(depense.getSousCategorie().getIdSousCategorie());
    Budget budget = budgetRepository.findByIdBudget(depense.getBudget().getIdBudget());
    ParametreDepense parametreDepense = parametreDepenseRepository.findByIdParametre(depense.getParametreDepense().getIdParametre());

    if(budget == null)
        throw new BadRequestException("Desolé ce budget n'existe pas");

    if(sousCategorie == null)
        throw new BadRequestException("Desolé cette catégorie n'existe pas");

    if (utilisateur == null)
        throw new BadRequestException("Utilisateur invalid");

    if(multipartFile != null){
        String location = "C:\\xampp\\htdocs\\cosit";
        try{
            Path rootlocation = Paths.get(location);
            if(!Files.exists(rootlocation)){
                Files.createDirectories(rootlocation);
                Files.copy(multipartFile.getInputStream(),
                        rootlocation.resolve(multipartFile.getOriginalFilename()));
                depense.setImage("cosit/"
                        +multipartFile.getOriginalFilename());
            }else{
                try {
                    String nom = location+"\\"+multipartFile.getOriginalFilename();
                    Path name = Paths.get(nom);
                    if(!Files.exists(name)){
                        Files.copy(multipartFile.getInputStream(),
                                rootlocation.resolve(multipartFile.getOriginalFilename()));
                        depense.setImage("cosit/"
                                +multipartFile.getOriginalFilename());
                    }else{
                        Files.delete(name);
                        Files.copy(multipartFile.getInputStream(),rootlocation.resolve(multipartFile.getOriginalFilename()));
                        depense.setImage("cosit/"
                                +multipartFile.getOriginalFilename());
                    }
                }catch (Exception e){
                    throw new Exception("Impossible de télécharger l\'image");
                }
            }
        } catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }

    if (depense.getMontantDepense() > budget.getMontantRestant()) {
        throw new BadRequestException("Le montant de la dépense ne doit pas être supérieur à celui du montant restant du budget " );
    } else if (budget.getMontantRestant() == 0) {
        throw new BadRequestException("Le  montant restant du budget est atteint" );

    }
    if (depense.getMontantDepense() >= depense.getParametreDepense().getMontantSeuil()) {
        // Montant supérieur ou égal au seuil, envoie de notification
        try {
            System.out.println("Debut de l'envoi dans le service demande");
            sendNotifService.sendDemande(depense);
        } catch (BadRequestException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Fin de l'envoi dans le service demande");

        // Ne pas ajouter la dépense immédiatement, attendre la validation de l'administrateur
        depense.setAutorisationAdmin(false);

        // Enregistrement initial avec la dépense non validée
        depense = depenseRepository.save(depense);
    } else {
        // Montant inférieur au seuil, la dépense peut être enregistrée directement
        // Mettre à jour le montant restant du budget
        budget.setMontantRestant(budget.getMontantRestant() - depense.getMontantDepense());
        depense.setViewed(false);

        // Marquer la dépense comme validée car elle n'a pas besoin de validation administrative
        depense.setAutorisationAdmin(true);

        // Enregistrer la dépense mise à jour
        depense = depenseRepository.save(depense);
    }

    // Mettre à jour le montant restant du budget
    budgetRepository.save(budget);

    return depense;
}
    // Méthode permettant de  valider la dépense par l'administrateur
    public Depense validateDepenseByAdmin(Long depenseId) throws Exception {
        Depense depense = depenseRepository.findById(depenseId)
                .orElseThrow(() -> new NotFoundException("Dépense non trouvée"));

        // Valider la dépense
        depense.setAutorisationAdmin(true);

        // Enregistrement la dépense validée
        return depenseRepository.save(depense);
    }
    public Depense saveDepenseByAdmin(Depense depense , MultipartFile multipartFile) throws Exception {
        Admin admin = adminRepository.findByIdAdmin(depense.getAdmin().getIdAdmin());
        Budget budget = budgetRepository.findByIdBudget(depense.getBudget().getIdBudget());
        SousCategorie sousCategorie = sousCategorieRepository.findByIdSousCategorie(depense.getSousCategorie().getIdSousCategorie());

        if(budget == null)
            throw new BadRequestException("Desolé ce budget n'existe pas");

        if(sousCategorie == null)
            throw new BadRequestException("Desolé cette catégorie n'existe pas");

        if (admin == null)
            throw new BadRequestException("Admin invalid");

        if(multipartFile != null){
            String location = "C:\\xampp\\htdocs\\cosit";
            try{
                Path rootlocation = Paths.get(location);
                if(!Files.exists(rootlocation)){
                    Files.createDirectories(rootlocation);
                    Files.copy(multipartFile.getInputStream(),
                            rootlocation.resolve(multipartFile.getOriginalFilename()));
                    depense.setImage("cosit/"
                            +multipartFile.getOriginalFilename());
                }else{
                    try {
                        String nom = location+"\\"+multipartFile.getOriginalFilename();
                        Path name = Paths.get(nom);
                        if(!Files.exists(name)){
                            Files.copy(multipartFile.getInputStream(),
                                    rootlocation.resolve(multipartFile.getOriginalFilename()));
                            depense.setImage("cosit/"
                                    +multipartFile.getOriginalFilename());
                        }else{
                            Files.delete(name);
                            Files.copy(multipartFile.getInputStream(),rootlocation.resolve(multipartFile.getOriginalFilename()));
                            depense.setImage("cosit/"
                                    +multipartFile.getOriginalFilename());
                        }
                    }catch (Exception e){
                        throw new Exception("Impossible de télécharger l\'image");
                    }
                }
            } catch (Exception e){
                throw new Exception(e.getMessage());
            }
        }

        if (depense.getMontantDepense() > budget.getMontantRestant()) {
                throw new BadRequestException("Le montant de la dépense ne doit pas être supérieur à celui du montant restant du budget " );
        } else if (budget.getMontantRestant() == 0) {
            throw new BadRequestException("Le  montant restant du budget est atteint" );

        }
        // Mettre à jour le montant restant du budget
        budget.setMontantRestant(budget.getMontantRestant() - depense.getMontantDepense());
        depense.setViewed(false);
        System.out.println(depense);
        budgetRepository.save(budget);

        // Enregistrer la dépense mise à jour
        return depenseRepository.save(depense);
    }

    public Depense marquerView(long id){
        Depense isDepenseExist = depenseRepository.findById(id).orElseThrow(()-> new EntityNotFoundException("Depense non trouvé"));
        isDepenseExist.setViewed(true);
        return depenseRepository.save(isDepenseExist);
    }
    public Depense updateDepense(Depense depense, long id, MultipartFile multipartFile) throws Exception {
        Depense isDepenseExist = depenseRepository.findById(id).orElseThrow(()-> new EntityNotFoundException("Depense non trouvé"));

//        isDepenseExist.setLibelle(depense.getLibelle());
        isDepenseExist.setDateDepense(depense.getDateDepense());
        isDepenseExist.setDescription(depense.getDescription());
        isDepenseExist.setMontantDepense(depense.getMontantDepense());
        if(multipartFile != null){
            String location = "C:\\xampp\\htdocs\\cosit";
            try{
                Path rootlocation = Paths.get(location);
                if(!Files.exists(rootlocation)){
                    Files.createDirectories(rootlocation);
                    Files.copy(multipartFile.getInputStream(),
                            rootlocation.resolve(multipartFile.getOriginalFilename()));
                    isDepenseExist.setImage("cosit/"
                            +multipartFile.getOriginalFilename());
                }else{
                    try {
                        String nom = location+"\\"+multipartFile.getOriginalFilename();
                        Path name = Paths.get(nom);
                        if(!Files.exists(name)){
                            Files.copy(multipartFile.getInputStream(),
                                    rootlocation.resolve(multipartFile.getOriginalFilename()));
                            isDepenseExist.setImage("cosit/"
                                    +multipartFile.getOriginalFilename());
                        }else{
                            Files.delete(name);
                            Files.copy(multipartFile.getInputStream(),rootlocation.resolve(multipartFile.getOriginalFilename()));
                            isDepenseExist.setImage("cosit/"
                                    +multipartFile.getOriginalFilename());
                        }
                    }catch (Exception e){
                        throw new Exception("Impossible de télécharger l\'image");
                    }
                }
            } catch (Exception e){
                throw new Exception(e.getMessage());
            }
        }
        return depenseRepository.save(isDepenseExist);
    }

    public HashMap<String, Object> sommeTotalDesDepenses(long idBudget) {
        HashMap<String, Object> hashMap = new HashMap<>();

        Integer somme = depenseRepository.getSommeTotalDepensesParIdBudget(idBudget);

        // Vérifier si la somme est nulle
        if (somme == null) {
            hashMap.put("Total", 0);
        } else {
            hashMap.put("Total", somme);
        }

        return hashMap;
    }
//    public HashMap<String,Object> sommeOfAllDepense(long idBudget){
//        HashMap<String , Object> hashMap = new HashMap<>();
//        Integer[] result = depenseRepository.getSommeOfTotalDepenseByIdBudget(idBudget);
//        if(result[0] == null || result[0] == null){
//            hashMap.put("Total",0);
//        }
//        else{
//            hashMap.put("Total",result[0]);
//        }
//        return hashMap;
//    }

    public List<Depense> sortBudgetByMonthAndYear( String date){

        // Obtention de tous les budget dans la base de donnÃ©es
        List<Depense> depenseList = depenseRepository.getDepenseByMonthAndYear("%"+date+"%");

        // Si la liste est vide, le systÃ¨me lÃ¨vera une exception
        if (depenseList.isEmpty())
            throw new EntityNotFoundException("Aucune depense trouvÃ©");

        // Dans le cas contraire le systÃ¨me retourne la liste
        return depenseList;
    }
    public List<Depense> searchDepense(String desc){

        // Obtention de tous les budget dans la base de donnÃ©es
        List<Depense> depenseList = depenseRepository.findByDescriptionContaining(desc);

        // Si la liste est vide, le systÃ¨me lÃ¨vera une exception
        if (depenseList.isEmpty())
            throw new EntityNotFoundException("Aucun depense trouvÃ©");

        // Dans le cas contraire le systÃ¨me retourne la liste
        return depenseList;
    }
    public List<Depense> allDepense() {
        List<Depense> depenseList = depenseRepository.findAll();

        if (depenseList.isEmpty())
            throw new EntityNotFoundException("Aucune dépense trouvée");


        // Tri des dépenses par date de dépense, de la plus récente à la plus ancienne
        depenseList = depenseList
                .stream().sorted((d1, d2) -> d2.getDescription().compareTo(d1.getDescription()))
                .collect(Collectors.toList());

        return depenseList;
    }

    public List<Depense> allDepenseByIdUser(long idUtilisateur){
        List<Depense> depenseList = depenseRepository.findByUtilisateurIdUtilisateur(idUtilisateur);

        if(depenseList.isEmpty())
            throw new EntityNotFoundException("Aucun depense trouvé en fonction de la ");

        depenseList = depenseList
                .stream().sorted((d1, d2) -> d2.getDescription().compareTo(d1.getDescription()))
                .collect(Collectors.toList());

        return depenseList;
    }
    public List<Depense> allDepenseByIdAdmin(long idAdmin){
        List<Depense> depenseList = depenseRepository.findByAdminIdAdmin(idAdmin);

        if(depenseList.isEmpty())
            throw new EntityNotFoundException("Aucun depense trouvé ");

        depenseList = depenseList
                .stream().sorted((d1, d2) -> d2.getDescription().compareTo(d1.getDescription()))
                .collect(Collectors.toList());
        return depenseList;
    }
    public List<Depense> getDepenseByIdBudget(long idBudget){
        List<Depense> depensesList = depenseRepository.findByBudgetIdBudget(idBudget);

        depensesList = depensesList
                .stream().sorted((d1, d2) -> d2.getDescription().compareTo(d1.getDescription()))
                .collect(Collectors.toList());
        return depensesList;

    }
//    public List<Depense> allDepenseByIdDemande(long idDemande){
//        List<Depense> depenseList = depenseRepository.findAllDepenseByDemande_IdDemande(idDemande);
//
//        if(depenseList.isEmpty())
//            throw new EntityNotFoundException("Aucun depense trouvé en fonction de la demande");
//        return depenseList;
//    }
    public String deleteDepende(long id){
        Depense isDepenseExist = depenseRepository.findByIdDepense(id);
        if(isDepenseExist == null)
            throw  new EntityNotFoundException("cette depenses n'existe pas");

        depenseRepository.delete(isDepenseExist);
        return "Dépense supprimer avec succèss";
    }
}
