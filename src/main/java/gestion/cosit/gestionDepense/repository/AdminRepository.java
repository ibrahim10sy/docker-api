package gestion.cosit.gestionDepense.repository;

import gestion.cosit.gestionDepense.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends JpaRepository<Admin , Long> {
    Admin findByIdAdmin(long id);
    Admin findByEmailAndAndPassWord(String email, String passWord);

    Admin findByEmail(String email);
}