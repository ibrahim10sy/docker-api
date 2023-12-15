package gestion.cosit.gestionDepense.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
public class CategorieDepense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idCategoriedepense;

    @Column(length = 255, nullable = false)
    private String libelle;

    @JsonIgnore
    @OneToMany(mappedBy = "categorieDepense")
    private List<Depense> depenseList;

    @JsonIgnore
    @OneToMany(mappedBy = "categorieDepense")
    private List<SousCategorie> sousCategorieList;

    @ManyToOne
    private Utilisateur utilisateur;

}
