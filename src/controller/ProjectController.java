package controller;

import entity.Party;
import entity.User;
import entity.vo.ProjectAndMemberId;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import repository.Hangul;
import repository.PartyRepository;
import service.PartyService;
import service.ProjectService;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class ProjectController {

    private final ProjectService projectService;
    private final PartyService partyService;
    private final PartyRepository partyRepository;

    public ProjectController(ProjectService projectService, PartyService partyService, PartyRepository partyRepository) {
        this.projectService = projectService;
        this.partyService = partyService;
        this.partyRepository = partyRepository;
    }

    //프로젝트 생성페이지
    @RequestMapping("/create-project.pknu")
    public ModelAndView createProjectPage(@RequestParam("party_id") String partyId, HttpSession session){
        ModelAndView mnv = new ModelAndView();
        Party party = partyRepository.findById(partyId);
        User user = (User) session.getAttribute("user");

        // 세션 id 값과 파티의 leader_id 값이 같은가?
        if(partyService.isLeaderInParty(party , user)){
//            mnv.addObject("party_id",partyId);
            mnv.addObject("party",party);
            mnv.setViewName("creat_project_page");
        }else{
            // 아니면 메인페이지로 이동
            mnv.setViewName("redirect:/main.pknu?msg=incorrectConnection");
        }
        return mnv;
    }

    //프로젝트 생성 로직
    @RequestMapping("/create-project/create.pknu")
    public String createProject(
            @RequestParam("project_name")String projectName,
            @RequestParam("party_id")String partyId,
            HttpSession session
            ){

        User user = (User) session.getAttribute("user");
        Party party = partyRepository.findById(partyId);
        boolean result = projectService.createProject(party, projectName,user);

        String url ="redirect:/party.pknu?party_name="+Hangul.hangul(party.getName());
        //프로젝트 생성 성공
        if(result){
            return url+"&msg=make_project_success";
        }
        //프로젝트 생성 실패
        else{
            return url+"&msg=make_project_fail";
        }
    }

    //유저가 해당파티의 프로젝트,프로젝트 가입여부들을 json으로 반환
    @ResponseBody
    @RequestMapping(value = "/project-list.pknu",produces = "application/text; charset=utf8")
    public String projectList(@RequestParam("party_id") String partyId, HttpSession session){
        System.out.println("ProjectController.projectList");
        User user = (User) session.getAttribute("user");
        // 유저가 파티에 가입되었는지 부터 체크해야할듯
        //오류발생 위험 있음...try문으로 덮어야하나..
        Party party = new Party();
        party.setId(partyId);

        JSONObject result = new JSONObject();
        JSONArray data = new JSONArray();

        List<ProjectAndMemberId> projectAndMemberIdList = projectService.userJoinedProjectList(party, user);

        for (ProjectAndMemberId vo : projectAndMemberIdList) {
            JSONObject projectAndJoined = new JSONObject();
            projectAndJoined.put("project_id",vo.getProject().getId());
            projectAndJoined.put("project_name",vo.getProject().getName());
            boolean joined = false;
            if(vo.getMemberId()!= null){
                joined = true;
            }
            projectAndJoined.put("joined",joined);
            data.put(projectAndJoined);
        }
        result.put("user_id",user.getId());
        result.put("party_id",partyId);
        result.put("data",data);
        return result.toString();

    }

    //프로젝트 참가

    //프로젝트 탈퇴




}
