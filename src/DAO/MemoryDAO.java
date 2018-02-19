package DAO;

import core.Game;
import core.characters.AI;
import core.characters.Player;
import statistics.PlayerStat;
import statistics.Statistics;

import java.util.HashMap;
import java.util.Map;


public class MemoryDAO implements DAO
{
	public static final boolean VERTICAL = true;
	public static final boolean HORIZONTAL = false;

	private static MemoryDAO memoryDAO;
	private Map<Long, Player> players;
	private Map<Long, Game> games;
	private Map<Long, PlayerStat> playersStat;
//	private HashMap<Long, AI> ais;

	private Statistics stats;

	/**
	 * collect statistics
	 */
	public void collectStat()
	{
		stats = null;
		stats = new Statistics(games, players);
		stats.collectStat();
	}

	public Statistics getStatistics()
	{
		return stats;
	}

	public Map<Long, Player> getPlayersHashMap()
	{
		return players;
	}

	public Map<Long, Game> getGamesHashMap()
	{
		return games;
	}

	public void setPlayersHashMap(Map<Long, Player> players)
	{
		this.players = null;
		this.players = players;
	}

	public void setGamesHashMap(Map<Long, Game> games)
	{
		this.games = null;
		this.games = games;
	}

	/**
	 * game's methods
	 */
	public long newGame(Player firstPlayer, Player secondPlayer, int mapSize)
	{
		Game g = new Game(firstPlayer, secondPlayer, mapSize);
		addGame(g);
		return g.getId();
	}

	public void addGame(Game g)
	{
		games.put(g.getId(), g);
	}

	public void setGame(Game g)
	{
		games.put(g.getId(), g);
	}

	public Game getGame(Long id)
	{
		return games.get(id);
	}

	public long getGameId(Long id1, Long id2)
	{
		long i = 0;
		for(Game g : games.values())
			if(g.isPlayersIds(id1, id2))
				return i;
			else
				i++;
		return -1;
	}

	public void removeGame(Long id)
	{
		games.remove(id);
	}

	/**
	 * player's methods
	 */
	public Long newPlayer(String name)
	{
		Player p = new Player(name, false);
		addPlayer(p);
		return p.getId();
	}

	public void addPlayer(Player p)
	{
		players.put(p.getId(), p);
	}

	public void setPlayer(Player p)
	{
		players.put(p.getId(), p);
	}

	public long getPlayerId(String name)
	{
		for(Player g : players.values())
			if(g.getName().equals(name))
				return g.getId();
		return -1;
	}

	public Player getPlayer(Long id)
	{
		return players.get(id);
	}

	public void removePlayer(Long id)
	{
		players.remove(id);
	}


	/**
	 * AI's methods
	 */
	public long newAI(String name, int difficulty)
	{
		AI ai = new AI(name, difficulty);
		addPlayer(ai);
		return ai.getId();
	}


	public void setAI(AI ai)
	{
		setPlayer(ai);
	}

/*
	public void addAI(AI ai)
	{
		ais.put(ai.getId(),ai);
	}

	public long getAIId(String name)
	{
		for(AI ai : ais.values())
			if(ai.getName().equals(name))
				return ai.getId();
		return -1;
	}

	public AI getAI(Long id)
	{
		return ais.get(id);
	}

	public void removeAI(Long id)
	{
		ais.remove(id);
	}
*/

	private MemoryDAO()
	{
		players = new HashMap<>();
		games = new HashMap<>();
		//ais=new HashMap<Long, AI>();
		playersStat = new HashMap<>();
	}

	public static MemoryDAO getInstance()
	{
		if(memoryDAO == null)
		{
			memoryDAO = new MemoryDAO();
		}
		return memoryDAO;
	}

	public void clearMemoryDAO()
	{
		players.clear();
		games.clear();
		playersStat.clear();
	}
}
