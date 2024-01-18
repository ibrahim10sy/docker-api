package gestion.cosit.gestionDepense.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Map;

@Service
public class ProcedureService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

//    public List<Map<String, Object>> getTotalDepenseByAdmin(Long adminId){
//        String query = "CALL GetDepenseParCategorie(?)";
//        return jdbcTemplate.queryForList(query,adminId);
//    }

    public List<Map<String, Object>> getTotalDepenseByUserByJour(Long userId){
        String query = "CALL DepensesParCategorieParJourUtilisateur(?)";
        return jdbcTemplate.queryForList(query,userId);
    }
    public List<Map<String, Object>> getTotalDepenseByUserByMois(Long userId){
        String query = "CALL DepensesParCategorieParMoisUtilisateur(?)";
        return jdbcTemplate.queryForList(query,userId);
    }
    public List<Map<String, Object>> getTotalDepenseByUser(Long userId){
        String query = "CALL GetDepenseTotalByUsers(?)";
        return jdbcTemplate.queryForList(query,userId);
    }

    public List<Map<String, Object>> getTotalDepense(){
        String query = "CALL GetDepenseTotal()";
        return jdbcTemplate.queryForList(query);
    }

    public List<Map<String, Object>> getTotalDepenseByJour(){
        String query = "CALL GetDepenseParJour()";
        return jdbcTemplate.queryForList(query);
    }

    public List<Map<String, Object>> getTotalDepenseByMois(){
        String query = "CALL GetDepenseParMois()";
        return jdbcTemplate.queryForList(query);
    }

    public List<Map<String, Object>> getTotalDepenseBySousCategorie(long id){
        String query = "CALL GetTotalDepenseParSousCategorie(?)";
        return jdbcTemplate.queryForList(query, id);
    }
}
