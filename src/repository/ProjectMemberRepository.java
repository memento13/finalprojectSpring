package repository;

import entity.Project;
import entity.ProjectMember;
import entity.User;

public interface ProjectMemberRepository {
    ProjectMember addProjectMember(ProjectMember projectMember);
    ProjectMember findByProjectAndUser(Project project,User user);
    Integer deleteProjectMember(ProjectMember projectMember);

}
