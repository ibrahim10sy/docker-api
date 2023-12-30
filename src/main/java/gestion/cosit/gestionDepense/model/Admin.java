package gestion.cosit.gestionDepense.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
@JsonIgnoreProperties({"budgetList", "demandeList", "categorieList", "depenseList", "sendNotificationList"})
public class Admin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idAdmin;

    @Column(length = 255, nullable = false)
    private String nom;

    @Column(length = 255, nullable = false)
    private String prenom;

    @Column(nullable = true)
    private String image;

    @Column(length = 255, nullable = false)
    private String email;

    @Column(length = 255, nullable = false)
    private String phone;

    @Column(length = 255, nullable = false)
    private String passWord;

    @OneToMany(mappedBy = "admin", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Budget> budgetList;

    @OneToMany(mappedBy = "admin", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Demande> demandeList;

    @OneToMany(mappedBy = "admin", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<CategorieDepense> categorieList;

    @OneToMany(mappedBy = "admin", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Depense> depenseList;

    @OneToMany(mappedBy = "admin", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<SendNotification> sendNotificationList;

    @Override
    public String toString() {
        return "Admin{idAdmin=" + idAdmin + ", nom='" + nom + "', prenom='" + prenom + " email= '"+ email +"' passWord='" + passWord+ "' phone ='"+ phone +"'}";
    }
}
