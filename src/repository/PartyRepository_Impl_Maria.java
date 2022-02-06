package repository;

import entity.Party;
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
import java.util.UUID;

@Repository("partyRepository")
public class PartyRepository_Impl_Maria implements PartyRepository{

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Party> partyRowMapper;

    public PartyRepository_Impl_Maria(JdbcTemplate jdbcTemplate) {

        this.jdbcTemplate = jdbcTemplate;
        this.partyRowMapper =  new RowMapper<Party>() {
            @Override
            public Party mapRow(ResultSet resultSet, int i) throws SQLException {
                Party vo = new Party();
                vo.setId(resultSet.getString("id"));
                vo.setName(resultSet.getString("name"));
                vo.setLeaderId(resultSet.getString("leader_id"));
                vo.setCreateDate(resultSet.getString("created_date"));
                vo.setModifiedDate(resultSet.getString("modified_date"));
                return vo;
            }
        };
    }

    @Override
    public Party addParty(Party party) {
        System.out.println("PartyRepository_Impl_Maria.addParty");
        Party result = new Party();
        Integer uc = 0;
        String uuid = UUID.randomUUID().toString();

        PreparedStatementSetter pss = new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement stmt) throws SQLException {
                stmt.setString(1, uuid);
                stmt.setString(2, Hangul.hangul(party.getName()));
                stmt.setString(3, Hangul.hangul(party.getLeaderId()));
            }
        };
        // id(uuid), name,leader_id, default,default
        try {
            uc = jdbcTemplate.update("INSERT INTO parties values (?,?,?,default,default)",pss);
            if(uc==1){
                result.setId(uuid);
                result.setName(party.getName());
                result.setLeaderId(party.getLeaderId());
            }
        }catch (Exception e){
            e.printStackTrace();
            result = null;
        }

        return result;
    }

    @Override
    public Integer deleteParty(Party party) {
        String sql = "delete from parties where id = ?";
        Integer uc = 0;
        try{
            uc = jdbcTemplate.update(sql, new PreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement stmt) throws SQLException {
                    stmt.setString(1,party.getId());
                }
            });
        }catch (Exception e){
            uc = 0;
        }
        return uc;
    }

    @Override
    public List<Party> findByLeaderId(User leader) {
        List<Party> result = new ArrayList<>();

        try {
            result = jdbcTemplate.query("select * from parties where leader_id= ?", partyRowMapper, leader.getId());
        }catch (Exception e){
            //오류는 몰?루
        }

        return result;
    }

    @Override
    public Party findByName(String partyName) {
        Party result = null;

        List<Party> partyList = new ArrayList<>();

        try {
            partyList = jdbcTemplate.query("select * from parties where name= ?", partyRowMapper, partyName);
        }catch (Exception e){
            e.printStackTrace();
        }
        if(partyList.size()==1){
            result = partyList.get(0);
        }

        return result;
    }

    @Override
    public List<Party> findDidNotJoinPartiesByUser(User user) {
        List<Party> result = new ArrayList<>();
        String sql = "select id,name,leader_id,parties.created_date,parties.modified_date from parties left join (select * from party_members where user_id= ? ) user_joined on parties.id = user_joined.party_id where user_id is null";
        try {
            result = jdbcTemplate.query(sql, partyRowMapper, user.getId());
        }catch (Exception e){
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public List<Party> findDidNotJoinPartiesByUserAndKeyword(User user, String keyword) {
        List<Party> result = new ArrayList<>();
        String sqlKeyword = "%"+keyword+"%";
        String sql = "select id,name,leader_id,parties.created_date,parties.modified_date from parties left join (select * from party_members where user_id= ? ) user_joined on parties.id = user_joined.party_id where user_id is null and name like ?";

        try {
            result = jdbcTemplate.query(sql, partyRowMapper, user.getId(),sqlKeyword);
        }catch (Exception e){
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public Party findById(String partyId) {
        Party result = null;
        try {
            List<Party> query = jdbcTemplate.query("select * from parties where id = ?", partyRowMapper, partyId);
            if(query.size()==1){
                result = query.get(0);
            }
        }catch (Exception e){
            result = null;
        }

        return result;
    }
}
