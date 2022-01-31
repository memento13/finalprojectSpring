package repository;

import entity.Party;
import entity.Project;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository("projectRepository")
public class ProjectRepository_Impl_Maria implements ProjectRepository{

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Project> projectRowMapper;

    public ProjectRepository_Impl_Maria(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.projectRowMapper =  new RowMapper<Project>() {
            @Override
            public Project mapRow(ResultSet resultSet, int i) throws SQLException {
                Project vo = new Project();
                vo.setId(resultSet.getString("id"));
                vo.setName(resultSet.getString("name"));
                vo.setParty_id(resultSet.getString("party_id"));
                vo.setCreateDate(resultSet.getString("created_date"));
                vo.setModifiedDate(resultSet.getString("modified_date"));
                return vo;
            }
        };
    }

    @Override
    public Project addProject(Project project) {
        Project result = new Project();
        Integer uc = 0;
        String uuid = UUID.randomUUID().toString();

        PreparedStatementSetter pss = new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement stmt) throws SQLException {
                stmt.setString(1,uuid);
                stmt.setString(2,Hangul.hangul(project.getName()));
                stmt.setString(3,project.getParty_id());
            }
        };

        // id, name,party_id, created_date,modified_date
        try {
            uc = jdbcTemplate.update("INSERT INTO projects values (?,?,?,default,default)",pss);
            if(uc==1){
                result.setId(uuid);
                result.setName(project.getName());
                result.setParty_id(project.getParty_id());
            }
        }catch (Exception e){
            e.printStackTrace();
            result = null;
        }

        return result;
    }

    @Override
    public List<Project> findProjectsByParty(Party party) {
        List<Project> result = new ArrayList<>();
        try{
            result = jdbcTemplate.query("select * from projects where party_id = ?", projectRowMapper, party.getId());

        }catch (Exception e){
            result = new ArrayList<>();
            //오류는 몰?루
        }

        return result;
    }

    @Override
    public Project findProjectByPartyAndName(Party party, String projectName) {
        Project project = null;
        try{
            List<Project> query = jdbcTemplate.query("select * from projects where party_id = ? and name = ?", projectRowMapper, party.getId(), projectName);
            if(query.size()==1){
                project = query.get(0);
            }
        }catch (Exception e){
            project = null;
        }
        return project;
    }


}
