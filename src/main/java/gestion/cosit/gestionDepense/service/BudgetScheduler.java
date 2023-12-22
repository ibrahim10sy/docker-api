package gestion.cosit.gestionDepense.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class BudgetScheduler {

    @Autowired
    private  BudgetService budgetService;

    @Scheduled(cron = "0 0 0  1 * ?") // Exécuté à minuit chaque premier jour du mois
    public void  allocateBudgetMonthly(long idBudget){

        System.out.println("test budgetScheduler ");
        budgetService.allocateBudgetMonthlyById(idBudget);
    }
}
