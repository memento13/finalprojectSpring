package service;

import entity.Party;
import entity.PartyMember;
import entity.Post;
import entity.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
    private final PostRepository postRepository;
    private final LikeRepository likeRepository;
    private final ProjectMemberRepository projectMemberRepository;

    public PartyService(UserRepository userRepository,
                        PartyRepository partyRepository,
                        PartyMemberRepository partyMemberRepository,
                        PostRepository postRepository,
                        LikeRepository likeRepository,
                        ProjectMemberRepository projectMemberRepository) {
        this.partyMemberRepository = partyMemberRepository;
        this.partyRepository = partyRepository;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.likeRepository = likeRepository;
        this.projectMemberRepository = projectMemberRepository;
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
    @Transactional(rollbackFor = Exception.class)
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
    @Transactional(readOnly = true)
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
    @Transactional(readOnly = true)
    public List<Party> partiesWhereIamMember(User user){
        List<Party> partyList = partyMemberRepository.findPartiesByMemberUser(user);
        return partyList;
    }

    /**
     * 해당하는 partyName이 같은 파티의 정보를 모아서 전달해주는 함수
     * @param partyName 찾으려는 파티의 이름
     * @param user 세션으로 전달받은 유저객체
     * @return Map<String,Object>를 반환하기 때문에
     *         객체가 Object로 업캐스팅하여 들어감
     *         그래서 객체 사용시 다운캐스팅 하여 사용해야함
     */
    @Transactional(readOnly = true)
    public Map<String,Object> partyInfo(String partyName,User user){
        Map<String, Object> resultMap = new HashMap<>();

        Party party = partyRepository.findByName(partyName);
        Boolean isPartyLeader = false;
        if(party != null){
            resultMap.put("party",party);
            if(user.getId().equals(party.getLeaderId())){
                isPartyLeader = true;
            }
            resultMap.put("isPartyLeader",isPartyLeader);

        }
        //추후 넘겨야할 정보 추가시 넣을 공간

        return resultMap;
    }

    /**
     * 해당 유저가 참가거나 설립하지 않은 파티들을 List<Party>에 담아서 전달해주는 함수
     * @param user 참가하지 않은 파티를 찾으려는 user의 user객체
     * @param keyword 검색할 키워드, 없으면 알아서 참가하지 않은 모든 파티를 반환
     * @return List<Party> 없는 경우 null이 아닌 빈 리스트를 반환함
     */
    @Transactional(readOnly = true)
    public List<Party> partyListDidNotJoin(User user,String keyword){
        List<Party> partyList = new ArrayList<>();
        if(keyword==null ||keyword.equals("")){
             partyList = partyRepository.findDidNotJoinPartiesByUser(user);
        }
        else{
            partyList = partyRepository.findDidNotJoinPartiesByUserAndKeyword(user,keyword);
        }
        return partyList;
    }


    /**
     * 유저가 파티 참가시 실행되는 함수
     * @param party 유저가 참가하려는 파티
     * @param user 유저
     * @return 파티 참가 성공시 true 실패시 false 반환
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean joinParty(Party party, User user){
        boolean result = false;
        //해당 파티에 가입 되어있는지 확인
        Integer uc = partyMemberRepository.checkUserJoinedParty(party, user);
        //파티에 가입
        if(uc==0){
            Integer inputUc = partyMemberRepository.addPartyMember(user, party);
            if(inputUc==1){
                result = true;
            }
        }
        return result;
    }

    /**
     * 유저가 파티 탈퇴시 실행되는 함수
     * 파티장 여부 확인
     * 게시글 삭제 ( 관련 좋아요까지 삭제)
     * 해당 파티와 관련된 게시글에 남긴 좋아요는 삭제 안함
     * (나중에 좋아요한 글 열람시 해당로직에서 차단되기 때문)
     * 프로젝트 멤버 삭제
     * 파티 멤버 삭제
     * @param party 유저가 탈퇴하려는 파티
     * @param user 유저
     * @return 파티 참가 성공시 true 실패시 false 반환
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean leaveParty(Party party, User user){

        boolean result = false;
        //파티 멤버 객체 찾아오기
        PartyMember partyMember = partyMemberRepository.findPartyMemberByPartyUser(party, user);
        if(partyMember != null){
            //파티장이 아니면 삭제로직
            if(partyMember.getGrade()==1){

                //게시글 삭제 ( 관련 좋아요까지 삭제)
                List<Post> posts = postRepository.findPostsByPartyMember(partyMember);
                for (Post post : posts) {
                    likeRepository.deleteLikesByPost(post);
                    postRepository.deletePost(post);
                }
                projectMemberRepository.deleteProjectMembersByPartyMember(partyMember);
                //파티 멤버 삭제
                Integer uc = partyMemberRepository.deletePartyMember(partyMember);
                if(uc==1){
                    result =true;
                }
            }
        }

        return result;
    }

    /**
     * 유저가 해당파티의 파티장인지 true false 로 알려줌
     * @param party 파티장인지 조사할 대상 파티
     * @param user 파티장인지 조사할 유저
     * @return 리더인 경유 true 아닌 경우 false 반환
     */
    @Transactional(readOnly = true)
    public boolean isLeaderInParty(Party party,User user){
        boolean result = false;
        party = partyRepository.findById(party.getId());
        if(party != null){
            if(party.getLeaderId().equals(user.getId())){
                result = true;
            }
        }
        return result;
    }

    /**
     * 유저가 파티 삭제시 실행되는 함수
     * 유저가 파티를 삭제할 수 있는 권한(파티장)인지 확인
     * 맞으면 파티 삭제 삭제 (on delete cascade 여서 연관 외래키 삭제됨)
     * @param party
     * @param user
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteParty(Party party, User user){
        boolean result = false;
        party = partyRepository.findById(party.getId());
        if(user.getId().equals(party.getLeaderId())){
            Integer uc = partyRepository.deleteParty(party);
            if(uc==1){
                result = true;
            }
        }
        return result;
    }
}
