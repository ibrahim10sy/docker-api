package gestion.cosit.gestionDepense.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
public class Budget {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idBudget;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private int montant;

    @Column(nullable = false)
    private int montantRestant;

    @NotNull(message = "Désole la date ne doit pas etre null")
    @Column(nullable = false)
    private LocalDate dateDebut;


    @Column(nullable = false)
    private LocalDate dateFin;

    @NotNull
    @ManyToOne
    private Admin admin;

    @NotNull
    @ManyToOne
    private Utilisateur utilisateur;
    @OneToMany(mappedBy = "budget",cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Depense> depenseList;

}
