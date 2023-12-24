package gestion.cosit.gestionDepense.repository;

import gestion.cosit.gestionDepense.model.CategorieDepense;
import gestion.cosit.gestionDepense.model.SousCategorie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SousCategorieRepository extends JpaRepository<SousCategorie, Long> {
    SousCategorie findByIdSousCategorie(long id);
    //    List<SousCategorie> findByCategorie_idCategorie(long id);
//    List<SousCategorie> findByCategorieDepense_IdCategoriedepense(long idCategorieDepense);

    List<SousCategorie> findByCategorieDepenseIdCategoriedepense(long idCategoriedepense);
}
