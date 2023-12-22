package gestion.cosit.gestionDepense.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.util.Date;

@Data
@Entity
public class Depense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idDepense;

    @Column(length = 255, nullable = false)
    private String libelle;

//    @Column(nullable = true)
//    private  String image;

    @Column(length = 255, nullable = false)
    private String description;

    @Column(length = 255, nullable = false)
    private int montantDepense;


    @NotNull(message = "Désolé, la date ne doit pas être null")
    @Column(nullable = false)
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date dateDepense;


    @ManyToOne
    @JoinColumn(name = "utilisateur_id")
    private Utilisateur utilisateur;

    @ManyToOne
    @JoinColumn(name = "admin_id")
    private Admin admin;

    @OneToOne(mappedBy = "depense", cascade = CascadeType.ALL, orphanRemoval = true)
    private Demande demande;

    @ManyToOne
    private CategorieDepense categorieDepense;

    @ManyToOne
    private Bureau bureau;

    @ManyToOne
    private Budget budget;
}
