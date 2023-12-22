package gestion.cosit.gestionDepense.repository;

import gestion.cosit.gestionDepense.model.CategorieDepense;
import gestion.cosit.gestionDepense.model.Depense;
import gestion.cosit.gestionDepense.model.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DepenseRepository extends JpaRepository<Depense,Long> {

    Depense findByIdDepense(long id);
    List<Depense> findByUtilisateurAndDateDepenseBetween(Utilisateur utilisateur, LocalDate startDate, LocalDate endDate);
    Depense findByUtilisateurAndDateDepense(Utilisateur utilisateur, LocalDate localDate);

    Depense findByCategorieDepense(CategorieDepense categorieDepense);
    List<Depense> findAllDepenseByDemande_IdDemande(long idDemande);
    List<Depense> findByUtilisateurIdUtilisateur(long idUtilisateur);
    List<Depense> findByAdminIdAdmin(long idAdmin);

    List<Depense> findByBudgetIdBudget(long idBudget);
}

