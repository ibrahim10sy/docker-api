package gestion.cosit.gestionDepense.service;

import gestion.cosit.gestionDepense.Exception.NoContentException;
import gestion.cosit.gestionDepense.config.SecurityConfig;
import gestion.cosit.gestionDepense.model.CategorieDepense;
import gestion.cosit.gestionDepense.model.SendNotification;
import gestion.cosit.gestionDepense.model.Utilisateur;
import gestion.cosit.gestionDepense.repository.UtilisateurRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class UtilisateurService {

    @Autowired
    private UtilisateurRepository utilisateurRepository;
    @Autowired
    BCryptPasswordEncoder passwordEncoder;
    @Autowired
    FileService fileService;


//    public Utilisateur createUser(Utilisateur utilisateur, MultipartFile multipartFileImage) throws Exception {
//        if(utilisateurRepository.findByEmail(utilisateur.getEmail()) == null){
//
//            //On hashe le mot de passe
//            String passWordHasher = passwordEncoder.encode(utilisateur.getPassWord());
//            utilisateur.setPassWord(passWordHasher);
//            if (multipartFileImage != null) {
//                String location = "C:\\xampp\\htdocs\\cosit";
//                System.out.println("verif");
//                try {
//                    Path rootlocation = Paths.get(location);
//                    if (!Files.exists(rootlocation)) {
//                        Files.createDirectories(rootlocation);
//                        Files.copy(multipartFileImage.getInputStream(),
//                                rootlocation.resolve(multipartFileImage.getOriginalFilename()));
//                        utilisateur.setImage("cosit/"
//                                + multipartFileImage.getOriginalFilename());
//                    } else {
//                        System.out.println("Autre condition");
//                        try {
//                            String nom = location + "\\" + multipartFileImage.getOriginalFilename();
//                            Path name = Paths.get(nom);
//                            if (!Files.exists(name)) {
//                                Files.copy(multipartFileImage.getInputStream(),
//                                        rootlocation.resolve(multipartFileImage.getOriginalFilename()));
//                                utilisateur.setImage("cosit/"
//                                        + multipartFileImage.getOriginalFilename());
//                            } else {
//                                Files.delete(name);
//                                Files.copy(multipartFileImage.getInputStream(), rootlocation.resolve(multipartFileImage.getOriginalFilename()));
//                                utilisateur.setImage("cosit/"
//                                        + multipartFileImage.getOriginalFilename());
//                            }
//                        } catch (Exception e) {
//                            throw new Exception("Impossible de télécharger l\'image");
//                        }
//                    }
//                } catch (Exception e) {
//                    throw new Exception(e.getMessage());
//                }
//            }
//            System.out.println("user service"+utilisateur);
//            return  utilisateurRepository.save(utilisateur);
//        }else{
//            throw new EntityExistsException("Cet compte existe déjà");
//        }
//    }
//    private static final String UPLOAD_DIRECTORY = "src/main/resources/images";
@Async("asyncExecutor")
public CompletableFuture<String> uploaderImageAsync(Utilisateur utilisateur, MultipartFile multipartFileImage) {
    try {
        // Générer un nom de fichier unique
        String fileName = UUID.randomUUID().toString() + fileService.getExtension(multipartFileImage.getOriginalFilename());

        String imageUploadDirectory = "uploads"; // Chemin relatif dans le répertoire du projet
        Path imageRootLocation = Paths.get(imageUploadDirectory);

        // Enregistrer l'image dans Firebase Storage et obtenir l'URL de téléchargement
        ResponseEntity<String> uploadResponse = fileService.upload(multipartFileImage, fileName);

        // Utiliser directement l'URL obtenue du téléversement dans Firebase Storage
        String imageUrl = uploadResponse.getBody();

        // Stocker l'URL de l'image dans l'objet Utilisateur
        utilisateur.setImage(imageUrl);

        return CompletableFuture.completedFuture(imageUrl);
    } catch (Exception e) {
        // Gérer l'exception, par exemple en journalisant l'erreur
        e.printStackTrace();
        return CompletableFuture.failedFuture(e);
    }
}

    public Utilisateur createUser(Utilisateur utilisateur, MultipartFile multipartFileImage) throws Exception {
        if (utilisateurRepository.findByEmail(utilisateur.getEmail()) == null) {
            // On hashe le mot de passe
            String passWordHasher = passwordEncoder.encode(utilisateur.getPassWord());
            utilisateur.setPassWord(passWordHasher);

            if (multipartFileImage != null) {
                // Appeler la méthode uploaderImage de manière asynchrone
                CompletableFuture<String> uploadTask = uploaderImageAsync(utilisateur, multipartFileImage);

                // Attendre la fin de la tâche asynchrone avant de continuer
                uploadTask.join();
            }

            System.out.println("user service" + utilisateur);
            return utilisateurRepository.save(utilisateur);
        } else {
            throw new EntityExistsException("Ce compte existe déjà");
        }
    }

//    public Utilisateur createUser(Utilisateur utilisateur, MultipartFile multipartFileImage) throws Exception {
//        if (utilisateurRepository.findByEmail(utilisateur.getEmail()) == null) {
//
//            // On hashe le mot de passe
//            String passWordHasher = passwordEncoder.encode(utilisateur.getPassWord());
//            utilisateur.setPassWord(passWordHasher);
//
//            if (multipartFileImage != null) {
//                try {
//                    // Configuration du client S3
//                    S3Client s3Client = S3Client.builder()
//                            .region(Region.EU_NORTH_1)  // Utilisation de la région eu-north-1
//                            .credentialsProvider(EnvironmentVariableCredentialsProvider.create())
//                            .build();
//
//                    // Nom du compartiment S3
//                    String bucketName = "cositgestion";
//
//                    // Chemin dans le compartiment S3
//                    String s3Key = "images/" + multipartFileImage.getOriginalFilename();
//
//                    // Téléchargement de l'image vers Amazon S3
//                    PutObjectRequest putObjectRequest = PutObjectRequest.builder()
//                            .bucket(bucketName)
//                            .key(s3Key)
//                            .build();
//
//                    InputStream inputStream = multipartFileImage.getInputStream();
//                    s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(inputStream, multipartFileImage.getSize()));
////                    https://cositgestion.s3.eu-north-1.amazonaws.com/IMG_3329.JPG
//                    // Enregistrement du chemin de l'image dans la base de données
//                    utilisateur.setImage("https://" + bucketName + ".s3." + Region.EU_NORTH_1.id() + ".amazonaws.com/" + s3Key);
//                } catch (Exception e) {
//                    throw new Exception("Impossible de télécharger l'image vers Amazon S3", e);
//                }
//            }
//
//            // Enregistrement de l'utilisateur dans la base de données
//            return utilisateurRepository.save(utilisateur);
//
//        } else {
//            throw new EntityExistsException("Cet compte existe déjà");
//        }
//    }
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

    public Utilisateur updateUtilisateur(Utilisateur utilisateur, long id, MultipartFile multipartFileImage) throws Exception {
        Utilisateur user = utilisateurRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("utilisateur introuvable avec id :" +id));

        //On hashe le mot de passe
//        String passWordHasher = passwordEncoder.encode(user.getPassWord());


        user.setNom(utilisateur.getNom());
        user.setPrenom(utilisateur.getPrenom());
        user.setEmail(utilisateur.getEmail());
        user.setPassWord(utilisateur.getPassWord());
        user.setPhone(utilisateur.getPhone());
        user.setRole(utilisateur.getRole());

        // Mettez à jour le mot de passe si un nouveau mot de passe est fourni
        if (utilisateur.getPassWord() != null && !utilisateur.getPassWord().isEmpty()) {
            String hashedPassword = passwordEncoder.encode(utilisateur.getPassWord());
            user.setPassWord(hashedPassword);
        }

        if (multipartFileImage != null) {
            // Appeler la méthode uploaderImage de manière asynchrone
            CompletableFuture<String> uploadTask = uploaderImageAsync(utilisateur, multipartFileImage);

            // Attendre la fin de la tâche asynchrone avant de continuer
            uploadTask.join();
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
//    public Utilisateur connection(String email, String passWord){
//        String passWordHasher = passwordEncoder.encode(passWord);
//        Utilisateur user = utilisateurRepository.findByEmailAndPassWord(email, passWordHasher);
//        if (user == null) {
//            throw new EntityNotFoundException("Ce utilisateur n'existe pas");
//        }
//
//        return user;
//    }
    public Utilisateur connection(String email, String passWord) {
        Utilisateur user = utilisateurRepository.findByEmail(email);

        if (user == null || !passwordEncoder.matches(passWord, user.getPassWord())) {
            throw new EntityNotFoundException("Combinaison e-mail/mot de passe incorrecte");
        }

        return user;
    }
    public Utilisateur reinitialiserMotDePasse(String email, String nouveauMotDePasse) {
        Utilisateur utilisateur = utilisateurRepository.findByEmail(email);

        if (utilisateur != null) {
            // Mettre à jour le mot de passe dans la base de données
            utilisateur.setPassWord(passwordEncoder.encode(nouveauMotDePasse));

        }
        return utilisateurRepository.save(utilisateur);
    }
}
