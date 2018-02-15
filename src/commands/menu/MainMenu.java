package commands.menu;

import commands.Command;
import core.Input;

/**
 * Created by IntelliJ IDEA.
 * User: mosinnik
 * Date: 26.07.2010
 * Time: 11:13:26
 * To change this template use File | Settings | File Templates.
 */
public class MainMenu
{
	public static void start()
	{
		Class a=null;
		boolean repeat=true;
		while(repeat)
		{
			int k=-1;
			System.out.println("\n\n\n\tMAIN MENU");
			System.out.println("1. Work with DB");
			System.out.println("2. New player");
			System.out.println("3. New game");//start game[new,continue,exit]
			System.out.println("4. Continue game");
			System.out.println("5. Help");
			System.out.println("6. Exit");
			System.out.println("Choose some way.");
			try
			{
				k=Input.inputSelect();
			}
			catch(NumberFormatException e)
			{
				System.out.println("Incorrect select. Select again.");
				k=-1;
			}
			if(k!=-1)
				try
				{
					switch(k)
					{
						case 1:
							a=Class.forName("commands.menu.DBMenu");
							break;
						case 2:
							a=Class.forName("commands.NewPlayer");
							break;
						case 3:
							a=Class.forName("commands.menu.GameMenu");
							break;
						case 4:
							a=Class.forName("commands.ContinueGame");
							break;
						case 5:
							a=Class.forName("commands.Help");
							break;
						case 6:
							return;
						default:
							a=Class.forName("commands.Help");
					}
					Command q=(Command)a.newInstance();
					q.execute();
				}
				catch(ClassNotFoundException e)
				{
					e.printStackTrace();
				}
				catch(InstantiationException e)
				{
					e.printStackTrace();
				}
				catch(IllegalAccessException e)
				{
					e.printStackTrace();
				}
		}
	}

}
