package repository;

import entity.Party;
import entity.Project;
import entity.User;
import entity.vo.ProjectAndMemberId;

import java.util.List;

public interface ProjectRepository {

    public Project addProject(Project project);
    public Integer deleteProject(Project project);
    public List<Project> findProjectsByParty(Party party);
    public Project findProjectByPartyAndName(Party party,String projectName);
    public List<ProjectAndMemberId> findProjectAndMemberIdByPartyAndUser(Party party, User user);
    public Project findById(String projectId);

}
