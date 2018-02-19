package statistics;

import core.Game;
import core.characters.Player;

import java.util.HashMap;
import java.util.Map;


public class Statistics
{
	public Statistics(Map<Long, Game> mGames, Map<Long, Player> mPlayers)
	{
		this.mPlayersStat = new HashMap<Long, PlayerStat>();
		this.mGames = mGames;
		this.mPlayers = mPlayers;
	}

	public void collectStat()
	{
		long id = 0;
		for(Player p : mPlayers.values())
		{
			if(!p.isAI())
			{
				PlayerStat ps = new PlayerStat(p.getName());
				ps.addScore(p.getScore());
				id = p.getId();
				for(Game g : mGames.values())
				{
					if(g.getPlayersIds()[0] == id || g.getPlayersIds()[1] == id)
					{
						ps.addGameTime(g.getTime());
						ps.addNumberGame();
						if(!g.getState())
							if((g.getWin() && g.getPlayersIds()[0] == id) || (!g.getWin() && !(g.getPlayersIds()[0] == id)))
								ps.addNumberWinGame();
							else
								ps.addNumberLoseGame();
						else
							ps.addNumberNotFinishGame();
					}
				}
				mPlayersStat.put(ps.getId(), ps);
			}
		}
	}

	public void printStatistics()
	{
		System.out.println("Player\tScore\tGames\tWin\tLose\tTime");
		for(PlayerStat ps : mPlayersStat.values())
		{
			System.out.println(ps.toString());
		}
	}

	private Map<Long, PlayerStat> mPlayersStat;
	private Map<Long, Player> mPlayers;
	private Map<Long, Game> mGames;
}
