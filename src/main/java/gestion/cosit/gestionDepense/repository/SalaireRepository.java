package gestion.cosit.gestionDepense.repository;

import gestion.cosit.gestionDepense.model.Salaire;
import gestion.cosit.gestionDepense.model.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface SalaireRepository extends JpaRepository<Salaire,Long> {
    Salaire findByIdSalaire(long id);
    Optional<Salaire> findByUtilisateurAndDate(Utilisateur utilisateur, LocalDate date);

    List<Salaire> findByUtilisateur_IdUtilisateur(long idUtilisateur);
    @Query(value = "SELECT sum(montant) FROM Salaire", nativeQuery = true)
    Integer[] getSommeOfTotalSalaireNotFinish();
}
