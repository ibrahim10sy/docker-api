package gestion.cosit.gestionDepense.repository;

import gestion.cosit.gestionDepense.model.Bureau;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BureauRepository extends JpaRepository<Bureau, Long> {

    Bureau findByIdBureau(long id);
    Bureau findByNom(String nom);
}