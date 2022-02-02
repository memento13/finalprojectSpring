package repository;

import entity.Party;
import entity.Project;
import entity.User;
import entity.vo.ProjectAndMemberId;
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

    @Override
    public List<ProjectAndMemberId> findProjectAndMemberIdByPartyAndUser(Party party, User user) {
        List<ProjectAndMemberId> result = new ArrayList<>();
        RowMapper<ProjectAndMemberId> rowMapper = new RowMapper<ProjectAndMemberId>() {
            @Override
            public ProjectAndMemberId mapRow(ResultSet resultSet, int i) throws SQLException {
                ProjectAndMemberId vo = new ProjectAndMemberId();
                Project project = new Project();

                project.setId(resultSet.getString("projects_sub.id"));
                project.setName(Hangul.hangul(resultSet.getString("projects_sub.name")));
                project.setParty_id(resultSet.getString("projects_sub.party_id"));
                project.setCreateDate(resultSet.getString("projects_sub.created_date"));
                project.setModifiedDate(resultSet.getString("projects_sub.modified_date"));

                vo.setProject(project);
                vo.setMemberId(resultSet.getString("project_members_sub.user_id"));
                return vo;
            }
        };
        String sql = "select projects_sub.id, projects_sub.name, projects_sub.party_id, projects_sub.created_date, projects_sub.modified_date,  project_members_sub.user_id from (select * from projects where party_id=?) projects_sub left join (select * from project_members where party_id=? and user_id=?) project_members_sub on projects_sub.id = project_members_sub.project_id;";
        try{
            result = jdbcTemplate.query(sql, rowMapper, party.getId(), party.getId(), user.getId());
        }catch (Exception e){
            e.printStackTrace();
            result = new ArrayList<>();
        }
        return result;
    }
}
