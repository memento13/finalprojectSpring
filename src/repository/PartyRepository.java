package repository;

        import entity.Party;
        import entity.User;

        import java.util.List;

public interface PartyRepository {

    public Party addParty(Party party);
    public List<Party> findByLeaderId(User leader);
    public Party findByName(String partyName);
}
