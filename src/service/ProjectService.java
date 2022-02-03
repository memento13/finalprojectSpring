package service;

import entity.Party;
import entity.Project;
import entity.ProjectMember;
import entity.User;
import entity.vo.ProjectAndMemberId;
import org.springframework.stereotype.Service;
import repository.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final PartyRepository partyRepository;
    private final UserRepository userRepository;
    private final ProjectMemberRepository projectMemberRepository;

    public ProjectService(PartyRepository partyRepository, ProjectRepository projectRepository, UserRepository userRepository, ProjectMemberRepository projectMemberRepository) {
        this.partyRepository = partyRepository;
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
        this.projectMemberRepository = projectMemberRepository;

    }


    /**
     * 파티장이 파티에 프로젝트를 추가하는 함수
     * @param party 프로젝트를 추가할 파티
     * @param projectName 만들 프로젝트의 이름
     * @return 성공시 true 실패시 false 반환
     */
    public boolean createProject(Party party, String projectName,User user){

        boolean result = false;

        // 파티장인지 확인
        if(!party.getLeaderId().equals(user.getId())){
            return result;
        }

        Project project = new Project();
        project.setParty_id(party.getId());
        project.setName(projectName);

        // 같은 파티내 중복이름 체크
        Project nameExistedProject = projectRepository.findProjectByPartyAndName(party, projectName);

        //프로젝트 생성
        if(nameExistedProject == null){
            project = projectRepository.addProject(project);
            if(project != null){
                result = true;
            }
        }

        return result;
    }

    /**
     * 파티에 생성된 프로젝트와
     * 해당 유저가 프로젝트의 참가 여부를 알려주는 리스트 반환
     * @param party
     * @param user
     * @return 값이 없으면 null 이 아닌 빈 리스트가 반환됨
     */
    public List<ProjectAndMemberId> checkUserJoinedProjectList(Party party, User user){
        List<ProjectAndMemberId> result = projectRepository.findProjectAndMemberIdByPartyAndUser(party, user);
        return result;
    }

    /**
     * 유저를 프로젝트에 추가하는 함수
     * @param project 프로젝트
     * @param user 유저
     * @return 성공시 true , 실패시 false 반환
     */
    public boolean joinProject(Project project,User user){
        boolean result = false;
        ProjectMember projectMember = new ProjectMember();
        projectMember.setPartyId(project.getParty_id());
        projectMember.setProjectId(project.getId());
        projectMember.setUserId(user.getId());
        projectMember = projectMemberRepository.addProjectMember(projectMember);

        if(projectMember!= null){
            result = true;
        }

        return result;
    }

    /**
     * 유저가 해당 프로젝트에 가입중인지 검증하는 함수
     * @param project 유저가 가입한건지 검증할 프로젝트
     * @param user 검증할려는 유저
     * @return 가입시 true, 미가입시 false 반환
     */
    public boolean checkUserJoinedProject(Project project,User user){
        boolean result = false;
        ProjectMember projectMember= projectMemberRepository.findByProjectAndUser(project, user);
        if(projectMember != null){
            result = true;
        }
        return result;
    }

    /**
     * project의 정보를 모아서 Map으로 전달하는 함수
     * @param project id값만 있는 프로젝트
     * @return 프로젝트페이지에 띄우기 위한 정보가 있는 모든 값
     */
    public Map<String,Object> projectInfo(Project project){
        Map<String, Object> resultMap = new HashMap<>();
        project = projectRepository.findById(project.getId());
        if(project != null){
            resultMap.put("project",project);
        }
        return resultMap;
    }
}
