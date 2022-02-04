package repository;

import entity.Post;
import entity.Project;
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

@Repository("postRepository")
public class PostRepository_Impl_Maria implements PostRepository {

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Post> postRowMapper;

    public PostRepository_Impl_Maria(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;

        this.postRowMapper = new RowMapper<Post>() {
            @Override
            public Post mapRow(ResultSet resultSet, int i) throws SQLException {
                Post vo = new Post();

                vo.setId(resultSet.getString("posts.id"));
//                vo.setTitle(Hangul.hangul(resultSet.getString("posts.title")));
                vo.setTitle(resultSet.getString("posts.title"));
//                vo.setContent(Hangul.hangul(resultSet.getString("posts.content")));
                vo.setContent(resultSet.getString("posts.content"));
                User user = new User();
                user.setId(resultSet.getString("posts.user_id"));
                user.setName(resultSet.getString("users.name"));
                vo.setUser(user);
                vo.setProjectId(resultSet.getString("posts.project_id"));
                vo.setPartyId(resultSet.getString("posts.party_id"));
                vo.setCreateDate(resultSet.getString("created_date"));
                vo.setModifiedDate(resultSet.getString("modified_date"));

                return vo;
            }
        };
    }


    @Override
    public Post addPost(Post post) {

        Post result = null;

        Integer uc = 0;
        String uuid = UUID.randomUUID().toString();
        PreparedStatementSetter pss = new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement stmt) throws SQLException {
                stmt.setString(1,uuid);
                stmt.setString(2,post.getTitle());
                stmt.setString(3,post.getContent());
                stmt.setString(4,post.getPartyId());
                stmt.setString(5,post.getProjectId());
                stmt.setString(6,post.getUserId());
            }
        };

        //id(uuid),title,content,party_id,project_id,user_id,default,default
        String sql = "insert into posts values (?,?,?,?,?,?,default ,default)";
        try {
            uc = jdbcTemplate.update(sql, pss);
            if(uc ==1){
                result = post;
                result.setId(uuid);
            }
        }catch (Exception e){
            e.printStackTrace();
            result = null;
        }
        return result;
    }

    @Override
    public List<Post> findPostsByProject(Project project) {
        List<Post> result = new ArrayList<>();

        String sql = "select posts.id, posts.title, posts.content, posts.party_id, posts.project_id, posts.user_id, posts.created_date, posts.modified_date," +
                "users.name from posts left join users on posts.user_id = users.id where project_id=? order by created_date desc";

        try {
            result = jdbcTemplate.query(sql, postRowMapper, project.getId());
        }catch (Exception e){
            e.printStackTrace();
            result = new ArrayList<>();
        }
        return result;
    }

    @Override
    public Post findById(String postId) {
        Post result = null;
        List<Post> query = new ArrayList<>();

        String sql = "select posts.id, posts.title, posts.content, posts.party_id, posts.project_id, posts.user_id, posts.created_date, posts.modified_date," +
                "users.name from posts left join users on posts.user_id = users.id where posts.id = ?";

        try {
            query = jdbcTemplate.query(sql, postRowMapper, postId);
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
    public Integer deletePost(Post post) {

        PreparedStatementSetter pss = new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement stmt) throws SQLException {
                stmt.setString(1,post.getId());
            }
        };

        String sql = "delete from posts where id = ?";
        int update = jdbcTemplate.update(sql, pss);
        return update;
    }
}
