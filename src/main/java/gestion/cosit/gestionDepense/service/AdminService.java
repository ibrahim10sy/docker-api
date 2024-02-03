package gestion.cosit.gestionDepense.service;

import gestion.cosit.gestionDepense.Exception.NoContentException;
import gestion.cosit.gestionDepense.model.Admin;
import gestion.cosit.gestionDepense.model.Utilisateur;
import gestion.cosit.gestionDepense.repository.AdminRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.webjars.NotFoundException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
public class AdminService {

    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    BCryptPasswordEncoder passwordEncoder;
    @Autowired
    FileService fileService;
    //création de admin

    private static final String UPLOAD_DIRECTORY = "src/main/resources/images";
    @Async("asyncExecutor")
    public CompletableFuture<String> uploaderImageAsync(Admin admin, MultipartFile multipartFileImage) {
        try {
            // Générer un nom de fichier unique
            String fileName = UUID.randomUUID().toString() + fileService.getExtension(multipartFileImage.getOriginalFilename());

            Path uploadPath = Paths.get(UPLOAD_DIRECTORY);
            Path filePath = uploadPath.resolve(fileName);

            // Enregistrer l'image dans Firebase Storage et obtenir l'URL de téléchargement
            ResponseEntity<String> uploadResponse = fileService.upload(multipartFileImage, fileName);

            // Utiliser directement l'URL obtenue du téléversement dans Firebase Storage
            String imageUrl = uploadResponse.getBody();

            // Stocker l'URL de l'image dans l'objet Utilisateur
            admin.setImage(imageUrl);

            return CompletableFuture.completedFuture(imageUrl);
        } catch (Exception e) {
            // Gérer l'exception, par exemple en journalisant l'erreur
            e.printStackTrace();
            return CompletableFuture.failedFuture(e);
        }
    }
    public Admin createAdmin(Admin admin, MultipartFile multipartFileImage) throws Exception {
        if(adminRepository.findByEmail(admin.getEmail()) == null){

            String passWordHasher = passwordEncoder.encode(admin.getPassWord());
            admin.setPassWord(passWordHasher);
            if (multipartFileImage != null) {
                // Appeler la méthode uploaderImage de manière asynchrone
                CompletableFuture<String> uploadTask = uploaderImageAsync(admin, multipartFileImage);

                // Attendre la fin de la tâche asynchrone avant de continuer
                uploadTask.join();
            }
            System.out.println(admin.toString());
            return  adminRepository.save(admin);
        }else{
            throw new EntityExistsException("Cet compte existe déjà");
        }
    }

    public List<Admin> getAllAdmin(){
        List<Admin> admins = adminRepository.findAll();
        if(admins.isEmpty()){
            throw new NoContentException("Aucun donnée trouvé");
        }
        return admins;
    }

    public Admin getAdminById(long id){
        Admin admin = adminRepository.findByIdAdmin(id);
        if(admin == null){
            throw new EntityNotFoundException("Aucun donné trouvé avec l'ID :"+id);
        }
        return admin;
    }

    public Admin updateAdmin(Admin admin, long id, MultipartFile multipartFileImage) throws Exception {
        Admin admin1 = adminRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("utilisateur non trouvé"));

        admin1.setNom(admin.getNom());
        admin1.setPrenom(admin.getPrenom());
        admin1.setEmail(admin.getEmail());
        admin1.setPassWord(admin.getPassWord());
        // Mettez à jour le mot de passe si un nouveau mot de passe est fourni
        if (admin.getPassWord() != null && !admin.getPassWord().isEmpty()) {
            String hashedPassword = passwordEncoder.encode(admin.getPassWord());
            admin1.setPassWord(hashedPassword);
        }
        if (multipartFileImage != null) {
            // Appeler la méthode uploaderImage de manière asynchrone
            CompletableFuture<String> uploadTask = uploaderImageAsync(admin, multipartFileImage);

            // Attendre la fin de la tâche asynchrone avant de continuer
            uploadTask.join();
        }
        return adminRepository.save(admin1);
    }

    public  String deleteAdmin(long id){
        Admin admin1 = adminRepository.findById(id).orElseThrow(()-> new EntityNotFoundException("Admin non trouvé avec l'ID :"+id));
        if(admin1 == null)
            throw new EntityNotFoundException("Ce compte n'existe pas");
        adminRepository.delete(admin1);
        return "Compte supprimé avec succèss";
    }

    public Admin connexion(String email, String passWord){
        if(adminRepository.findByEmailAndAndPassWord(email,passWord) != null){
            return adminRepository.findByEmailAndAndPassWord(email, passWord);
        }else{
            throw  new NotFoundException("Ce compte n'existe pas");
        }
    }

    public Admin connection(String email, String passWord){
         Admin admin= adminRepository.findByEmail(email);
        if (admin == null || !passwordEncoder.matches(passWord, admin.getPassWord())) {
            throw new EntityNotFoundException("Ce compte n'existe pas");
        }

        return admin;
    }

//    public Utilisateur connection(String email, String passWord) {
//        Utilisateur user = utilisateurRepository.findByEmail(email);
//
//        if (user == null || !passwordEncoder.matches(passWord, user.getPassWord())) {
//            throw new EntityNotFoundException("Combinaison e-mail/mot de passe incorrecte");
//        }
//
//        return user;
//    }
}
