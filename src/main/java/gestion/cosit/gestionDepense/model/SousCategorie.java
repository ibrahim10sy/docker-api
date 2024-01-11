package gestion.cosit.gestionDepense.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class SousCategorie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idSousCategorie;

    @Column(length = 255, nullable = false)
    private String libelle;

    @ManyToOne
    private CategorieDepense categorieDepense;

    @JsonIgnore
    @OneToMany(mappedBy = "sousCategorie",cascade = CascadeType.ALL)
    private List<Depense> depenseList;

    @JsonIgnore
    @OneToMany(mappedBy = "sousCategorie",cascade = CascadeType.ALL)
    private List<Salaire> salaireList;
}
