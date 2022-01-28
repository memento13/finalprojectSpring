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

    public PartyRepository_Impl_Maria(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Integer addParty(Party party) {
        Integer uc = 0;

        PreparedStatementSetter pss = new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement stmt) throws SQLException {
                stmt.setString(1, UUID.randomUUID().toString());
                stmt.setString(2, Hangul.hangul(party.getName()));
                stmt.setString(3, Hangul.hangul(party.getLeaderId()));
            }
        };
        // id(uuid), name,leader_id, default,default
        try {
            uc = jdbcTemplate.update("INSERT INTO parties values (?,?,?,default,default)",pss);
        }catch (Exception e){
            uc=0;
        }

        return uc;
    }

    @Override
    public List<Party> findByLeaderId(User leader) {
        List<Party> result = new ArrayList<>();

        RowMapper<Party> rowMapper = new RowMapper<Party>() {
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
        try {
            result = jdbcTemplate.query("select * from parties where leader_id= ?", rowMapper, leader.getId());
        }catch (Exception e){
            //오류는 몰?루
        }

        return result;
    }

}
