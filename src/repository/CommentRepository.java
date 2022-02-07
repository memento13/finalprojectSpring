package repository;

import entity.Comment;
import entity.Post;

import java.util.List;

public interface CommentRepository {
    public Integer addComment(Comment comment);
    public List<Comment> findCommentByPost(Post post);
    public Integer deleteComment(Comment comment);
}
