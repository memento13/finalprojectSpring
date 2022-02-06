package repository;

import entity.PartyMember;
import entity.Project;
import entity.ProjectMember;
import entity.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository("projectMemberRepository")
public class ProjectMemberRepository_Impl_Maria implements ProjectMemberRepository{

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<ProjectMember> projectMemberRowMapper;

    public ProjectMemberRepository_Impl_Maria(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.projectMemberRowMapper = new RowMapper<ProjectMember>() {
            @Override
            public ProjectMember mapRow(ResultSet resultSet, int i) throws SQLException {
                ProjectMember vo = new ProjectMember();
                vo.setPartyId(resultSet.getString("party_id"));
                vo.setProjectId(resultSet.getString("project_id"));
                vo.setUserId(resultSet.getString("user_id"));
                vo.setCreateDate(resultSet.getString("created_date"));
                vo.setModifiedDate(resultSet.getString("modified_date"));
                return vo;
            }
        };
    }

    @Override
    public ProjectMember addProjectMember(ProjectMember projectMember) {

        ProjectMember result = null;

        Integer uc = 0;
        PreparedStatementSetter pss = new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement stmt) throws SQLException {
                stmt.setString(1, projectMember.getPartyId());
                stmt.setString(2, projectMember.getProjectId());
                stmt.setString(3, projectMember.getUserId());
            }
        };
        // party_id, project_id,user_id, default,default
        try {
            uc = jdbcTemplate.update("INSERT INTO project_members values (?,?,?,default,default)",pss);
            if(uc==1){
                result = projectMember;
            }
        }catch (Exception e){
            e.printStackTrace();
            result = null;
        }

        return result;
    }

    @Override
    public Integer deleteProjectMember(ProjectMember projectMember) {
        Integer uc = 0;
        PreparedStatementSetter pss = new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement stmt) throws SQLException {
                stmt.setString(1, projectMember.getUserId());
                stmt.setString(2, projectMember.getProjectId());
            }
        };
        // party_id, project_id,user_id
        try {
            uc = jdbcTemplate.update("delete from project_members where user_id = ? and project_id = ?",pss);
        }catch (Exception e){
            e.printStackTrace();
            uc = 0;
        }
        return uc;
    }

    @Override
    public Integer deleteProjectMembersByPartyMember(PartyMember partyMember) {
        Integer uc = 0;
        PreparedStatementSetter pss = new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement stmt) throws SQLException {
                stmt.setString(1, partyMember.getUserId());
                stmt.setString(2, partyMember.getPartyId());
            }
        };
        // party_id, user_id
        try {
            uc = jdbcTemplate.update("delete from project_members where user_id = ? and party_id = ?",pss);
        }catch (Exception e){
            e.printStackTrace();
            uc = 0;
        }
        return uc;
    }

    @Override
    public Integer deleteProjectMembersByProject(Project project) {
        Integer uc = 0;
        PreparedStatementSetter pss = new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement stmt) throws SQLException {
                stmt.setString(1, project.getId());
            }
        };
        // party_id, user_id
        try {
            uc = jdbcTemplate.update("delete from project_members where project_id = ?",pss);
        }catch (Exception e){
            e.printStackTrace();
            uc = 0;
        }
        return uc;
    }

    @Override
    public ProjectMember findByProjectAndUser(Project project, User user) {
        ProjectMember result = null;
        String sql = "select * from project_members where project_id = ? and user_id = ?";
        try {
            List<ProjectMember> query = jdbcTemplate.query(sql, projectMemberRowMapper, project.getId(), user.getId());
            if(query.size()==1){
                result = query.get(0);
            }
        }catch (Exception e){
            result = null;
        }
        return result;
    }


}
