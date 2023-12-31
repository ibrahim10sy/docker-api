package gestion.cosit.gestionDepense.repository;

import gestion.cosit.gestionDepense.model.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UtilisateurRepository extends JpaRepository<Utilisateur,Long> {

    public Utilisateur findByEmailAndPassWord(String email, String passWord);

    public  Utilisateur findByEmail(String email);

    public  Utilisateur findByIdUtilisateur(long id);
}
