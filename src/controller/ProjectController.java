package controller;

import entity.Party;
import entity.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import repository.Hangul;
import repository.PartyRepository;
import repository.PartyRepository_Impl_Maria;
import service.PartyService;
import service.ProjectService;

import javax.servlet.http.HttpSession;

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




}
