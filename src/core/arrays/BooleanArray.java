package core.arrays;

/**
 * Created by IntelliJ IDEA.
 * User: mosinnik
 * Date: 12.07.2010
 * Time: 17:47:57
 * To change this template use File | Settings | File Templates.
 */
public class BooleanArray
{
	public BooleanArray()
	{
		isShotSuccessful=false;
		isShipDown=false;
	}

	public boolean isShotSuccessful()
	{
		return isShotSuccessful;
	}

	public void shotSuccessful()
	{
		isShotSuccessful=true;
	}

	public boolean isShipDown()
	{
		return isShipDown;
	}

	public void shipDown()
	{
		isShipDown=true;
	}

	private boolean isShotSuccessful;
	private boolean isShipDown;
}
