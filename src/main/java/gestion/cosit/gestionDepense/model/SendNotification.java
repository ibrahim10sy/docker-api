package gestion.cosit.gestionDepense.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Data
public class SendNotification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idNotification ;

    @NotBlank(message = "Le texte de notification ne doit pas être null")
    @Column(nullable = false)
    private String message ;

    @NotNull(message = "Désolé, la date ne doit pas être null")
    @Column(nullable = false)
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date date;

    @ManyToOne
    @JoinColumn(name = "idUtilisateur")
    private Utilisateur utilisateur;

    @ManyToOne
    @JoinColumn(name = "idAdmin")
    private Admin admin;

    @ManyToOne
    @JoinColumn(name="idDepense")
    private Depense depense;
}
