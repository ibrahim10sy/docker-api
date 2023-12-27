package gestion.cosit.gestionDepense.repository;

import gestion.cosit.gestionDepense.model.Budget;
import gestion.cosit.gestionDepense.model.CategorieDepense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BudgetRepository extends JpaRepository<Budget,Long> {

    Budget findByIdBudget(long id);

//    Budget findByDateFin(LocalDate localDate);
    Budget findByDateFin(LocalDate dateFin);
    List<Budget> findByDescriptionContaining( String desc);
    List<Budget> findByUtilisateurIdUtilisateur(long idUtilisateur);
    List<Budget> findByAdminIdAdmin(long idAdmin);
    @Query(value = "SELECT * FROM Budget WHERE  date_debut LIKE :date ",nativeQuery = true)
    List<Budget> getBudgetByMonthAndYear(@Param("date") String date);

    @Query(value = "SELECT sum(montant), sum(montant_restant) FROM Budget WHERE utilisateur_id_utilisateur = :idUtilisateur",nativeQuery = true)
    Integer[][] getSommeOfTotalBudgetNotFinishByUser(@Param("idUtilisateur") long idUtilisateur);

    @Query(value = "SELECT sum(montant), sum(montant_restant) FROM Budget WHERE admin_id_admin = :idAdmin",nativeQuery = true)
    Integer[][] getSommeOfTotalBudgetNotFinishByAdmin(@Param("idAdmin") long idAdmin);

}
