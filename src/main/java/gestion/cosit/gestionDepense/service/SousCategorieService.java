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
public List<SousCategorie> getAllSousCategorie(long idCategoriedepense){
        List<SousCategorie> sousCategorieList = sousCategorieRepository.findByCategorieDepenseIdCategoriedepense(idCategoriedepense);
        if(sousCategorieList.isEmpty())
            throw new NoContentException("Aucune sous categorie trouvé");
        return sousCategorieList;
}
    public String supprimer(long idSousCategorie){
        SousCategorie sousCategorie = sousCategorieRepository.findByIdSousCategorie(idSousCategorie);
        if (sousCategorie == null)
            throw new NoContentException("Cette sous categorie n'existe pas");

        Depense depense = depenseRepository.findByIdDepense(idSousCategorie);
        if(depense != null)
            throw new NoContentException("On peut pas supprimer une categorie qui est déjà associer à une depense");

        sousCategorieRepository.delete(sousCategorie);
        return "supprimer avec succèss";
    }

}
