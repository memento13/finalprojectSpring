package repository;

import entity.Party;
import entity.User;

import java.util.List;

public interface PartyRepository {

    public Party addParty(Party party);

    public List<Party> findByLeaderId(User leader);

    public Party findByName(String partyName);

    public Party findById(String partyId);

    public List<Party> findDidNotJoinPartiesByUser(User user);

    public List<Party> findDidNotJoinPartiesByUserAndKeyword(User user, String keyword);

    public Integer deleteParty(Party party);
}
