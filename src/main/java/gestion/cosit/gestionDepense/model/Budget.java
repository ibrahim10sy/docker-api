package gestion.cosit.gestionDepense.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.util.Date;
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

    @NotNull(message = "Désolé, la date ne doit pas être null")
    @Column(nullable = false)
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date dateDebut;

    @NotNull(message = "Désolé, la date ne doit pas être null")
    @Column(nullable = false)
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date dateFin;

    @NotNull
    @ManyToOne
    private Admin admin;

    @ManyToOne
    private Utilisateur utilisateur;

    @OneToMany(mappedBy = "budget", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Depense> depenseList;

    // Mise à jour de la méthode toString()
//    @Override
//    public String toString() {
//        return "Budget{idBudget=" + idBudget + ", description='" + description + "', montant=" + montant + ", montantRestant=" + montantRestant + ", dateDebut=" + dateDebut + ", dateFin=" + dateFin + "}";
//    }
}
