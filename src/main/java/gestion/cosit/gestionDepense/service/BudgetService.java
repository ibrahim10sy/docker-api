package gestion.cosit.gestionDepense.service;

import gestion.cosit.gestionDepense.Exception.NoContentException;
import gestion.cosit.gestionDepense.model.*;
import gestion.cosit.gestionDepense.repository.AdminRepository;
import gestion.cosit.gestionDepense.repository.BudgetRepository;
import gestion.cosit.gestionDepense.repository.UtilisateurRepository;
import jakarta.persistence.EntityNotFoundException;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
public class BudgetService {

    @Autowired
     private BudgetRepository budgetRepository;
    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private UtilisateurRepository utilisateurRepository;


    @Transactional
    public Budget createBudget(Budget budget) {
        Admin admin = adminRepository.findByIdAdmin(budget.getAdmin().getIdAdmin());
         if(admin == null)
           throw new EntityNotFoundException("Ce admin n'existe pas");
//        Utilisateur utilisateur = utilisateurRepository.findByIdUtilisateur(budget.getUtilisateur().getIdUtilisateur());


        LocalDate toDay = LocalDate.now(); // Obtention de la date du jour en type LocalDate
        LocalDate dateDebut = budget.getDateDebut();
        LocalDate jourMaxDuMois = dateDebut.with(TemporalAdjusters.lastDayOfMonth()); // Obtention du dernier date du mois actuel
        budget.setDateFin(jourMaxDuMois);
        budget.setMontantRestant(budget.getMontant());
        return budgetRepository.save(budget);
    }

    @Transactional
    public void allocateBudgetMonthlyById(long budgetId) {
        // Load the specific budget by ID
        Optional<Budget> optionalBudget = budgetRepository.findById(budgetId);

        if (optionalBudget.isPresent()) {
            Budget existingBudget = optionalBudget.get();
            System.out.println("Allocation monthly test for budget with ID " + budgetId + ": " + existingBudget);

            // Clone existing budget
            Budget newBudget = new Budget();
            newBudget.setDescription(existingBudget.getDescription());
            newBudget.setMontant(existingBudget.getMontant());
            newBudget.setDateDebut(existingBudget.getDateDebut());
            newBudget.setDateFin(existingBudget.getDateFin());
            newBudget.setAdmin(existingBudget.getAdmin());
            newBudget.setMontantRestant(existingBudget.getMontant());

            // Save the new budget
            budgetRepository.save(newBudget);

            System.out.println("Allocation monthly completed for budget with ID " + budgetId + ": " + newBudget);
        }
    }

//    public Budget updateBudget(Budget budget, long id) {
//        // Récupérer le budget existant par son ID
//        Budget existingBudget = budgetRepository.findById(id)
//                .orElseThrow(() -> new EntityNotFoundException("Ce budget n'existe pas avec l'ID spécifique " + id));
//
//        // Mettre à jour les champs du budget existant avec les valeurs du nouveau budget
//        existingBudget.setDescription(budget.getDescription());
//        existingBudget.setMontant(budget.getMontant());
//        existingBudget.setDateDebut(budget.getDateDebut());
//
//        // Calculer la date de fin en utilisant le dernier jour du mois de la date de début
//        LocalDate dateDebut = existingBudget.getDateDebut();
//        LocalDate jourMaxDuMois = dateDebut.with(TemporalAdjusters.lastDayOfMonth());
//        existingBudget.setDateFin(jourMaxDuMois);
//
//        // Mettre à jour le montant restant avec le montant du nouveau budget
//        existingBudget.setMontantRestant(budget.getMontant());
//
//        // Sauvegarder les modifications dans la base de données en utilisant le repository
//        return budgetRepository.save(existingBudget);
//    }
    public Budget updateBudget(Budget budget, long id){
        Budget existingBudget = budgetRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Ce budget n'existe pas avec l'ID spécifique " + id));
        existingBudget.setDescription(budget.getDescription());
        existingBudget.setMontant(budget.getMontant());
        existingBudget.setDateDebut(budget.getDateDebut());
        LocalDate date = budget.getDateDebut();
        LocalDate jourMaxDuMois = date.with(TemporalAdjusters.lastDayOfMonth());
        existingBudget.setDateFin(jourMaxDuMois);

        return budgetRepository.save(existingBudget);
    }

    public List<Budget> allBudget(){
        List<Budget> budgetList = budgetRepository.findAll();

        // Si la liste est vide, le systÃ¨me lÃ¨vera une exception
        if (budgetList.isEmpty())
            throw new NoContentException("Aucun budget trouvÃ©");

        // Dans le cas contraire le systÃ¨me retourne la liste
        return budgetList;
    }
    public List<Budget> getAllBudgetByUser(long idUtilisateur){
        List<Budget>  budgetListe = budgetRepository.findByUtilisateurIdUtilisateur(idUtilisateur);

        if(budgetListe.isEmpty()){
            throw new EntityNotFoundException("Aucun budget trouvé pour ce utilisateur");
        }

        return budgetListe;
    }
    public List<Budget> allBudgetByUser(long idUtilisateur){
        List<Budget> budgetList = budgetRepository.findByUtilisateurIdUtilisateur(idUtilisateur);

        // Si la liste est vide, le systÃ¨me lÃ¨vera une exception
        if (budgetList.isEmpty())
            throw new NoContentException("Aucun budget trouvÃ©");

        // Dans le cas contraire le systÃ¨me retourne la liste
        return budgetList;
    }

//    public List<Budget> allBudgetByAdmin(long idAdmin){
//        List<Budget> budgetList = budgetRepository.findByAdminIdAdmin(idAdmin);
//
//        // Si la liste est vide, le systÃ¨me lÃ¨vera une exception
//        if (budgetList.isEmpty())
//            throw new NoContentException("Aucun budget trouvÃ©");
//
//        // Dans le cas contraire le systÃ¨me retourne la liste
//        return budgetList;
//    }
    public List<Budget> searchBudget(String desc){

        // Obtention de tous les budget dans la base de donnÃ©es
        List<Budget> budgetList = budgetRepository.findByDescriptionContaining(desc);

        // Si la liste est vide, le systÃ¨me lÃ¨vera une exception
        if (budgetList.isEmpty())
            throw new EntityNotFoundException("Aucun budget trouvÃ©");

        // Dans le cas contraire le systÃ¨me retourne la liste
        return budgetList;
    }

    public List<Budget> sortBudgetByMonthAndYear( String date){

        // Obtention de tous les budget dans la base de donnÃ©es
        List<Budget> budgetList = budgetRepository.getBudgetByMonthAndYear("%"+date+"%");

        // Si la liste est vide, le systÃ¨me lÃ¨vera une exception
        if (budgetList.isEmpty())
            throw new EntityNotFoundException("Aucun budget trouvÃ©");

        // Dans le cas contraire le systÃ¨me retourne la liste
        return budgetList;
    }

    public HashMap<String,Object> sommeOfAllBudgetNotFinish(){
        HashMap<String , Object> hashMap = new HashMap<>();
        Integer[][] result = budgetRepository.getSommeOfTotalBudgetNotFinish();
        if(result[0][0] == null || result[0][1] == null){
            hashMap.put("Total",0);
            hashMap.put("Restant",0);
        }
        else{
            hashMap.put("Total",result[0][0]);
            hashMap.put("Restant",result[0][1]);
        }
        return hashMap;
    }
    public String deleteBudget(long idBudget) throws BadRequestException {
        Budget budgetVerif = budgetRepository.findByIdBudget(idBudget);

        // Si budgetVerif est null, alors le budget n'a pas Ã©tÃ©t trouvÃ© et le systÃ¨me lÃ¨vera une exception
        if (budgetVerif == null)
            throw  new EntityNotFoundException("Aucun budget trouvÃ©");

        if (budgetVerif.getDateFin().isBefore(LocalDate.now()))
            throw new BadRequestException("DÃ©solÃ© vous ne pouvez pas modifier un budget dÃ©jÃ  expirer");

        if (!budgetVerif.getDepenseList().isEmpty())
            throw new BadRequestException("DÃ©solÃ© vous ne pouvez plus supprimer cet budget car possÃ¨de dÃ©jÃ  au moins une depense");

        budgetRepository.deleteById(idBudget);
        return "succes";
    }
}
