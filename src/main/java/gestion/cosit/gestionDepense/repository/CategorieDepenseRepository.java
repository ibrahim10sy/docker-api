package gestion.cosit.gestionDepense.repository;

import gestion.cosit.gestionDepense.model.Admin;
import gestion.cosit.gestionDepense.model.CategorieDepense;
import gestion.cosit.gestionDepense.model.SousCategorie;
import gestion.cosit.gestionDepense.model.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CategorieDepenseRepository extends JpaRepository<CategorieDepense,Long> {
    CategorieDepense findByIdCategoriedepense(long idCategoriedepense);

    //    CategorieDepense findByPersonnelAndIdCategorieDepense(Personnel personnel, long id);
    CategorieDepense findByUtilisateurAndLibelle(Utilisateur utilisateur, String libelle);
    CategorieDepense findByAdminAndLibelle(Admin admin, String libelle);

//    List<SousCategorie> findByCategorieDepense_idCategorieDepense(long id);

    List<CategorieDepense> findByAdminIdAdmin(long idAdmin);

    List<CategorieDepense> findByUtilisateurIdUtilisateur(long idUtilisateur);
}

