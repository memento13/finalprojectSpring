package repository;

import entity.Like;
import entity.Post;
import entity.User;

import java.util.List;

public interface LikeRepository {

    public Integer addLike(Like like);
    public Like findByPostAndUser(Post post, User user);
    public List<Like> findLikesByPost(Post post);
    public Integer deleteLikesByPost(Post post);

}
