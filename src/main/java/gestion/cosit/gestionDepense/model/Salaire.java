package gestion.cosit.gestionDepense.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Date;

@Data
@Entity
public class Salaire {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idSalaire;

    @Column(length = 255, nullable = false)
    private String description;

    @Column(nullable = false)
    private double montant;

    @NotNull(message = "Désolé, la date ne doit pas être null")
    @Column(nullable = false)
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date date;

    @ManyToOne
    private Utilisateur utilisateur;

}