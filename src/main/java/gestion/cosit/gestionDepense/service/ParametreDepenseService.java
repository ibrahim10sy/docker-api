package gestion.cosit.gestionDepense.service;

import gestion.cosit.gestionDepense.model.ParametreDepense;
import gestion.cosit.gestionDepense.repository.ParametreDepenseRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ParametreDepenseService {

    @Autowired
    private ParametreDepenseRepository parametreDepenseRepository;

    public ParametreDepense saveParametre(ParametreDepense parametreDepense){
        return parametreDepenseRepository.save(parametreDepense);
    }

    public ParametreDepense updateParametre(ParametreDepense parametreDepense, long id){
        ParametreDepense parametreDepense1 = parametreDepenseRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Aucun parametre trouvé"));

        parametreDepense1.setDescription(parametreDepense1.getDescription());
        parametreDepense1.setMontantSeuil(parametreDepense.getMontantSeuil());

        return parametreDepenseRepository.save(parametreDepense1);
    }

    public List<ParametreDepense> getAll(){
        List<ParametreDepense> parametreDepenses = parametreDepenseRepository.findAll();
        if(parametreDepenses.isEmpty())
            throw new EntityNotFoundException("Aucun parametre trouvé");
        return parametreDepenses;
    }

    public String deleteParametre(int id){
        ParametreDepense parametreDepense = parametreDepenseRepository.findByIdParametre(id);

        parametreDepenseRepository.delete(parametreDepense);
        return "Supprimer avec success";
    }
}
