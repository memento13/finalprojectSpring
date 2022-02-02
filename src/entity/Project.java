package entity;

public class Project {
    private String id;
    private String name;
    private Party party_id = new Party();
    private String createDate;
    private String modifiedDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Party getParty(){
        return party_id;
    }
    public void setParty(Party party){
        this.party_id = party;
    }

    public String getParty_id() {
        return party_id.getId();
    }

    public void setParty_id(String party_id) {
        this.party_id.setId(party_id);
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
