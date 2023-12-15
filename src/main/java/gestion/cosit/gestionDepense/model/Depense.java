package gestion.cosit.gestionDepense.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
public class Depense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idDepense;

    @Column(length = 255, nullable = false)
    private String libelle;

    @Column(length = 255, nullable = false)
    private String description;

    @Column(length = 255, nullable = false)
    private double montantDepense;

    @Column(length = 255, nullable = false)
    private double montantPlafondRestant;

    @Column(length = 255, nullable = false)
    private double montantPlafond;

    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    private LocalDate dateDepense;


    @ManyToOne
    @JoinColumn(name = "personnel_id")  // Nom de la colonne qui stocke la clé étrangère dans la table Depense
    private Utilisateur utilisateur;


    @OneToOne(mappedBy = "depense", cascade = CascadeType.ALL, orphanRemoval = true)
    private Demande demande;

    @ManyToOne
    private CategorieDepense categorieDepense;

    @ManyToOne
    private Bureau bureau;

}
