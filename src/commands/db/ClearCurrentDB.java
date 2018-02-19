package commands.db;

import commands.Command;
import dao.MemoryDAO;

/**
 * Created by IntelliJ IDEA.
 * User: mosinnik
 * Date: 02.08.2010
 * Time: 16:06:40
 * To change this template use File | Settings | File Templates.
 */
public class ClearCurrentDB implements Command
{
	public void execute()
	{
		System.out.println("\n\n\n\tClearCurrentDB!!!!");
		MemoryDAO.getInstance().clearMemoryDAO();
		System.out.println("Memory cleared.");
	}
}