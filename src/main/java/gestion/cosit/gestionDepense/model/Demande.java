package gestion.cosit.gestionDepense.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Data
public class Demande {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idDemande;

    @Column(length = 255, nullable = false)
    private String motif;

    @Column(nullable = false)
    private int montantDemande;


    @JsonFormat(pattern="yyyy-MM-dd")
    private Date dateDemande;

    @Column(nullable = true)
    private boolean autorisationDirecteur;

    @Column(nullable = true)
    private boolean autorisationAdmin;

    @ManyToOne
    private Admin admin;


    @ManyToOne
    private Utilisateur utilisateur;

    @OneToOne
    @JsonIgnore
    private  Depense depense;

    @OneToMany(mappedBy = "demande", orphanRemoval = true)
    @JsonIgnore
    private List<SendNotification> notifications;

}