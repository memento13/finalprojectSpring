package repository;

import entity.Party;
import entity.User;

import java.util.List;

public interface PartyRepository {

    public Integer addParty(Party party);
    public List<Party> findByLeaderId(User leader);
}
