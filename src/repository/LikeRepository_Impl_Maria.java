package repository;

import entity.Like;
import entity.Post;
import entity.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("likeRepository")
public class LikeRepository_Impl_Maria implements LikeRepository{

    private final JdbcTemplate jdbcTemplate;

    public LikeRepository_Impl_Maria(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public Integer addLike(Like like) {
        return null;
    }

    @Override
    public Like findByPostAndUser(Post post, User user) {
        return null;
    }

    @Override
    public List<Like> findLikesByPost(Post post) {
        return null;
    }
}
