package repository;

import entity.Party;
import entity.Project;
import entity.ProjectMember;
import entity.User;

public interface ProjectMemberRepository {
    ProjectMember addProjectMember(ProjectMember projectMember);

}