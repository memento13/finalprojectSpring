package repository;

import entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

@Repository("userRepository")
public class UserRepository_Impl_Maria implements UserRepository {

    private JdbcTemplate jtpl;

    @Override
    public Integer addUser(User user) {

        Integer uc = 0;

        PreparedStatementSetter pss = new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement stmt) throws SQLException {
                stmt.setString(1, UUID.randomUUID().toString());
                stmt.setString(2, Hangul.hangul(user.getEmail()));
                stmt.setString(3, Hangul.hangul(user.getPassword()));
                stmt.setString(4, Hangul.hangul(user.getName()));
            }
        };
        // id(uuid), email,password, name, default,default
        try {
            uc = jtpl.update("INSERT INTO users values (?,?,?,?,default,default)",pss);
        }catch (Exception e){
            uc=0;
        }

        return uc;
    }

    @Override
    public User findUserByEmailAndPassword(User user) {
        Integer uc = 0;

        User result = null;

        RowMapper<User> rowMapper = new RowMapper<User>() {
            @Override
            public User mapRow(ResultSet resultSet, int i) throws SQLException {
                User vo = new User();
                vo.setId(resultSet.getString("id"));
                vo.setEmail(resultSet.getString("email"));
                vo.setPassword(resultSet.getString("password"));
                vo.setName(resultSet.getString("name"));
                vo.setCreatedDate(resultSet.getString("created_date"));
                vo.setModifiedDate(resultSet.getString("modified_date"));

                return vo;
            }
        };
        List<User> userList = null;
        try {
            userList = jtpl.query("select * from users where email= ? and password= ?", rowMapper, user.getEmail(), user.getPassword());
            uc = userList.size();
        }catch (Exception e){
            uc=0;
        }
        if(uc==1){
            result = userList.get(0);
        }
        return result;
    }

    @Autowired
    public UserRepository_Impl_Maria(JdbcTemplate jdbcTemplate) {
        this.jtpl = jdbcTemplate;
    }
}
