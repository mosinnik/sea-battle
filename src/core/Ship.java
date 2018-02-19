package core;

import core.arrays.CoordinateArray;

import java.io.Serializable;

public class Ship implements Serializable
{
	public Ship(CoordinateArray coordinates, int length)
	{
		this.i = coordinates.getI();
		this.k = coordinates.getK();
		this.length = length;
		this.direction = coordinates.getDirection();
		state = new boolean[length];
		for(int ii = 0; ii < length; ii++)
			state[ii] = true;
		cells = new int[length][2];
		setCells(i, k, length, direction);
		isSea = false;
	}

	public Ship(boolean isSea)
	{
		this.isSea = isSea;
	}

	public boolean isDown()
	{
		for(int i = 0; i < length; i++)
			if(state[i])
				return false;
		return true;
	}

	public boolean isSea()
	{
		return isSea;
	}

	void setCells(int i, int k, int length, boolean direction)
	{
		int dI = 0, dK = 0;

		if(!direction)
			dK = 1;
		else
			dI = 1;

		cells[0][0] = i;
		cells[0][1] = k;
		System.out.println(cells[0][0] + " " + cells[0][1]);

		for(int ii = 1; ii < length; ii++)
		{
			cells[ii][0] = cells[ii - 1][0] + dI;
			//System.out.print(cells[ii][0]+" ");
			cells[ii][1] = cells[ii - 1][1] + dK;
			//System.out.println(cells[ii][1]);
		}
		//System.out.println("=====");
	}

	public int[] getPos()
	{
		int[] pos = {i, k, length, direction ? 1 : 0};
		return pos;
	}

	int destroy(int i, int k)
	{
		int ind = getInd(i, k);
		if(ind >= 0)
			if(state[ind])
			{
				state[ind] = false;
				//System.out.println("="+ind+"="+state[ind]+"=");
			}
			else
			{
				ind = -1;
			}
		return ind;
	}

	int getInd(int i, int k)
	{
		if(!isSea)
			for(int ii = 0; ii < length; ii++)
				if(cells[ii][0] == i && cells[ii][1] == k)
					return ii;
		return -1;
	}

	public boolean getState(int i, int k)
	{
		return state[getInd(i, k)];
	}


	private int i;
	private int k;
	private boolean direction;
	private int length;
	public int[][] cells;
	private boolean[] state;
	private boolean isSea;
}
