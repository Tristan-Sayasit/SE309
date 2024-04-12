package jparepos;

import entities.Player;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IPlayerRepository extends CrudRepository<Player, Long> {
    Player getPlayerByUserId(long userId);
    Player getAllPlayers();
    void InsertNewPlayer(Player newPlayer);
    void UpdatePlayerById(Player updatedPlayer);
}
