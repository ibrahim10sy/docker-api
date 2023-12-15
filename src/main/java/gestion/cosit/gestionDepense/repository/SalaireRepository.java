package gestion.cosit.gestionDepense.repository;

import gestion.cosit.gestionDepense.model.Salaire;
import gestion.cosit.gestionDepense.model.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface SalaireRepository extends JpaRepository<Salaire,Long> {
    Salaire findByIdSalaire(long id);
    Optional<Salaire> findByUtilisateurAndDate(Utilisateur utilisateur, Date date);

    List<Salaire> findByUtilisateur_IdUtilisateur(long idUtilisateur);
}
