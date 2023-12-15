package gestion.cosit.gestionDepense.repository;

import gestion.cosit.gestionDepense.model.Demande;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DemandeRepository extends JpaRepository<Demande,Long> {

    Demande findByIdDemande(long id);
    List<Demande> findByUtilisateur_IdUtilisateur(long IdUtilisateur);
}
