package gestion.cosit.gestionDepense.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Bureau {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idBureau;

    @Column(length = 255, nullable = false)
    private String nom;

    @Column(length = 255, nullable = false)
    private String adresse;

    @JsonIgnore
    @OneToMany(mappedBy = "bureau",cascade = CascadeType.ALL)
    private List<Depense> depenseList;

}