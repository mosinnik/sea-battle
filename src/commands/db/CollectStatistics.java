package commands.db;

import commands.Command;
import dao.MemoryDAO;

/**
 * Created by IntelliJ IDEA.
 * User: mosinnik
 * Date: 02.08.2010
 * Time: 14:44:19
 * To change this template use File | Settings | File Templates.
 */
public class CollectStatistics implements Command
{
	public void execute()
	{
		System.out.println("\n\n\n\tCollectStatistics!!!!");
		MemoryDAO.getInstance().collectStat();
		MemoryDAO.getInstance().getStatistics().printStatistics();
		System.out.println();
	}
}
