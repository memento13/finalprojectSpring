package repository;

import entity.Comment;
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
import java.util.UUID;

@Repository("commentRepository")
public class CommentRepository_Impl_Maria implements CommentRepository {

    private final JdbcTemplate jdbcTemplate;

    public CommentRepository_Impl_Maria(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Integer addComment(Comment comment) {
        Integer uc = 0;
        PreparedStatementSetter pss;
        String sql;
        String uuid = UUID.randomUUID().toString();
        if(comment.getParentComment()!=null){
            pss = new PreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement stmt) throws SQLException {
                    stmt.setString(1,uuid);
                    stmt.setString(2,comment.getContent());
                    stmt.setString(3,comment.getUserId());
                    stmt.setString(4,comment.getPostId());
                    stmt.setString(5,comment.getParentCommentId());
                }
            };
            //uuid,content,user_id,post_id,parent_comment_id,default,default
            sql = "insert into comments values (?,?,?,?,?,default,default)";
        }else{
            pss = new PreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement stmt) throws SQLException {
                    stmt.setString(1,uuid);
                    stmt.setString(2,comment.getContent());
                    stmt.setString(3,comment.getUserId());
                    stmt.setString(4,comment.getPostId());
                }
            };
            //uuid,content,user_id,post_id,null,default,default
            sql = "insert into comments values (?,?,?,?,null,default,default)";
        }

        try {
            uc = jdbcTemplate.update(sql, pss);
        }catch (Exception e){
            uc = 0;
        }
        return uc;
    }

    @Override
    public List<Comment> findCommentByPost(Post post) {

        List<Comment> result = new ArrayList<>();
        RowMapper<Comment> rowMapper = new RowMapper<Comment>() {
            @Override
            public Comment mapRow(ResultSet resultSet, int i) throws SQLException {
                Comment comment = new Comment();
                comment.setId(resultSet.getString("comments.id"));
                comment.setContent(resultSet.getString("comments.content"));
                User user = new User();
                user.setId(resultSet.getString("comments.user_id"));
                user.setName(resultSet.getString("users.name"));
                comment.setUser(user);
                comment.setPostId(resultSet.getString("comments.post_id"));
                if(resultSet.getString("comments.parents_comment_id") != null){
                    comment.setParentCommentId(resultSet.getString("comments.parents_comment_id"));
                }
                comment.setCreateDate(resultSet.getString("comments.created_date"));
                comment.setModifiedDate(resultSet.getString("comments.modified_date"));

                return comment;
            }
        };

        String sql = "select comments.id, comments.content, comments.user_id, comments.post_id, comments.parents_comment_id, comments.created_date, comments.modified_date, users.name" +
                " from comments left join users on comments.user_id = users.id where post_id = ? order by created_date asc";
        try {
            result = jdbcTemplate.query(sql, rowMapper, post.getId());
        }catch (Exception e){
            result = new ArrayList<>();
        }
        return result;
    }

    @Override
    public Comment findCommentById(String commentId) {
        Comment result = null;
        RowMapper<Comment> rowMapper = new RowMapper<Comment>() {
            @Override
            public Comment mapRow(ResultSet resultSet, int i) throws SQLException {
                Comment comment = new Comment();
                comment.setId(resultSet.getString("comments.id"));
                comment.setContent(resultSet.getString("comments.content"));
                User user = new User();
                user.setId(resultSet.getString("comments.user_id"));
                user.setName(resultSet.getString("users.name"));
                comment.setUser(user);
                comment.setPostId(resultSet.getString("comments.post_id"));
                if(resultSet.getString("comments.parents_comment_id") != null){
                    comment.setParentCommentId(resultSet.getString("comments.parents_comment_id"));
                }
                comment.setCreateDate(resultSet.getString("comments.created_date"));
                comment.setModifiedDate(resultSet.getString("comments.modified_date"));

                return comment;
            }
        };

        String sql = "select comments.id, comments.content, comments.user_id, comments.post_id, comments.parents_comment_id, comments.created_date, comments.modified_date, users.name" +
                " from comments left join users on comments.user_id = users.id where comments.id = ? order by created_date asc";
        try {
            List<Comment> query = jdbcTemplate.query(sql, rowMapper, commentId);
            if(query.size()==1){
                result = query.get(0);
            }
        }catch (Exception e){
            result = null;
        }
        return result;
    }

    @Override
    public Integer deleteComment(Comment comment) {

        Integer uc = 0;
        PreparedStatementSetter pss = new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement stmt) throws SQLException {
                stmt.setString(1,comment.getId());
            }
        };
        String sql = "UPDATE comments set user_id = 'tempuser', content = '삭제된 댓글입니다.' where id = ?";

        try {
            uc = jdbcTemplate.update(sql, pss);
        }catch (Exception e){
            uc = 0;
        }
        return uc;
    }
}
