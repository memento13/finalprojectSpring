package repository;

import entity.Party;
import entity.PartyMember;
import entity.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
        System.out.println("PartyMemberRepository_Impl_Maria.addPartyLeader");

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
            e.printStackTrace();
            uc=0;
        }
        return uc;
    }


    @Override
    public Integer deletePartyMember(PartyMember partyMember) {
        Integer uc = 0;

        PreparedStatementSetter pss = new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement stmt) throws SQLException {
                stmt.setString(1,partyMember.getUserId());
                stmt.setString(2,partyMember.getPartyId());
            }
        };
        try {
            //user_id, party_id
            uc = jdbcTemplate.update("delete from party_members where user_id = ? and party_id = ?",pss);
        }catch (Exception e){
            e.printStackTrace();
            uc=0;
        }

        return uc;
    }


    @Override
    public PartyMember findPartyMemberByPartyUser(Party party, User user) {
        PartyMember result = null;

        String sql  = "select * from party_members where party_id = ? and user_id = ?";

        try {
            List<PartyMember> query = jdbcTemplate.query(sql, new RowMapper<PartyMember>() {
                        @Override
                        public PartyMember mapRow(ResultSet resultSet, int i) throws SQLException {
                            PartyMember vo = new PartyMember();
                            vo.setPartyId(resultSet.getString("party_id"));
                            vo.setUserId(resultSet.getString("user_id"));
                            vo.setGrade(resultSet.getInt("grade"));
                            vo.setCreateDate(resultSet.getString("created_date"));
                            vo.setModifiedDate(resultSet.getString("modified_date"));
                            return vo;
                        }
                    }
                    , party.getId(), user.getId());
            if(query.size()==1){
                result = query.get(0);
            }
        }catch (Exception e){
            e.printStackTrace();
            result = null;
        }
        return result;
    }

    @Override
    public List<Party> findPartiesByLeaderUser(User user) {

        List<Party> result = new ArrayList<>();
        String sql = "select parties.id, parties.name, parties.leader_id, parties.created_date, parties.modified_date from parties join party_members pm on parties.id = pm.party_id where user_id= ? and pm.grade=50";
        RowMapper<Party> rowMapper = new RowMapper<Party>() {
            @Override
            public Party mapRow(ResultSet resultSet, int i) throws SQLException {
                Party vo = new Party();
                vo.setId(resultSet.getString("parties.id"));
                vo.setName(resultSet.getString("parties.name"));
                vo.setLeaderId(resultSet.getString("parties.leader_id"));
                vo.setCreateDate(resultSet.getString("parties.created_date"));
                vo.setModifiedDate(resultSet.getString("parties.modified_date"));
                return vo;
            }
        };
        try {
            result = jdbcTemplate.query(sql, rowMapper, user.getId());
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public List<Party> findPartiesByMemberUser(User user) {
        List<Party> result = new ArrayList<>();
        String sql = "select parties.id, parties.name, parties.leader_id, parties.created_date, parties.modified_date from parties join party_members pm on parties.id = pm.party_id where user_id= ? and pm.grade=1";
        RowMapper<Party> rowMapper = new RowMapper<Party>() {
            @Override
            public Party mapRow(ResultSet resultSet, int i) throws SQLException {
                Party vo = new Party();
                vo.setId(resultSet.getString("parties.id"));
                vo.setName(resultSet.getString("parties.name"));
                vo.setLeaderId(resultSet.getString("parties.leader_id"));
                vo.setCreateDate(resultSet.getString("parties.created_date"));
                vo.setModifiedDate(resultSet.getString("parties.modified_date"));
                return vo;
            }
        };
        try {
            result = jdbcTemplate.query(sql, rowMapper, user.getId());
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public Integer checkUserJoinedParty(Party party, User user) {
        Integer result = 0;

        String sql = "select count(*) from party_members where party_id = ? and user_id = ?";
        result = jdbcTemplate.queryForObject(sql, Integer.class,party.getId(),user.getId());

        return result;
    }

    @Override
    public List<Party> findPartiesByUser(User user) {
        return null;
    }

    @Override
    public List<User> findUsersByParty(Party party) {
        return null;
    }
}
