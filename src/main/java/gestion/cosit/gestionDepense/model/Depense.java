package gestion.cosit.gestionDepense.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Data
@Entity
public class Depense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idDepense;

    @Column(nullable = true)
    private  String image;

    @Column(length = 255, nullable = false)
    private String description;

    @Column(length = 255, nullable = false)
    private int montantDepense;


    @NotNull(message = "Désolé, la date ne doit pas être null")
    @Column(nullable = false)
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date dateDepense;

    @Column(nullable = false)
    private boolean isViewed;

    @Column(nullable = true)
    private boolean autorisationAdmin;

    @ManyToOne
    @JoinColumn(name = "utilisateur_id")
    private Utilisateur utilisateur;

    @ManyToOne
    @JoinColumn(name = "admin_id")
    private Admin admin;

//    @OneToOne(mappedBy = "depense", cascade = CascadeType.ALL, orphanRemoval = true)
//    private Demande demande;

    @ManyToOne
    private SousCategorie sousCategorie;

    @ManyToOne
    private Bureau bureau;

    @ManyToOne
    private Budget budget;

    @ManyToOne
    private ParametreDepense parametreDepense;

    @OneToMany(mappedBy = "depense", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<SendNotification> notifications;
}
