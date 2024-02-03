package gestion.cosit.gestionDepense.service;

import gestion.cosit.gestionDepense.Exception.NoContentException;
import gestion.cosit.gestionDepense.model.*;
import gestion.cosit.gestionDepense.repository.*;
import jakarta.persistence.EntityNotFoundException;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.webjars.NotFoundException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
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
    @Autowired
    FileService fileService;

//    private static final String UPLOAD_DIRECTORY = "src/main/resources/images";
    @Async("asyncExecutor")
    public CompletableFuture<String> uploaderImageAsync(Depense depense, MultipartFile multipartFileImage) {
        try {
            // Générer un nom de fichier unique
            String fileName = UUID.randomUUID().toString() + fileService.getExtension(multipartFileImage.getOriginalFilename());

//            String imageUploadDirectory = "uploads"; // Chemin relatif dans le répertoire du projet
//            Path imageRootLocation = Paths.get(imageUploadDirectory);

            // Enregistrer l'image dans Firebase Storage et obtenir l'URL de téléchargement
            ResponseEntity<String> uploadResponse = fileService.upload(multipartFileImage, fileName);

            // Utiliser directement l'URL obtenue du téléversement dans Firebase Storage
            String imageUrl = uploadResponse.getBody();

            // Stocker l'URL de l'image dans l'objet depense
            depense.setImage(imageUrl);

            return CompletableFuture.completedFuture(imageUrl);
        } catch (Exception e) {
            // Gérer l'exception, par exemple en journalisant l'erreur
            e.printStackTrace();
            return CompletableFuture.failedFuture(e);
        }
    }

//    public Depense saveDepenseByUser(Depense depense , MultipartFile multipartFileImage) throws Exception {
//        Utilisateur utilisateur = utilisateurRepository.findByIdUtilisateur(depense.getUtilisateur().getIdUtilisateur());
//        SousCategorie sousCategorie = sousCategorieRepository.findByIdSousCategorie(depense.getSousCategorie().getIdSousCategorie());
//        Budget budget = budgetRepository.findByIdBudget(depense.getBudget().getIdBudget());
//        ParametreDepense parametreDepense = parametreDepenseRepository.findByIdParametre(depense.getParametreDepense().getIdParametre());
//
//        if(budget == null)
//            throw new BadRequestException("Desolé ce budget n'existe pas");
//
//        if(sousCategorie == null)
//            throw new BadRequestException("Desolé cette catégorie n'existe pas");
//
//        if (utilisateur == null)
//            throw new BadRequestException("Utilisateur invalid");
//
//        if (multipartFileImage != null) {
//            // Appeler la méthode uploaderImage de manière asynchrone
//            CompletableFuture<String> uploadTask = uploaderImageAsync(depense, multipartFileImage);
//
//            // Attendre la fin de la tâche asynchrone avant de continuer
//            uploadTask.join();
//        }
//
//        if (depense.getMontantDepense() > budget.getMontantRestant()) {
//            throw new BadRequestException("Le montant de la dépense ne doit pas être supérieur à celui du montant restant du budget " );
//        } else if (budget.getMontantRestant() == 0) {
//            throw new BadRequestException("Le  montant restant du budget est atteint" );
//
//        }
//        if (depense.getMontantDepense() >= depense.getParametreDepense().getMontantSeuil()) {
//            // Montant supérieur ou égal au seuil, envoie de notification
//            try {
//                System.out.println("Debut de l'envoi dans le service demande");
//                sendNotifService.sendDemande(depense);
//            } catch (BadRequestException e) {
//                throw new RuntimeException(e);
//            }
//            System.out.println("Fin de l'envoi dans le service demande");
//
//            // Ne pas ajouter la dépense immédiatement, attendre la validation de l'administrateur
//            depense.setAutorisationAdmin(false);
//
//            // Enregistrement initial avec la dépense non validée
//            depense = depenseRepository.save(depense);
//
//        } else {
//            // Montant inférieur au seuil, la dépense peut être enregistrée directement
//            // Mettre à jour le montant restant du budget
//            budget.setMontantRestant(budget.getMontantRestant() - depense.getMontantDepense());
//            depense.setViewed(false);
//
//            // Marquer la dépense comme validée car elle n'a pas besoin de validation administrative
//            depense.setAutorisationAdmin(true);
//
//            // Enregistrer la dépense mise à jour
//            depense = depenseRepository.save(depense);
//
//            try {
//                System.out.println("Debut de l'envoi dans le service demande dans else");
//                sendNotifService.sendDepense(depense);
//            } catch (BadRequestException e) {
//                throw new RuntimeException(e);
//            }
//            System.out.println("Fin de l'envoi dans le service demande else");
//        }
//
//        // Mettre à jour le montant restant du budget
//        budgetRepository.save(budget);
//
//        return depense;
//    }
public Depense saveDepenseByUser(Depense depense, MultipartFile multipartFileImage) throws Exception {
    Utilisateur utilisateur = utilisateurRepository.findByIdUtilisateur(depense.getUtilisateur().getIdUtilisateur());
    SousCategorie sousCategorie = sousCategorieRepository.findByIdSousCategorie(depense.getSousCategorie().getIdSousCategorie());
    Budget budget = budgetRepository.findByIdBudget(depense.getBudget().getIdBudget());
    ParametreDepense parametreDepense = parametreDepenseRepository.findByIdParametre(depense.getParametreDepense().getIdParametre());

    if(budget == null)
        throw new BadRequestException("Désolé ce budget n'existe pas");

    if(sousCategorie == null)
        throw new BadRequestException("Désolé cette catégorie n'existe pas");

    if (utilisateur == null)
        throw new BadRequestException("Utilisateur invalide");

    final Depense finalDepense = depense;
    CompletableFuture<Void> uploadTask = CompletableFuture.runAsync(() -> {
        try {
            if (multipartFileImage != null) {
                // Appeler la méthode uploaderImage de manière asynchrone
                uploaderImageAsync(finalDepense, multipartFileImage).join();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    });

    if (depense.getMontantDepense() > budget.getMontantRestant()) {
        throw new BadRequestException("Le montant de la dépense ne doit pas être supérieur à celui du montant restant du budget " );
    } else if (budget.getMontantRestant() == 0) {
        throw new BadRequestException("Le montant restant du budget est atteint" );
    }

    CompletableFuture<Void> sendNotifTask = CompletableFuture.runAsync(() -> {
        if (finalDepense.getMontantDepense() >= finalDepense.getParametreDepense().getMontantSeuil()) {
            // Montant supérieur ou égal au seuil, envoie de notification
            System.out.println("Debut de l'envoi dans le service demande");
            sendNotifService.sendDemandeAsync(finalDepense);
            System.out.println("Fin de l'envoi dans le service demande");

            // Ne pas ajouter la dépense immédiatement, attendre la validation de l'administrateur
            finalDepense.setAutorisationAdmin(false);

            // Enregistrement initial avec la dépense non validée
            depenseRepository.save(depense);
        } else {
            // Montant inférieur au seuil, la dépense peut être enregistrée directement
            // Mettre à jour le montant restant du budget
            budget.setMontantRestant(budget.getMontantRestant() - finalDepense.getMontantDepense());
            finalDepense.setViewed(false);

            // Marquer la dépense comme validée car elle n'a pas besoin de validation administrative
            finalDepense.setAutorisationAdmin(true);

            // Enregistrer la dépense mise à jour
             depenseRepository.save(depense);

            System.out.println("Debut de l'envoi dans le service demande dans else");
            sendNotifService.sendDepenseAsync(depense);
            System.out.println("Fin de l'envoi dans le service demande else");
        }

        // Mettre à jour le montant restant du budget
        budgetRepository.save(budget);
    });

    // Attendre que les deux tâches soient terminées
    CompletableFuture.allOf(uploadTask, sendNotifTask).join();

    return depense;
}
    @Transactional
    public Depense validateDepenseByAdmin(Long depenseId) throws Exception {
        try {
            Depense depense = depenseRepository.findById(depenseId)
                    .orElseThrow(() -> new NotFoundException("Dépense non trouvée"));
            Budget budget = depense.getBudget();
            depense.setAutorisationAdmin(true);
            if (depense.getMontantDepense() > budget.getMontantRestant()) {
                throw new BadRequestException("Le montant de la dépense ne doit pas être supérieur à celui du montant restant du budget");
            } else if (budget.getMontantRestant() == 0) {
                throw new BadRequestException("Le montant restant du budget est atteint");
            }
            depenseRepository.save(depense);
            System.out.println("Validation dans le ser depenses");
            sendNotifService.approuverDemandeByAdmin(depense);
            System.out.println("Fin Validation dans le ser depense");
            budget.setMontantRestant(budget.getMontantRestant() - depense.getMontantDepense());
            budgetRepository.save(budget);
            return depense;
        } catch (Exception e) {
            throw new Exception("Erreur lors de l'approbation de la demande : " + e.getMessage());
        }
    }


    public Depense saveDepenseByAdmin(Depense depense , MultipartFile multipartFileImage) throws Exception {
        Admin admin = adminRepository.findByIdAdmin(depense.getAdmin().getIdAdmin());
        Budget budget = budgetRepository.findByIdBudget(depense.getBudget().getIdBudget());
        SousCategorie sousCategorie = sousCategorieRepository.findByIdSousCategorie(depense.getSousCategorie().getIdSousCategorie());

        if(budget == null)
            throw new BadRequestException("Desolé ce budget n'existe pas");

        if(sousCategorie == null)
            throw new BadRequestException("Desolé cette catégorie n'existe pas");

        if (admin == null)
            throw new BadRequestException("Admin invalid");

        if (multipartFileImage != null) {
            // Appeler la méthode uploaderImage de manière asynchrone
            CompletableFuture<String> uploadTask = uploaderImageAsync(depense, multipartFileImage);

            // Attendre la fin de la tâche asynchrone avant de continuer
            uploadTask.join();
        }

        if (depense.getMontantDepense() > budget.getMontantRestant()) {
                throw new BadRequestException("Le montant de la dépense ne doit pas être supérieur à celui du montant restant du budget " );
        } else if (budget.getMontantRestant() == 0) {
            throw new BadRequestException("Le  montant restant du budget est atteint" );

        }
        // Mettre à jour le montant restant du budget
        budget.setMontantRestant(budget.getMontantRestant() - depense.getMontantDepense());
        depense.setViewed(false);
        depense.setAutorisationAdmin(true);
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
    public Depense updateDepense(Depense depense, long id, MultipartFile multipartFileImage) throws Exception {
        Depense isDepenseExist = depenseRepository.findById(id).orElseThrow(()-> new EntityNotFoundException("Depense non trouvé"));

//        isDepenseExist.setLibelle(depense.getLibelle());
        isDepenseExist.setDateDepense(depense.getDateDepense());
        isDepenseExist.setDescription(depense.getDescription());
        isDepenseExist.setMontantDepense(depense.getMontantDepense());
        if (multipartFileImage != null) {
            // Appeler la méthode uploaderImage de manière asynchrone
            CompletableFuture<String> uploadTask = uploaderImageAsync(depense, multipartFileImage);

            // Attendre la fin de la tâche asynchrone avant de continuer
            uploadTask.join();
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
