package repository;

import entity.*;

import java.util.List;

public interface PostRepository {
    public Post addPost(Post post);
    public List<Post> findPostsByProject(Project project);
    public List<Post> findPostsByPartyMember(PartyMember partyMember);
    public Post findById(String postId);
    public Integer deletePost(Post post);

}
