package gestion.cosit.gestionDepense.repository;

import gestion.cosit.gestionDepense.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DepenseRepository extends JpaRepository<Depense,Long> {

    Depense findByIdDepense(long id);
    List<Depense> findByUtilisateurAndDateDepenseBetween(Utilisateur utilisateur, LocalDate startDate, LocalDate endDate);
    Depense findByUtilisateurAndDateDepense(Utilisateur utilisateur, LocalDate localDate);

    Depense findBySousCategorie(SousCategorie sousCategorie);
    List<Depense> findAllDepenseByDemande_IdDemande(long idDemande);
    List<Depense> findByUtilisateurIdUtilisateur(long idUtilisateur);
    List<Depense> findByAdminIdAdmin(long idAdmin);

    List<Depense> findByBudgetIdBudget(long idBudget);

    List<Depense> findByDescriptionContaining( String desc);
    @Query(value = "SELECT * FROM Depense WHERE  date_depense LIKE :date ",nativeQuery = true)
    List<Depense> getDepenseByMonthAndYear(@Param("date") String date);

    @Query(value = "SELECT sum(montant_depense) FROM Depense WHERE budget_id_budget = :idBudget",nativeQuery = true)
    Integer getSommeTotalDepensesParIdBudget(@Param("idBudget") long idBudget);}

