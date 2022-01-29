package controller;

import entity.Party;
import entity.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import service.PartyService;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class PartyController {

    private final PartyService partyService;

    public PartyController(PartyService partyService) {
        this.partyService = partyService;
    }


    //    파티 생성페이지
    @RequestMapping("/create-party.pknu")
    public ModelAndView createPartyPage(){

        ModelAndView mnv = new ModelAndView();
        mnv.setViewName("create_party_page");
        return mnv;

    }

//    파티생성 로직
    @RequestMapping("/creat-party/create.pknu")
    public String createParty(HttpSession session, @RequestParam("party_name") String partyName){
        User user = (User) session.getAttribute("user");
        Party party = new Party();
        party.setName(partyName);
        boolean result = partyService.createParty(user, party);
        //  파티 생성 성공시
        if(result){
            return "redirect:/main.pknu?result=createPartySuccess";
        }
        // 실패시
        else{
            return "redirect:/creaet-party.pknu?result=createPartyFail";
        }
    }

//    내가 속한 파티 목록 모듈
    @RequestMapping("/party-list.pknu")
    public ModelAndView showPartyList(HttpSession session){

        ModelAndView mnv = new ModelAndView();
        User user = (User) session.getAttribute("user");

        List<Party> partyListWhereIamLeader = partyService.PartiesWhereIamLeader(user);
        List<Party> partyListWhereIamMember = partyService.PartiesWhereIamMember(user);
        mnv.addObject("leaderList",partyListWhereIamLeader);
        mnv.addObject("memberList",partyListWhereIamMember);
        mnv.setViewName("party_list_module");
        return mnv;
    }




}
