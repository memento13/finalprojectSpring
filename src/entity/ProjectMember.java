package entity;

public class ProjectMember {
    //  partyId 는 projectId.getPartyId()를 사용
    private Project projectId = new Project();
    private User userId = new User();
    private String createDate;
    private String modifiedDate;

    public String getProjectId() {
        return projectId.getId();
    }

    public void setProjectId(String projectId) {
        this.projectId.setId(projectId);
    }

    public String getPartyId() {
        return projectId.getParty_id();
    }

    public void setPartyId(String partyId) {
        this.projectId.setParty_id(partyId);
    }

    public String getUserId() {
        return userId.getId();
    }

    public void setUserId(String userId) {
        this.userId.setId(userId);
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
