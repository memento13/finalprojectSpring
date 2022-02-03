package entity;

public class Post {
    private String id;
    private String title;
    private String content;
    private User user = new User();
    private Project project = new Project();
    private String createDate;
    private String modifiedDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getUserId() {
        return user.getId();
    }

    public void setUser(User user) {
        this.user = user;
    }
    public void setUserId(String userId) {
        this.user.setId(userId);
    }

    public Project getProject() {
        return project;
    }
    public String getProjectId(){
        return project.getId();
    }

    public void setProject(Project project) {
        this.project = project;
    }
    public void setProjectId(String projectId) {
        this.project.setId(projectId);
    }

    public Party getParty() {
        return project.getParty();
    }

    public void setParty(Party party) {
        this.project.setParty(party);
    }

    public String getPartyId(){
        return project.getParty_id();
    }
    public void setPartyId(String partyId) {
        this.project.setParty_id(partyId);
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
