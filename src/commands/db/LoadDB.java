package commands.db;

import commands.Command;
import core.Game;
import core.Input;
import core.characters.Player;
import dao.MemoryDAO;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: mosinnik
 * Date: 12.07.2010
 * Time: 23:13:34
 * To change this template use File | Settings | File Templates.
 */
public class LoadDB implements Command
{
	public void execute()
	{
		System.out.println("\n\n\n\tLoadDB!!!!");
		System.out.println("Inter path and file name for load DB:");
		String path = Input.inputLine();
		if(!path.endsWith(".db"))
			path += ".db";
		File f = new File(path);
		if(f.exists())
		{
			try
			{
				FileInputStream fis = new FileInputStream(path);
				ObjectInputStream oos = new ObjectInputStream(fis);
				MemoryDAO.getInstance().setPlayersHashMap((HashMap<Long, Player>)oos.readObject());
				MemoryDAO.getInstance().setGamesHashMap((HashMap<Long, Game>)oos.readObject());
			}
			catch(ClassNotFoundException e)
			{
				e.printStackTrace();
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
			System.out.println("DB load successful.");
		}
		else
		{
			System.out.println("File don't exist.");
		}
	}

}