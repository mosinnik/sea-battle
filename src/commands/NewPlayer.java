package commands;

import DAO.MemoryDAO;
import core.Input;

/**
 * Created by IntelliJ IDEA.
 * User: mosinnik
 * Date: 12.07.2010
 * Time: 23:14:01
 * To change this template use File | Settings | File Templates.
 */
public class NewPlayer implements Command
{
	public void execute()
	{
		System.out.println("\n\n\n\t\tNewPlayer!!!!");
		String name=Input.inputName();
		if(name.toLowerCase().equals("exit"))
			return;
		else if(name.toLowerCase().equals("help") || name.toLowerCase().equals("-h") || name.toLowerCase().equals("/?"))
		{
			System.out.println("\nHelp.\nIf you want create new player inter name and if this name not used by other men you can use it else you should inter other name.");
			execute();
		}
		else
		{
			if(MemoryDAO.getInstance().getPlayerId(name)!=-1)
				System.out.println(name+" is already created.");
			else
				newPlayer(name);
			System.out.print("Do you want add another players to db?");
			if(Input.yesOrNo())
				execute();
		}
	}

	public static long newPlayer(String name)
	{
		long playerId=MemoryDAO.getInstance().newPlayer(name);
		System.out.println(name+" created.");
		return playerId;
	}
}
