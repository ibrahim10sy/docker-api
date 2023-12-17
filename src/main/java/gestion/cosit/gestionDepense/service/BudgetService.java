package gestion.cosit.gestionDepense.service;

import gestion.cosit.gestionDepense.Exception.NoContentException;
import gestion.cosit.gestionDepense.model.Admin;
import gestion.cosit.gestionDepense.model.Budget;
import gestion.cosit.gestionDepense.repository.AdminRepository;
import gestion.cosit.gestionDepense.repository.BudgetRepository;
import jakarta.persistence.EntityNotFoundException;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.HashMap;
import java.util.List;

@Service
public class BudgetService {

    @Autowired
     private BudgetRepository budgetRepository;
    @Autowired
    private AdminRepository adminRepository;


    @Transactional
    public Budget createBudget(Budget budget) {
        Admin admin = adminRepository.findByIdAdmin(budget.getAdmin().getIdAdmin());
         if(admin == null)
           throw new NoContentException("Ce admin n'existe pas");
        LocalDate toDay = LocalDate.now(); // Obtention de la date du jour en type LocalDate
        LocalDate dateDebut = budget.getDateDebut();
        LocalDate jourMaxDuMois = dateDebut.with(TemporalAdjusters.lastDayOfMonth()); // Obtention du dernier date du mois actuel
        budget.setDateFin(jourMaxDuMois);
        budget.setMontantRestant(budget.getMontant());
        return budgetRepository.save(budget);
    }

    @Transactional
    public void allocateBudgetMonthly(){
        LocalDate now = LocalDate.now();
        Budget budget = budgetRepository.findByDateFin(now.withDayOfMonth(now.lengthOfMonth()));
        if(budget != null){
            System.out.println("test allocation :"+budget);
            LocalDate dateDebut = budget.getDateDebut();
            LocalDate jourMaxDuMois = dateDebut.with(TemporalAdjusters.lastDayOfMonth()); // Obtention du dernier date du mois actuel
            budget.setDateFin(jourMaxDuMois);
            budget.setMontantRestant(budget.getMontant());
            System.out.println("fin du test allocation ");

            budgetRepository.save(budget);
        }
    }
//    public Budget createBudget(Budget budget) throws BadRequestException {
//        Admin admin = adminRepository.findByIdAdmin(budget.getAdmin().getIdAdmin());
//
//        if(admin == null)
//            throw new NoContentException("Ce admin n'existe pas");
//
//        LocalDate toDay = LocalDate.now(); // Obtention de la date du jour en type LocalDate
//        LocalDate dateDebut = budget.getDateDebut();
//        LocalDate jourMaxDuMois = dateDebut.with(TemporalAdjusters.lastDayOfMonth()); // Obtention du dernier date du mois actuel
//        budget.setDateFin(jourMaxDuMois);
//        budget.setMontantRestant(budget.getMontant());
//
//        if (dateDebut.getMonthValue() < toDay.getMonthValue() || (dateDebut.getYear() != toDay.getYear()))
//            throw new BadRequestException("Veuillez choisie une date dans "+toDay.getMonth()+" "+toDay.getYear());
//
//        Budget verifBudget = budgetRepository.findByDateFin(budget.getDateFin());
//        if (verifBudget != null )
//            throw new BadRequestException("Vous avez déjà un budget pour ce mois");
//
//        System.out.println("Budget service "+budget);
//        return budgetRepository.save(budget); // On sauvegarde ce budget dans notre base de donnée
//    }

    public Budget updateBudget(Budget budget, long id){
        Budget budget1 = budgetRepository.findById(id).orElseThrow(()-> new EntityNotFoundException("Ce budget nexiste pas l' ID spécifique "+id));

        budget1.setDescription(budget.getDescription());
        budget1.setMontant(budget.getMontant());
        budget1.setDateDebut(budget.getDateDebut());
        budget1.setDateFin(budget.getDateDebut());

        return budgetRepository.save(budget);
    }

    public List<Budget> allBudget(){
        List<Budget> budgetList = budgetRepository.findAll();

        // Si la liste est vide, le systÃ¨me lÃ¨vera une exception
        if (budgetList.isEmpty())
            throw new NoContentException("Aucun budget trouvÃ©");

        // Dans le cas contraire le systÃ¨me retourne la liste
        return budgetList;
    }

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
        Double[][] result = budgetRepository.getSommeOfTotalBudgetNotFinish();
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
