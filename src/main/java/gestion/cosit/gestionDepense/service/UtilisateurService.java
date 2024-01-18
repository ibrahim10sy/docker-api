package gestion.cosit.gestionDepense.service;

import gestion.cosit.gestionDepense.Exception.NoContentException;
import gestion.cosit.gestionDepense.model.CategorieDepense;
import gestion.cosit.gestionDepense.model.SendNotification;
import gestion.cosit.gestionDepense.model.Utilisateur;
import gestion.cosit.gestionDepense.repository.UtilisateurRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UtilisateurService {

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    //Methode pour créer un user

    public Utilisateur createUser(Utilisateur utilisateur, MultipartFile multipartFileImage) throws Exception {
        if(utilisateurRepository.findByEmail(utilisateur.getEmail()) == null){
            if (multipartFileImage != null) {
                String location = "C:\\xampp\\htdocs\\cosit";
                System.out.println("verif");
                try {
                    Path rootlocation = Paths.get(location);
                    if (!Files.exists(rootlocation)) {
                        Files.createDirectories(rootlocation);
                        Files.copy(multipartFileImage.getInputStream(),
                                rootlocation.resolve(multipartFileImage.getOriginalFilename()));
                        utilisateur.setImage("cosit/"
                                + multipartFileImage.getOriginalFilename());
                    } else {
                        System.out.println("Autre condition");
                        try {
                            String nom = location + "\\" + multipartFileImage.getOriginalFilename();
                            Path name = Paths.get(nom);
                            if (!Files.exists(name)) {
                                Files.copy(multipartFileImage.getInputStream(),
                                        rootlocation.resolve(multipartFileImage.getOriginalFilename()));
                                utilisateur.setImage("cosit/"
                                        + multipartFileImage.getOriginalFilename());
                            } else {
                                Files.delete(name);
                                Files.copy(multipartFileImage.getInputStream(), rootlocation.resolve(multipartFileImage.getOriginalFilename()));
                                utilisateur.setImage("cosit/"
                                        + multipartFileImage.getOriginalFilename());
                            }
                        } catch (Exception e) {
                            throw new Exception("Impossible de télécharger l\'image");
                        }
                    }
                } catch (Exception e) {
                    throw new Exception(e.getMessage());
                }
            }
            System.out.println("user service"+utilisateur);
            return  utilisateurRepository.save(utilisateur);
        }else{
            throw new EntityExistsException("Cet compte existe déjà");
        }
    }
    public List<Utilisateur> lire(){
        List<Utilisateur> utilisateurList = utilisateurRepository.findAll();

        utilisateurList = utilisateurList
                .stream().sorted((d1, d2) -> d2.getEmail().compareTo(d1.getEmail()))
                .collect(Collectors.toList());
        return utilisateurList;
    }

    //Méthode de recupperation d'un seul user

    public  Utilisateur getUtilisateurById(long idUtilisateur){
        Utilisateur utilisateur = utilisateurRepository.findByIdUtilisateur(idUtilisateur);

        if(utilisateur == null){
            throw new EntityNotFoundException("Cet utilisateur n'existe pas");
        }
        return utilisateur;
    }

    //Methode permettant d'éditer un user

    public Utilisateur updateUtilisateur(Utilisateur utilisateur, long id, MultipartFile multipartFile) throws Exception {
        Utilisateur user = utilisateurRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("utilisateur introuvable avec id :" +id));

        user.setNom(utilisateur.getNom());
        user.setPrenom(utilisateur.getPrenom());
        user.setEmail(utilisateur.getEmail());
        user.setPassWord(utilisateur.getPassWord());
        user.setPhone(utilisateur.getPhone());
        user.setRole(utilisateur.getRole());


        if(multipartFile != null){
            String location = "C:\\xampp\\htdocs\\cosit";
            try{
                Path rootlocation = Paths.get(location);
                if(!Files.exists(rootlocation)){
                    Files.createDirectories(rootlocation);
                    Files.copy(multipartFile.getInputStream(),
                            rootlocation.resolve(multipartFile.getOriginalFilename()));
                    user.setImage("cosit/"
                            +multipartFile.getOriginalFilename());
                }else{
                    try {
                        String nom = location+"\\"+multipartFile.getOriginalFilename();
                        Path name = Paths.get(nom);
                        if(!Files.exists(name)){
                            Files.copy(multipartFile.getInputStream(),
                                    rootlocation.resolve(multipartFile.getOriginalFilename()));
                            user.setImage("cosit/"
                                    +multipartFile.getOriginalFilename());
                        }else{
                            Files.delete(name);
                            Files.copy(multipartFile.getInputStream(),rootlocation.resolve(multipartFile.getOriginalFilename()));
                            user.setImage("cosit/"
                                    +multipartFile.getOriginalFilename());
                        }
                    }catch (Exception e){
                        throw new Exception("Impossible de télécharger l\'image");
                    }
                }
            } catch (Exception e){
                throw new Exception(e.getMessage());
            }
        }
        return  utilisateurRepository.save(user);
    }

    //Suppression de l'utilisateur

    public String deleteUserById(long id){
        Utilisateur user = utilisateurRepository.findByIdUtilisateur(id);
        if(user == null){
            throw new EntityNotFoundException("Désolé l'utilisateur à supprimer n'existe pas");
        }
        utilisateurRepository.delete(user);
        return "Utilisateur supprimé avec succèss";
    }

    //connexion d'user
  /*  public Utilisateur connexion(String email, String motDePasse){
        if(utilisateurRepository.findByEmailAndMotDePasse(email,motDePasse) != null){
            return utilisateurRepository.findByEmailAndMotDePasse(email, motDePasse);
        }else{
            throw  new NotFoundException("Ce compte n'existe pas");
        }
        return
    }*/
    public Utilisateur connection(String email, String passWord){
        Utilisateur user = utilisateurRepository.findByEmailAndPassWord(email, passWord);
        if (user == null) {
            throw new EntityNotFoundException("Ce utilisateur n'existe pas");
        }

        return user;
    }
}
