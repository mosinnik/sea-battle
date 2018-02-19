package commands;

import core.Input;

/**
 * Created by IntelliJ IDEA.
 * User: mosinnik
 * Date: 12.07.2010
 * Time: 23:14:16
 * To change this template use File | Settings | File Templates.
 */
public class ContinueGame implements Command
{
	public void execute()
	{
		System.out.println("\n\n\n\tContinueGame!!!!");
		Class a = null;
		while(true)
		{
			int k = -1;
			System.out.println("\tCONTINUE GAME MENU");
			System.out.println("Select type of game:");
			System.out.println("1. Human vs. Human.");
			System.out.println("2. Human vs. AI.");
			System.out.println("3. Return.");
			System.out.println("Choose some way.");
			try
			{
				k = Input.inputSelect();
				switch(k)
				{
					case 1:
						a = Class.forName("commands.continueGameCommands.ContinueHumanVsHumanGame");
						break;
					case 2:
						a = Class.forName("commands.continueGameCommands.ContinueHumanVsAIGame");
						break;
					case 3:
						return;
					default:
						a = Class.forName("commands.Help");
				}
				Command q = (Command)a.newInstance();
				q.execute();
			}
			catch(NumberFormatException e)
			{
				System.out.println("Incorrect select. Select again.");
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
	}
}