package core.characters;

import core.arrays.BooleanArray;
import core.arrays.CoordinateArray;
import core.Game;

import java.util.Random;


public class AI extends Player
{
	public AI(String name, int difficulty)
	{
		super(name,true);
		this.difficulty=difficulty;
		this.rnd=new Random();
	}

	public int getDifficulty()
	{
		return difficulty;
	}

	public void addShip()
	{
		for(int x=0;x<mapSize;x++)
			for(int y=0;y<mapSize;y++)
				shipMap[x][y]=0;

		CoordinateArray coordinates=new CoordinateArray(-1,-1,false);
		int n=g.getCountShip();
		int checker=0;
		boolean repeat=true;
//		System.out.println("addShip():");
//		System.out.println(" n: "+n);

		while(repeat)
		{
			repeat=false;
			checker=0;
			for(int x=0;x<mapSize;x++)
				for(int y=0;y<mapSize;y++)
					shipMap[x][y]=0;
			g.resetMap(super.getId());

			for(int length=n;length>0;length--)
			{
				for(int x=n;x>=length;x--)
				{
					coordinates.setI(rnd.nextInt(mapSize));
					coordinates.setK(rnd.nextInt(mapSize));
					coordinates.setDirection(rnd.nextBoolean());
					if(g.checkShipCoordinates(coordinates,length, super.getId()))
					{
						g.setShip(coordinates,length, super.getId());
						checker=0;
					}
					else
					{
						x++;
						checker++;
						//System.out.println("checker: "+checker);
					}
					if(checker>mapSize*mapSize)
					{
						x=length-1;
						length=0;
						repeat=true;
						System.out.println("checker>mapSize*mapSize: ch="+checker+"  S="+mapSize*mapSize);
					}
				}
			}
		}
	}

	public void setGame(Game g)
	{
		this.g=g;
		mapSize=g.getMapSize();
		mapEnemy=new int[mapSize][mapSize];

		for(int i=0;i<mapSize;i++)
			for(int k=0;k<mapSize;k++)
				mapEnemy[i][k]=0;

		shipMap=g.getCheckMap(super.getId());
		addShip();

		lastFireCells=new int[mapSize][3];
	}

	public boolean nextStep()
	{
		int i=0, k=0;
		BooleanArray b=new BooleanArray();

		if(indForLastFireCells==0)
		{
			i=rnd.nextInt(mapSize);
			k=rnd.nextInt(mapSize);
		}
		else
			for(int ii=0;ii<indForLastFireCells;ii++)
			{
				int d=lastFireCells[ii][2];
				i=lastFireCells[ii][0];
				k=lastFireCells[ii][1];

				if(d!=0)
				{
					d=checkLastFireCells(i, k, d);
					if((d&0x1000)!=0)
					{
						i--;
						d=d&0x0111;//not up
					}
					else if((d&0x0100)!=0)
					{
						i++;
						d=d&0x1011;//not down
					}
					else if((d&0x0010)!=0)
					{
						k++;
						d=d&0x1101;//not right
					}
					else if((d&0x0001)!=0)
					{
						k--;
						d=d&0x1110;//not left
					}
					lastFireCells[ii][2]&=d;
					break;
				}
			}
		//System.out.println(i+" "+k);
		if(mapEnemy[i][k]==0)
		{
			b=g.fire(i, k, super.getId());
			if(b.isShotSuccessful())
			{
				mapEnemy[i][k]=1;
				addToEnemyMap(i, k);
				addToLastFireCells(i, k);
				if(b.isShipDown())
				{
					checkDownShip(i, k);
					resetLastFireCells();
				}
				return true;
			}
			else
			{
				mapEnemy[i][k]=2;
				return false;
			}
		}
		else
			return nextStep();
	}


	public void checkDownShip(int i, int k)
	{
		int[] c=g.getShip(i, k, super.getId()).getPos();
		int di=0;
		int dk=0;

		i=c[0];
		k=c[1];
		int length=c[2];
		int direction=c[3];

		if(direction==1)
			di=1;
		else
			dk=1;

		for(int x=0;x<length;x++)
		{
			if(i!=0)
			{
				if(k!=0)
					mapEnemy[i-1][k-1]=1;
				if(k!=mapSize-1)
					mapEnemy[i-1][k+1]=1;
				mapEnemy[i-1][k]=1;
			}
			if(i!=mapSize-1)
			{
				if(k!=0)
					mapEnemy[i+1][k-1]=1;
				if(k!=mapSize-1)
					mapEnemy[i+1][k+1]=1;
				mapEnemy[i+1][k]=1;
			}
			if(k!=0)
				mapEnemy[i][k-1]=1;
			if(k!=mapSize-1)
				mapEnemy[i][k+1]=1;
			mapEnemy[i][k]=1;

			i+=di;
			k+=dk;
		}
	}


	int getZone(int i, int k)
	{
		int n=0;
//find zone where there is point A(i,k)
		if(i==0)
			n=0;
		else if(i==mapSize-1)
			n=6;
		else
			n=3;                                //      zones: n=
//                                              //  ===================
		if(k==0)                                //  = 0 =    1    = 2 =
			n+=0;                               //  ===================
		else if(k==mapSize-1)                   //  =   =         =   =
			n+=2;                               //  = 3 =    4    = 5 =
		else                                    //  =   =         =   =
			n+=1;                               //  ===================
		//System.out.println("n: "+n);          //  = 6 =    7    = 8 =
		return n;                               //  ===================
	}

	void addToEnemyMap(int i, int k)
	{
		switch(getZone(i, k))
		{
			case 0:
			{
				mapEnemy[i+1][k+1]=2;
				break;
			}
			case 1:
			{
				mapEnemy[i+1][k-1]=2;
				mapEnemy[i+1][k+1]=2;
				break;
			}
			case 2:
			{
				mapEnemy[i+1][k-1]=2;
				break;
			}
			case 3:
			{
				mapEnemy[i-1][k+1]=2;
				mapEnemy[i+1][k+1]=2;
				break;
			}
			case 4:
			{
				mapEnemy[i-1][k-1]=2;
				mapEnemy[i-1][k+1]=2;
				mapEnemy[i+1][k-1]=2;
				mapEnemy[i+1][k+1]=2;
				break;
			}
			case 5:
			{
				mapEnemy[i-1][k-1]=2;
				mapEnemy[i+1][k-1]=2;
				break;
			}
			case 6:
			{
				mapEnemy[i-1][k+1]=2;
				break;
			}
			case 7:
			{
				mapEnemy[i-1][k-1]=2;
				mapEnemy[i-1][k+1]=2;
				break;
			}
			case 8:
			{
				mapEnemy[i-1][k-1]=2;
				break;
			}
		}
	}

	void addToLastFireCells(int i, int k)
	{
		int ind=indForLastFireCells;
		int d=0x1111;
		lastFireCells[ind][0]=i;
		lastFireCells[ind][1]=k;
		lastFireCells[ind][2]=0x1111;   //UDRL  (U=up)

		int n=getZone(i, k);

		int dk=n%3;
		int di=(n-dk)/3;

		if(di==0)
			d=d&0x0111;//not up
		if(di==2)
			d=d&0x1011;//not down
		if(dk==0)
			d=d&0x1110;//not left
		if(dk==2)
			d=d&0x1101;//not right

		if(d!=0)
			d=checkLastFireCells(i, k, d);

		lastFireCells[ind][2]&=d;
		indForLastFireCells++;
	}

	int checkLastFireCells(int i, int k, int d)
	{
		//System.out.println("ik from check: "+i+" "+k);
		if((d&0x1000)!=0)
			if(mapEnemy[i-1][k]!=0)
				d=d&0x0111;//not up
		if((d&0x0100)!=0)
			if(mapEnemy[i+1][k]!=0)
				d=d&0x1011;//not down
		if((d&0x0010)!=0)
			if(mapEnemy[i][k+1]!=0)
				d=d&0x1101;//not right
		if((d&0x0001)!=0)
			if(mapEnemy[i][k-1]!=0)
				d=d&0x1110;//not left
		return d;
	}

	void resetLastFireCells()
	{
		lastFireCells=null;
		lastFireCells=new int[g.getMapSize()][3];
		indForLastFireCells=0;
	}


	private int[][] mapEnemy;
	private int[][] shipMap;
	private int mapSize;
	private int difficulty;
	private Game g;
	private Random rnd;
	private int[][] lastFireCells;
	private int indForLastFireCells;
}
