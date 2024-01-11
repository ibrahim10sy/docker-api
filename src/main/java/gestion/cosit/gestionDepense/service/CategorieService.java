package gestion.cosit.gestionDepense.service;

import com.sun.jdi.request.DuplicateRequestException;
import gestion.cosit.gestionDepense.Exception.NoContentException;
import gestion.cosit.gestionDepense.model.CategorieDepense;
import gestion.cosit.gestionDepense.model.Depense;
import gestion.cosit.gestionDepense.model.SousCategorie;
import gestion.cosit.gestionDepense.model.Utilisateur;
import gestion.cosit.gestionDepense.repository.CategorieDepenseRepository;
import gestion.cosit.gestionDepense.repository.DepenseRepository;
import gestion.cosit.gestionDepense.repository.SousCategorieRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategorieService {

    @Autowired
    private CategorieDepenseRepository categorieRepository;

    @Autowired
    private DepenseRepository depenseRepository;
    @Autowired
    private SousCategorieRepository sousCategorieRepository;

    public CategorieDepense saveCategorieByUser(CategorieDepense categorieDepense)  {
        CategorieDepense categorieVerif = categorieRepository.findByUtilisateurAndLibelle(categorieDepense.getUtilisateur(), categorieDepense.getLibelle());
        if (categorieVerif != null) {
            throw new DuplicateRequestException("Une catégorie avec le libellé '" + categorieDepense.getLibelle() +
                    "' existe déjà pour ce personnel.");
        }
        return categorieRepository.save(categorieDepense);
    }

    public CategorieDepense saveCategorieAdmin(CategorieDepense categorieDepense)  {
        CategorieDepense categorieVerif = categorieRepository.findByAdminAndLibelle(categorieDepense.getAdmin(), categorieDepense.getLibelle());
        if (categorieVerif != null) {
            throw new DuplicateRequestException("Une catégorie avec le libellé '" + categorieDepense.getLibelle() +
                    "' existe déjà pour ce personnel.");
        }
        return categorieRepository.save(categorieDepense);
    }

    public CategorieDepense modifierCategorieDepense(CategorieDepense categorieDepense, long id){
        CategorieDepense isCategorieExist = categorieRepository.findById(id).orElseThrow(()-> new EntityNotFoundException("Catégorie non trouvé"));

        isCategorieExist.setLibelle(categorieDepense.getLibelle());

        return categorieRepository.save(isCategorieExist);
    }

    ///list des sous_catégorie
//    public List<SousCategorie> listSousCategorie(long idCategorieDepense) {
//        List<SousCategorie> sousCategorieList = sousCategorieRepository.findByCategorieDepense_IdCategoriedepense(idCategorieDepense);
//
//        if (sousCategorieList.isEmpty()) {
//            throw new NoContentException("Aucun sous-catégorie trouvé pour la catégorie avec l'ID : " + idCategorieDepense);
//        }
//
//        return sousCategorieList;
//    }


    public List<CategorieDepense> getAllCategorieDepenseByUser(long idUtilisateur){
        List<CategorieDepense>  categoriesListe = categorieRepository.findByUtilisateurIdUtilisateur(idUtilisateur);

        if(categoriesListe.isEmpty()){
            throw new EntityNotFoundException("Aucun categorie trouvé");
        }

        return categoriesListe;
    }

    public List<CategorieDepense> getAllCategorieDepenseByAdmin(long idAdmin){
        List<CategorieDepense>  categoriesListe = categorieRepository.findByAdminIdAdmin(idAdmin);

        if(categoriesListe.isEmpty()){
            throw new EntityNotFoundException("Aucun categorie trouvé");
        }

        return categoriesListe;
    }
    public List<CategorieDepense> lire(){
        return categorieRepository.findAll();
    }

    public String deleteCategorieById(long id){
        CategorieDepense categorieDepense = categorieRepository.findByIdCategoriedepense(id);
       if (categorieDepense == null)
            throw new NoContentException("Cette categorie n'existe pas");

        categorieRepository.delete(categorieDepense);
        return "Catégorie supprimé avec succèss";
    }
//    public String supprimer(long idCategoriedepense){
//        CategorieDepense categorieDepense = categorieRepository.findByIdCategoriedepense(idCategoriedepense);
//        if (categorieDepense == null)
//            throw new NoContentException("Cette categorie n'existe pas");
//
//        Depense depense = depenseRepository.findByCategorieDepense(categorieDepense);
//        if(depense != null)
//            throw new NoContentException("On peut pas supprimer une categorie qui est déjà associer à une depense");
//
//        categorieRepository.delete(categorieDepense);
//        return "supprimer avec succèss";
//    }


}
