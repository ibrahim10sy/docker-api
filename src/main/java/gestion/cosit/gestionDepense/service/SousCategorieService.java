package gestion.cosit.gestionDepense.service;

import gestion.cosit.gestionDepense.Exception.NoContentException;
import gestion.cosit.gestionDepense.model.CategorieDepense;
import gestion.cosit.gestionDepense.model.Depense;
import gestion.cosit.gestionDepense.model.SousCategorie;
import gestion.cosit.gestionDepense.repository.CategorieDepenseRepository;
import gestion.cosit.gestionDepense.repository.DepenseRepository;
import gestion.cosit.gestionDepense.repository.SousCategorieRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SousCategorieService {

    @Autowired
    private SousCategorieRepository sousCategorieRepository;
    @Autowired
    private CategorieDepenseRepository categorieDepenseRepository;
    @Autowired
    private DepenseRepository depenseRepository;

    public SousCategorie saveSousCategorie(SousCategorie sousCategorie){
        CategorieDepense categorieDepense = categorieDepenseRepository.findByIdCategoriedepense(sousCategorie.getCategorieDepense().getIdCategoriedepense());
        if(categorieDepense == null)
            throw new NoContentException("Catégorie non trouvé");

        SousCategorie sousCategorie1 = sousCategorieRepository.findByIdSousCategorie(sousCategorie.getIdSousCategorie());
        if(sousCategorie1 != null)
            throw new NoContentException("Ce sous categorie existe déjà");
        return sousCategorieRepository.save(sousCategorie);
    }

    public SousCategorie modifierSousCategorie(SousCategorie sousCategorie, long id){
        SousCategorie isCategorieExist = sousCategorieRepository.findById(id).orElseThrow(()-> new EntityNotFoundException("Sous catégorie non trouvé"));

        isCategorieExist.setLibelle(sousCategorie.getLibelle());

        return sousCategorieRepository.save(isCategorieExist);
    }

    public List<SousCategorie> getAllSousCategorieByUser(long idCategorieDepense){
        List<SousCategorie>  categoriesListe = sousCategorieRepository.findByCategorieDepense_IdCategoriedepense(idCategorieDepense);

        if(categoriesListe.isEmpty()){
            throw new EntityNotFoundException("Aucun sous categorie categorie trouvé");
        }

        return categoriesListe;
    }
    public String supprimer(long idSousCategorie){
        CategorieDepense categorie = categorieDepenseRepository.findById(idSousCategorie).orElseThrow(()-> new NoContentException("Sous catégorie non trouvé"));
        if (categorie == null)
            throw new NoContentException("Cette sous categorie n'existe pas");

        Depense depense = depenseRepository.findByIdDepense(idSousCategorie);
        if(depense != null)
            throw new NoContentException("On peut pas supprimer une categorie qui est déjà associer à une depense");

        categorieDepenseRepository.delete(categorie);
        return "supprimer avec succèss";
    }
}
