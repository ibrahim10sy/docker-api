package gestion.cosit.gestionDepense.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Utilisateur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idUtilisateur;

    @Column(length = 255, nullable = false)
    private  String nom;

    @Column(length = 255, nullable = false)
    private  String prenom;

    @Column(length = 255, nullable = false)
    private  String email;

    @Column(nullable = true)
    private  String image;

    @Column(length = 255, nullable = false)
    private  String role;

    @Column(length = 255, nullable = false)
    private  String phone;

    @Column(length = 255, nullable = false)
    private  String motDePasse;

    @OneToMany(mappedBy = "utilisateur", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Budget> budgetList;

    @OneToMany(mappedBy = "utilisateur",cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Salaire> salaireList;

    @OneToMany(mappedBy = "utilisateur",cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Demande> demandeList;

    @OneToMany(mappedBy = "utilisateur", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<CategorieDepense> categorieList;

    @OneToMany(mappedBy = "utilisateur", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Depense> depenseList;
}

