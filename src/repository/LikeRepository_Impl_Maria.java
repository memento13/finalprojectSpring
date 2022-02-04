package repository;

import entity.Like;
import entity.Post;
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

@Repository("likeRepository")
public class LikeRepository_Impl_Maria implements LikeRepository{

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Like> likeRowMapper;

    public LikeRepository_Impl_Maria(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.likeRowMapper = new RowMapper<Like>() {
            @Override
            public Like mapRow(ResultSet resultSet, int i) throws SQLException {
                Like vo = new Like();
                vo.setUserId(resultSet.getString("user_id"));
                vo.setPostId(resultSet.getString("post_id"));
                vo.setCreateDate(resultSet.getString("created_date"));
                vo.setModifiedDate(resultSet.getString("modified_date"));
                return vo;
            }
        };
    }


    @Override
    public Integer addLike(Like like) {
        Integer result = 0;

        PreparedStatementSetter pss = new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement stmt) throws SQLException {
                stmt.setString(1,like.getPostId());
                stmt.setString(2,like.getUserId());
            }
        };
        //post_id,user_id,created_date,modified_date
        try {
            result = jdbcTemplate.update("insert into likes values (?,?,default ,default )", pss);
        }catch (Exception e){
            result = 0;
        }
        return result;
    }

    @Override
    public Like findByPostAndUser(Post post, User user) {
        Like result = null;

        String sql = "select * from likes where post_id = ? and user_id =?";
        try {
            List<Like> query = jdbcTemplate.query(sql, likeRowMapper, post.getId(), user.getId());
            if(query.size()==1){
                result = query.get(0);
            }
        }catch (Exception e){
            result = null;
        }
        return result;
    }

    @Override
    public List<Like> findLikesByPost(Post post) {

        List<Like> result = new ArrayList<>();
        String sql = "select * from likes where post_id = ?";
        try {
            result = jdbcTemplate.query(sql, likeRowMapper, post.getId());
        }catch (Exception e){
            result = new ArrayList<>();
        }
        return result;
    }

    @Override
    public Integer deleteLikesByPost(Post post) {
        PreparedStatementSetter pss = new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement stmt) throws SQLException {
                stmt.setString(1,post.getId());
            }
        };
        String sql = "delete from likes where post_id = ?";
        int update = jdbcTemplate.update(sql, pss);
        return update;
    }
}
