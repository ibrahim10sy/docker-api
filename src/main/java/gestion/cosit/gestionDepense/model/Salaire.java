package gestion.cosit.gestionDepense.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import gestion.cosit.gestionDepense.repository.MasquerMontant;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.util.Date;

@Data
@Entity
public class Salaire {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idSalaire;

    @Column(length = 255, nullable = false)
    private String description;

    @MasquerMontant
    @Column(nullable = false)
    private int montant;

    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    private LocalDate date;

    @ManyToOne
    private Utilisateur utilisateur;

    @ManyToOne
    private SousCategorie sousCategorie;
}