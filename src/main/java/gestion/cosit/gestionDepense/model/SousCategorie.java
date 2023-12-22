package gestion.cosit.gestionDepense.model;


import jakarta.persistence.*;
import lombok.Data;

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

}
