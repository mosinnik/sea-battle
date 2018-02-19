package dao;

import core.Game;
import core.characters.Player;

public interface DAO
{
	public void addGame(Game g);

	public Game getGame(Long id);

	public void removeGame(Long id);

	public long getGameId(Long id1, Long id2);

	public void addPlayer(Player p);

	public Player getPlayer(Long id);

	public void removePlayer(Long id);

	public long getPlayerId(String name);
 /*
	public void addAI(AI ai);
	public AI getAI(Long id);
	public void removeAI(Long id);
	public long getAIId(String name);   */
}
