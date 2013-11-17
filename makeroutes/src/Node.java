import java.util.ArrayList;

public class Node
{
	public float X;
	public float Y;
	public int index;
	public ArrayList dist = new ArrayList();
	public int assigned = -1;
	public boolean visited;
	public int demand = 3;
	
	public Node(float X, float Y, int index)
	{
		this.X = X;
		this.Y = Y;
		this.index = index;
		visited = false;
	}
}
