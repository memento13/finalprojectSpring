package repository;

import entity.Party;
import entity.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Repository("partyMemberRepository")
public class PartyMemberRepository_Impl_Maria implements PartyMemberRepository {

    private final JdbcTemplate jdbcTemplate;

    public PartyMemberRepository_Impl_Maria(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public Integer addPartyMember(User user, Party party) {

        Integer uc = 0;
        PreparedStatementSetter pss = new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement stmt) throws SQLException {
                stmt.setString(1,party.getId());
                stmt.setString(2,user.getId());
            }
        };

        try {
            // party_id,user_id,grade,created_date,modified_date;
            uc = jdbcTemplate.update("INSERT INTO party_members values (?,?,1,default,default)",pss);
        }catch (Exception e){
            uc=0;
        }
        return uc;
    }

    @Override
    public Integer addPartyLeader(User user, Party party) {

        Integer uc = 0;
        PreparedStatementSetter pss = new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement stmt) throws SQLException {
                stmt.setString(1,party.getId());
                stmt.setString(2,user.getId());
            }
        };

        try {
            // party_id,user_id,grade,created_date,modified_date;
            uc = jdbcTemplate.update("INSERT INTO party_members values (?,?,50,default,default)",pss);
        }catch (Exception e){
            uc=0;
        }
        return uc;
    }

    @Override
    public List<Party> findPartiesByLeaderUser(User user) {
        return null;
    }

    @Override
    public List<Party> findPartiesByMemberUser(User user) {
        return null;
    }

    @Override
    public List<Party> findPartiesByUser(User user) {
        return null;
    }

    @Override
    public List<User> findUserByParty(Party party) {
        return null;
    }
}
