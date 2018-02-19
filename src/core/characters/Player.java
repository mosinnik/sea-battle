package core.characters;

import java.io.Serializable;

public class Player implements Serializable
{
	private String name;
	private int score;
	private long id;
	private boolean isAI;
	private String password;

	private static long playerId;

	public Player(String name, boolean isAI)
	{
		this.name = name;
		score = 0;
		id = playerId;
		this.isAI = isAI;
		Player.incPlayerId();
	}

	public String getName()
	{
		return name;
	}

	public int getScore()
	{
		return score;
	}

	public void addScore(int inc)
	{
		score += inc;
		//System.out.println("addInPlayer:"+name+"  inc:"+inc);
	}

	public long getId()
	{
		return id;
	}

	public boolean isAI()
	{
		return isAI;
	}

	public boolean checkPassword(String password)
	{
		return this.password.equals(password);
	}

	public void changePassword(String password)
	{
		this.password = password;
	}

	//work with players id
	public static long getPlayerId()
	{
		return playerId;
	}

	public static long getNextPlayerId()
	{
		playerId++;
		return playerId;
	}

	public static void incPlayerId()
	{
		playerId++;
	}
}
