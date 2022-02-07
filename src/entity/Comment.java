package entity;

import java.util.ArrayList;
import java.util.List;

public class Comment {
    private String id;
    private String content;
    private User user = new User();
    private Post post = new Post();
    private Comment parentComment = null;
    private String createDate;
    private String modifiedDate;

    private List<Comment> replies = new ArrayList<Comment>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    public String getUserId(){
        return user.getId();
    }
    public void setUserId(String userId){
        this.user.setId(userId);
    }
    public String getUserName(){
        return this.user.getName();
    }

    public void setUserName(String userName){
        this.user.setName(userName);
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }
    public String getPostId(){
        return post.getId();
    }
    public void setPostId(String postId){
        this.post.setId(postId);
    }

    public Comment getParentComment() {
        return parentComment;
    }

    public void setParentComment(Comment parentComment) {
        this.parentComment = parentComment;
    }
    public String getParentCommentId(){
        return parentComment.getId();
    }

    public void setParentCommentId(String parentCommentId){
        if(this.parentComment== null){
            this.parentComment = new Comment();
        }
        this.parentComment.setId(parentCommentId);
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

    public void appendReply(Comment childComment){
        this.replies.add(childComment);
    }

    public List<Comment> getReplies() {
        return replies;
    }
}
