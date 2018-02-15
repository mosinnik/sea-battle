package core.arrays;

/**
 * Created by IntelliJ IDEA.
 * User: mosinnik
 * Date: 02.07.2010
 * Time: 12:45:41
 * To change this template use File | Settings | File Templates.
 */
public class CoordinateArray
{
	private boolean onlyIK;
	private int i;
	private int k;
	private boolean direction;

	public CoordinateArray(int i, int k)
	{
		this.i=i;
		this.k=k;
		onlyIK=true;
	}

	public CoordinateArray(int i, int k, boolean direction)
	{
		this.i=i;
		this.k=k;
		this.direction=direction;
		onlyIK=false;
	}

	public int getI()
	{
		return i;
	}

	public int getK()
	{
		return k;
	}

	public boolean getDirection()
	{
		return direction;
	}

	public void setI(int i)
	{
		this.i=i;
	}

	public void setK(int k)
	{
		this.k=k;
	}

	public void setDirection(boolean direction)
	{
		this.direction=direction;
	}

	public boolean onlyIK()
	{
		return onlyIK;
	}
}
