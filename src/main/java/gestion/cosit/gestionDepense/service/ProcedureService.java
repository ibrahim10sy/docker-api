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

    public List<Map<String, Object>> getTotalDepenseByAdmin(Long adminId){
        String query = "CALL GetDepenseParCategorie(?)";
        return jdbcTemplate.queryForList(query,adminId);
    }
}
