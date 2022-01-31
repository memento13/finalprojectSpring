package controller;

import entity.Party;
import entity.User;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import repository.Hangul;
import service.PartyService;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
            return "redirect:/create-party.pknu?result=createPartyFail";
        }
    }

//    내가 속한 파티 목록 모듈
    @RequestMapping("/party-list.pknu")
    public ModelAndView showPartyList(HttpSession session){

        ModelAndView mnv = new ModelAndView();
        User user = (User) session.getAttribute("user");

        List<Party> partyListWhereIamLeader = partyService.partiesWhereIamLeader(user);
        List<Party> partyListWhereIamMember = partyService.partiesWhereIamMember(user);
        mnv.addObject("leaderList",partyListWhereIamLeader);
        mnv.addObject("memberList",partyListWhereIamMember);
        mnv.setViewName("party_list_module");
        return mnv;
    }

    // 파티의 메인페이지
    @RequestMapping("/party.pknu")
    public ModelAndView partyPage(@RequestParam("party_name") String partyName){

        //파티 가입했는지 확인해야함 !! 추가해야함!!

        ModelAndView mnv = new ModelAndView();
        Map<String, Object> partyInfo = partyService.partyInfo(partyName);
        for (String key : partyInfo.keySet()) {
            Object obj = partyInfo.get(key);
            if(obj instanceof Party){
                mnv.addObject(key,(Party)obj);
            }
        }
//        mnv.setViewName("/WEB-INF/jsp/party/party_page.jsp");

        mnv.setViewName("party/party_page");

        return mnv;
    }

    // 파티 검색 페이지
    @RequestMapping("/party-search.pknu")
    public ModelAndView partySearchPage(){
        ModelAndView mnv = new ModelAndView();
        mnv.setViewName("party/party_search_page");
        return mnv;
    }


    // 파티 검색시 ajax를 이용해 json데이터를 반환 (한글 깨짐 수정안함)
    @ResponseBody
    @RequestMapping(value = "/party-search/search.pknu", produces = "application/text; charset=utf8")
    public String partySearch(@RequestParam(value = "keyword",required = false) String keyword,HttpSession session){

        User user = (User) session.getAttribute("user");
        List<Party> partyList = new ArrayList<>();

        partyList = partyService.partyListDidNotJoin(user, keyword);

        JSONObject result = new JSONObject();
        JSONArray data = new JSONArray();
        for (Party party : partyList) {
            JSONObject partyJson = new JSONObject();
            partyJson.put("id",party.getId());
            partyJson.put("name",party.getName());
            data.put(partyJson);
        }

        result.put("data",data);
        return Hangul.hangul(result.toString());
    }

    // 파티 참가 로직
    @RequestMapping("/party/join.pknu")
    public String partyJoin(@RequestParam(value = "party_id") String partyId, HttpSession session){
        User user = (User) session.getAttribute("user");
        Party party = new Party();
        party.setId(partyId);

        boolean result = partyService.joinParty(party, user);
        if(result){
            return "redirect:/main.pknu?result=joinPartySuccess";
        }
        else{
            return "redirect:/main.pknu?result=joinPartyFail";
        }
    }


}
