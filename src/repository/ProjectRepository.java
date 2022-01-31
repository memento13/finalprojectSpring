package repository;

import entity.Party;
import entity.Project;

import java.util.List;

public interface ProjectRepository {

    public Project addProject(Project project);
    public List<Project> findProjectsByParty(Party party);
    public Project findProjectByPartyAndName(Party party,String projectName);

}
