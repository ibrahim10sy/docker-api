package gestion.cosit.gestionDepense.repository;

import gestion.cosit.gestionDepense.model.CategorieDepense;
import gestion.cosit.gestionDepense.model.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategorieDepenseRepository extends JpaRepository<CategorieDepense,Long> {
    CategorieDepense findByIdCategoriedepense(long idCategoriedepense);

    //    CategorieDepense findByPersonnelAndIdCategorieDepense(Personnel personnel, long id);
    CategorieDepense findByUtilisateurAndLibelle(Utilisateur utilisateur, String libelle);

//    List<SousCategorie> findByCategorieDepense_idCategorieDepense(long id);

    List<CategorieDepense> findByUtilisateurIdUtilisateur(long idUtilisateur);

}

