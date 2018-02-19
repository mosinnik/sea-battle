package commands.db;

import commands.Command;
import core.Input;
import dao.MemoryDAO;

import java.io.*;

/**
 * Created by IntelliJ IDEA.
 * User: mosinnik
 * Date: 23.07.2010
 * Time: 21:53:05
 * To change this template use File | Settings | File Templates.
 */
public class SaveDB implements Command
{
	public void execute()
	{
		System.out.println("\n\n\n\tSaveDB!!!!");
		System.out.println("Inter path and file name for save DB:");
		String path = Input.inputLine();
		if(!path.endsWith(".db"))
			path += ".db";
		File f = new File(path);
		try
		{
			FileOutputStream fos = new FileOutputStream(f);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(MemoryDAO.getInstance().getPlayersHashMap());
			oos.writeObject(MemoryDAO.getInstance().getGamesHashMap());
		}
		catch(FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		System.out.println("DB save successful.");
	}
}