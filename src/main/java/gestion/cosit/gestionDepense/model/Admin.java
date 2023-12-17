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

    @OneToMany
    @JsonIgnore
    private List<Budget> budgetList;

    @OneToMany
    @JsonIgnore
    private List<Demande> demandeList;

}