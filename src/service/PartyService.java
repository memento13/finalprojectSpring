package service;

import entity.Party;
import entity.User;
import org.springframework.stereotype.Service;
import repository.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PartyService {

    private final PartyMemberRepository partyMemberRepository;
    private final PartyRepository partyRepository;
    private final UserRepository userRepository;

    public PartyService(UserRepository userRepository, PartyRepository partyRepository, PartyMemberRepository partyMemberRepository) {
        this.partyMemberRepository = partyMemberRepository;
        this.partyRepository = partyRepository;
        this.userRepository = userRepository;
    }

    /**
     * 파티를 생성할때 생성 성공시 true 반환 실패시 false
     *
     * 파티가 성공적으로 만들어지면
     * party_members에도 리더를 등록해줘야함
     *
     * @param user 파티장의 user값 parties의 leader_id에 넣어야함
     * @param party 생성할 파티의 이름
     * @return 파티를 생성할때 생성 성공시 true 반환 실패시 false
     */
    public boolean createParty(User user, Party party){
        System.out.println("PartyService.createParty");
        boolean result = false;
        party.setLeaderId(user.getId());
        Party createdParty = partyRepository.addParty(party);
        //완전한 파티 객체가 필요하다...
        //파티가 성공적으로 만들어진 경우
        if(createdParty != null){
            Integer partyMemberQueryCount = partyMemberRepository.addPartyLeader(user, createdParty);
            if(partyMemberQueryCount==1){
                result = true;
            }
        }
        return result;
    }

    /**
     * 해당하는 User가 리더인 파티를 찾음
     * @param user 파티장
     * @return 해당하는 User가 리더인 파티들이 담긴 리스트, 없는경우 빈 리스트를 반환함 null 아님!
     */
    public List<Party> partiesWhereIamLeader(User user){
        List<Party> partyList = new ArrayList<>();
         partyList= partyRepository.findByLeaderId(user);
         return partyList;
    }

    /**
     * 해당하는 User가 멤버인 파티를 찾음
     * @param user 파티장
     * @return 해당하는 User가 멤버인 파티들이 담긴 리스트, 없는경우 빈 리스트를 반환함 null 아님!
     */
    public List<Party> partiesWhereIamMember(User user){
        List<Party> partyList = partyMemberRepository.findPartiesByMemberUser(user);
        return partyList;
    }

    /**
     * 해당하는 partyName이 같은 파티의 정보를 모아서 전달해주는 함수
     * @param partyName 찾으려는 파티의 이름
     * @return Map<String,Object>를 반환하기 때문에
     *         객체가 Object로 업캐스팅하여 들어감
     *         그래서 객체 사용시 다운캐스팅 하여 사용해야함
     */
    public Map<String,Object> partyInfo(String partyName){
        Map<String, Object> resultMap = new HashMap<>();

        Party party = partyRepository.findByName(partyName);
        if(party != null){
            resultMap.put("party",party);
        }
        //추후 넘겨야할 정보 추가시 넣을 공간

        return resultMap;
    }
}
