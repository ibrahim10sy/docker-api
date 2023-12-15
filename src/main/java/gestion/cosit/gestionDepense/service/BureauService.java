package gestion.cosit.gestionDepense.service;

import gestion.cosit.gestionDepense.Exception.NoContentException;
import gestion.cosit.gestionDepense.model.Bureau;
import gestion.cosit.gestionDepense.repository.BureauRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BureauService {

    @Autowired
    private BureauRepository bureauRepository;

    public Bureau saveBureau(Bureau bureau) {
        Bureau existingBureau = bureauRepository.findByNom(bureau.getNom());

        if (existingBureau != null) {
            throw new DataIntegrityViolationException("Ce bureau existe déjà");
        }

        return bureauRepository.save(bureau);
    }

    public Bureau modifier(Bureau bureau, long id) {
        Bureau existingBureau = bureauRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Bureau non trouvé avec l'ID :" + id));

        existingBureau.setNom(bureau.getNom());
        existingBureau.setAdresse(bureau.getAdresse());

        return bureauRepository.save(existingBureau);
    }

    public List<Bureau> getAllBureau() {
        List<Bureau> bureauList = bureauRepository.findAll();

        if (bureauList.isEmpty()) {
            throw new NoContentException("Aucun bureau trouvé");
        }

        return bureauList;
    }

    public String supprimer(long id) {
        Bureau bureau = bureauRepository.findById(id)
                .orElseThrow(() -> new NoContentException("Bureau non trouvé"));

        bureauRepository.delete(bureau);
        return "Supprimé avec succès";
    }
}
