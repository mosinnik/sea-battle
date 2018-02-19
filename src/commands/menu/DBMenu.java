package commands.menu;

import commands.Command;
import core.Input;

/**
 * Created by IntelliJ IDEA.
 * User: mosinnik
 * Date: 02.08.2010
 * Time: 14:30:54
 * To change this template use File | Settings | File Templates.
 */
public class DBMenu implements Command
{
	public void execute()
	{
		Class a = null;
		boolean repeat = true;
		while(repeat)
		{
			int k = -1;
			System.out.println("\n\n\n\tDB MENU");
			System.out.println("1. Load DB");
			System.out.println("2. Save DB");
			System.out.println("3. Clear current DB");
			System.out.println("4. Collect statistics");
			System.out.println("5. Help");
			System.out.println("6. Exit");
			System.out.println("Choose some way.");
			try
			{
				k = Input.inputSelect();
			}
			catch(NumberFormatException e)
			{
				System.out.println("Incorrect select. Select again.");
				k = -1;
			}
			if(k != -1)
				try
				{
					switch(k)
					{
						case 1:
							a = Class.forName("commands.db.LoadDB");
							break;
						case 2:
							a = Class.forName("commands.db.SaveDB");
							break;
						case 3:
							a = Class.forName("commands.db.ClearCurrentDB");
							break;
						case 4:
							a = Class.forName("commands.db.CollectStatistics");
							break;
						case 5:
							a = Class.forName("commands.Help");
							break;
						case 6:
							return;
						default:
							a = Class.forName("commands.Help");
					}
					Command q = (Command)a.newInstance();
					q.execute();
				}
				catch(ClassNotFoundException | InstantiationException | IllegalAccessException e)
				{
					e.printStackTrace();
				}
		}
	}
}
