package service;

import entity.*;
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
    private final PostRepository postRepository;
    private final PostService postService;

    public ProjectService(PartyRepository partyRepository,
                          ProjectRepository projectRepository,
                          UserRepository userRepository,
                          ProjectMemberRepository projectMemberRepository,
                          PostRepository postRepository,
                          PostService postService) {
        this.partyRepository = partyRepository;
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
        this.projectMemberRepository = projectMemberRepository;
        this.postRepository = postRepository;
        this.postService = postService;

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
        project.setParty(partyRepository.findById(project.getParty_id()));
        if(project != null){
            resultMap.put("project",project);
        }
        List<Post> posts = postService.projectPosts(project);
        if(posts != null){
            resultMap.put("posts",posts);
        }

        return resultMap;
    }

    /**
     * project에서 유저 탈퇴와 탈퇴 성공여부를 반환하는 함수
     * 프로젝트에서 탈퇴시 게시글은 사라지지 않음
     * @param project 탈퇴 하려는 프로젝트
     * @param user 탈퇴하려는 유저
     * @return 성공시 true, 실패시 false 반환
     */
    public boolean leaveProject(Project project,User user){
        boolean result = false;

        ProjectMember projectMember = new ProjectMember();
        projectMember.setPartyId(project.getParty_id());
        projectMember.setProjectId(project.getId());
        projectMember.setUserId(user.getId());
        Integer uc = projectMemberRepository.deleteProjectMember(projectMember);
        if(uc==1){
            result = true;
        }
        return result;
    }

    /**
     * project를 삭제시 실행되는 함수
     * 실행하는 유저가 프로젝트를 삭제할 수 있는 유저(파티장)인지 확인
     * 프로젝트에 속한 게시글 삭제
     * 프로젝트 멤버에서 해당 프로젝트멤버 삭제
     * 프로젝트 삭제
     * @param project 삭제하려는 프로젝트 (id 값만 있는 깡통임)
     * @param user 삭제하려고 시도하는 유저 ( 프로젝트가 속한 파티장인지 검증해야함)
     * @return 삭제 성공시 true 실패시 false 반환
     */
    public boolean deleteProject(Project project,User user){
        boolean result = false;
        project = projectRepository.findById(project.getId());
        project.setParty(partyRepository.findById(project.getParty_id()));

        //파티장인지 검증
        boolean isPartyLeader = false;
        if(project.getParty().getLeaderId().equals(user.getId())){
            isPartyLeader = true;
        }

        if(isPartyLeader){

            postRepository.deletePostByProject(project);
            projectMemberRepository.deleteProjectMembersByProject(project);
            Integer uc = projectRepository.deleteProject(project);
            System.out.println("uc = " + uc);
            if(uc==1){
                result = true;
            }
        }
        System.out.println("ProjectService.deleteProject");
        System.out.println("result = " + result);

        return  result;
    }
}
