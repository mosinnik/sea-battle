package commands.menu;

import commands.Command;
import core.Input;

/**
 * Created by IntelliJ IDEA.
 * User: mosinnik
 * Date: 26.07.2010
 * Time: 11:22:19
 * To change this template use File | Settings | File Templates.
 */
public class GameMenu implements Command
{
	public void execute()
	{
		System.out.println("\n\n\n\tNewGame!!!!");
		Class a = null;
		while(true)
		{
			int k = -1;
			System.out.println("\tGAME MENU");
			System.out.println("Select type of game:");
			System.out.println("1. Human vs. Human.");
			System.out.println("2. Human vs. AI.");
			System.out.println("3. AI vs. AI.");
			System.out.println("4. Return.");
			System.out.println("Choose some way.");
			try
			{
				k = Input.inputSelect();
				switch(k)
				{
					case 1:
						a = Class.forName("commands.game.HumanVsHumanGame");
						break;
					case 2:
						a = Class.forName("commands.game.HumanVsAIGame");
						break;
					case 3:
						a = Class.forName("commands.game.AIVsAIGame");
						break;
					case 4:
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
			catch(NumberFormatException e)
			{
				System.out.println("Incorrect select. Select again.");
			}
		}
	}
}