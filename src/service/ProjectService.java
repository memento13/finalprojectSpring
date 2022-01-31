package service;

import entity.Party;
import entity.Project;
import entity.User;
import org.springframework.stereotype.Service;
import repository.*;

@Service
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final PartyRepository partyRepository;
    private final UserRepository userRepository;

    public ProjectService(PartyRepository partyRepository, ProjectRepository projectRepository, UserRepository userRepository) {
        this.partyRepository = partyRepository;
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
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
}
