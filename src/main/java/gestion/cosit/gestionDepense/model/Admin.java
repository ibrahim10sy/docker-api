package gestion.cosit.gestionDepense.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Admin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idAdmin;

    @Column(length = 255, nullable = false)
    private  String nom;

    @Column(length = 255, nullable = false)
    private  String prenom;

    @Column(nullable = true)
    private String image;

    @Column(length = 255, nullable = false)
    private  String email;

    @Column(length = 255, nullable = false)
    private  String phone;

    @Column(length = 255, nullable = false)
    private  String motDePasse;

    @OneToMany(mappedBy = "admin", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Budget> budgetList;

    @OneToMany(mappedBy = "admin", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Demande> demandeList;

    @OneToMany(mappedBy = "admin",cascade = CascadeType.ALL)
    @JsonIgnore
    private List<CategorieDepense> categorieList;

    @OneToMany(mappedBy = "admin",cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Depense> depenseList;

    @OneToMany(mappedBy = "admin",cascade = CascadeType.ALL)
    @JsonIgnore
    private List<SendNotification> sendNotificationList;
}