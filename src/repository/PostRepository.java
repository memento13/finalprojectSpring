package repository;

import entity.Post;
import entity.Project;

import java.util.List;

public interface PostRepository {
    public Post addPost(Post post);
    public List<Post> findPostsByProject(Project project);
    public Post findById(String postId);
    public Integer deletePost(Post post);
}
