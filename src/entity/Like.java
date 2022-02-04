package entity;

public class Like {
    private Post post = new Post();

    private String createDate;
    private String modifiedDate;

    public String getPostId() {
        return post.getId();
    }

    public void setPostId(String postId) {
        this.post.setId(postId);
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public User getUser() {
        return post.getUser();
    }

    public void setUser(User user) {
        post.setUser(user);
    }

    public String getUserId() {
        return post.getUser().getId();
    }

    public void setUserId(String userId) {
        this.post.getUser().setId(userId);
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(String modifiedDate) {
        this.modifiedDate = modifiedDate;
    }
}
