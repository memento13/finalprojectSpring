package repository;

import entity.Party;
import entity.PartyMember;
import entity.User;

import java.util.List;

public interface PartyMemberRepository {

    public Integer addPartyMember(User user, Party party);
    public Integer addPartyLeader(User user,Party party);

    public List<Party> findPartiesByLeaderUser(User user);
    public List<Party> findPartiesByMemberUser(User user);
    public List<Party> findPartiesByUser(User user);

    public List<User> findUsersByParty(Party party);
    public Integer checkUserJoinedParty(Party party,User user);
    public PartyMember findPartyMemberByPartyUser(Party party,User user);
    public Integer deletePartyMember(PartyMember partyMember);

}
