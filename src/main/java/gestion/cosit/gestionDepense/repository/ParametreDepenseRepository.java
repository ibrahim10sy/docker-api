package gestion.cosit.gestionDepense.repository;

import gestion.cosit.gestionDepense.model.ParametreDepense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParametreDepenseRepository extends JpaRepository<ParametreDepense, Long > {
    ParametreDepense findByIdParametre(long id);
}
