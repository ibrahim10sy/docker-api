package gestion.cosit.gestionDepense.repository;

import gestion.cosit.gestionDepense.model.SendNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SendNotifRepository  extends JpaRepository<SendNotification,Long> {

    SendNotification findByIdNotification(long id);
    List<SendNotification> findByUtilisateurIdUtilisateur(long idUtilisateur);
    List<SendNotification> findByAdminIdAdmin(long idAdmin);

}
