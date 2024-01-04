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

    @GetMapping("/expensesByAdmin/{adminId}")
    @Operation(summary = "Donner la statistique des depenses par categorie")
    public List<Map<String, Object>> getTotalExpensesByCategoryByAdmin(@PathVariable Long adminId) {
        return procedureService.getTotalDepenseByAdmin(adminId);
    }

    @GetMapping("/expensesByUser/{userId}")
    @Operation(summary = "Donner la statistique des depenses par categorie")
    public List<Map<String, Object>> getTotalExpensesByCategoryByUser(@PathVariable Long userId) {
        return procedureService.getTotalDepenseByUser(userId);
    }

    @GetMapping("/expensesByTotal")
    @Operation(summary = "Donner la statistique des depenses par categorie")
    public List<Map<String, Object>> getTotalExpensesByCategory() {
        return procedureService.getTotalDepense();
    }
}
