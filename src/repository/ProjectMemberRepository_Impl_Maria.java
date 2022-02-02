package repository;

import entity.Party;
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
        // id(uuid), name,leader_id, default,default
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
}
