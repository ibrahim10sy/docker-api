package gestion.cosit.gestionDepense.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class ParametreDepense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idParametre;

    @Column
    private String description;

    @Column
    private int montantSeuil;
}
