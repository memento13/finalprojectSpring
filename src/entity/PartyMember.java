package entity;

public class PartyMember {
    private User userId = new User();
    private Party partyId = new Party();
    private Integer grade;
    private String createDate;
    private String modifiedDate;

    public String getUserId() {
        return userId.getId();
    }

    public void setUserId(String userId) {
        this.userId.setId(userId);
    }

    public String getPartyId() {
        return partyId.getId();
    }

    public void setPartyId(String partyId) {
        this.partyId.setId(partyId);
    }

    public Integer getGrade() {
        return grade;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
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
