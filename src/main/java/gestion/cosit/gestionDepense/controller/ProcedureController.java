package gestion.cosit.gestionDepense.controller;

import gestion.cosit.gestionDepense.service.ProcedureService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/procedure")
public class ProcedureController {

    @Autowired
    private ProcedureService procedureService;

//    @GetMapping("/expensesByAdmin/{adminId}")
//    @Operation(summary = "Donner la statistique des depenses par categorie")
//    public List<Map<String, Object>> getTotalExpensesByCategoryByAdmin(@PathVariable Long adminId) {
//        return procedureService.getTotalDepenseByAdmin(adminId);
//    }

    @GetMapping("/expensesByUserByJour/{userId}")
    @Operation(summary = "Donner la statistique des depenses par categorie jours")
    public List<Map<String, Object>> getTotalExpensesByCategoryByUser(@PathVariable Long userId) {
        return procedureService.getTotalDepenseByUserByJour(userId);
    }
    @GetMapping("/expensesByUserByMois/{userId}")
    @Operation(summary = "Donner la statistique des depenses par categorie par mois")
    public List<Map<String, Object>> getTotalExpensesByCategoryByUserMois(@PathVariable Long userId) {
        return procedureService.getTotalDepenseByUserByMois(userId);
    }

    @GetMapping("/expensesTotalByUser/{userId}")
    @Operation(summary = "Donner la statistique des depenses par categorie par mois")
    public List<Map<String, Object>> getTotalExpensesByUser(@PathVariable Long userId) {
        return procedureService.getTotalDepenseByUser(userId);
    }
    @GetMapping("/expensesByTotalByJour")
    @Operation(summary = "Donner la statistique des depenses par categorie")
    public List<Map<String, Object>> getTotalExpensesByJour() {
        return procedureService.getTotalDepenseByJour();
    }

    @GetMapping("/expensesByTotalMois")
    @Operation(summary = "Donner la statistique des depenses par categorie")
    public List<Map<String, Object>> getTotalExpensesByMois() {
        return procedureService.getTotalDepenseByMois();
    }
    @GetMapping("/expensesByTotal")
    @Operation(summary = "Donner la statistique des depenses par categorie")
    public List<Map<String, Object>> getTotalExpensesByCategory() {
        return procedureService.getTotalDepense();
    }

    @GetMapping("/expensesByTotalBySousCategorie/{id}")
    @Operation(summary = "Donner la statistique des depenses par categorie")
    public List<Map<String, Object>> getTotalExpensesBySousCategory(@PathVariable long  id) {
        return procedureService.getTotalDepenseBySousCategorie(id);
    }
}
