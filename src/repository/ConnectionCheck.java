package repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository("connectionCheck")
public class ConnectionCheck {

    private JdbcTemplate jtpl;

    @Autowired
    public ConnectionCheck(JdbcTemplate jtpl) {
        this.jtpl = jtpl;
    }

    public String dbConnectionCheck(){
        String time = jtpl.queryForObject("select now()", String.class);
        String msg = jtpl.toString()+" : "+ time;
        return msg;
    }


}
