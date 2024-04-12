package jparepos;

import entities.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IPlayerRepository extends JpaRepository<Player, Long> {
    Player getPlayerByUserId(long userId);
    Player getAllPlayers();
    void InsertNewPlayer(Player newPlayer);
    void UpdatePlayerById(Player updatedPlayer);
}
