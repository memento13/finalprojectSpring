package entity.vo;

import entity.Project;

public class ProjectAndMemberId {
    private Project project;
    private String memberId;

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }
}
