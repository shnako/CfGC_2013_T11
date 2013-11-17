import java.util.ArrayList;
import java.util.Collections;

public class Distribute
{
	static ArrayList<Kitchen> k;
	static ArrayList<Node> n;
	static ArrayList<Route> r;
	
	public static void main(String[] args)
	{
		int kIndex = 0;
		int nIndex = 0;
		
		k = new ArrayList<Kitchen>();
		n = new ArrayList<Node>();
		r = new ArrayList<Route>();
		
		k.add(new Kitchen(5,10,kIndex++));
		k.add(new Kitchen(15,10,kIndex++));
		
		n.add(new Node(3,11,nIndex++));
		n.add(new Node(3,8,nIndex++));
		n.add(new Node(7,11,nIndex++));
		n.add(new Node(8,10,nIndex++));
		n.add(new Node(15,13,nIndex++));
		n.add(new Node(20,12,nIndex++));
		
		for (Node node:n)
		{
			for (Kitchen kit:k)
			{
				node.dist.add(Math.sqrt((kit.X-node.X)*(kit.X-node.X)+(kit.Y-node.Y)*(kit.Y-node.Y)));
			}
		}
		
		for (Node node:n)
		{
			node.assigned = node.dist.indexOf(Collections.min(node.dist));
			//System.out.println(node.index + " " + node.assigned);
		}
		
		for (Kitchen kit:k)
		{
			System.out.println();
			
			System.out.println("Moving to kitchen "+kit.index);
			
			System.out.println();
			
			while (allNodesVisited(kit.index))
			{
				int nindex = findClosest(kit.index);
				Route route = new Route();
				route.locationIndices.add(kit.index);
				addNodeToRoute(nindex, kit.index, route);
				r.add(route);
				addNextNode(nindex, kit.index, route);
			
				for (Integer i:route.locationIndices)
				{
					System.out.print(i+", ");
				}
				
				System.out.println();
				/*
				while (MapQuery.time(route) > 120-(10*route.locationIndices.size()-1))
				{
					unvisitNode(route.locationIndices.size()-1, route);
				}
				*/
			}
			
		}
		/*
		for (Route route:r)
		{
			for (Integer i:route.locationIndices)
			{
				System.out.print(i+", ");
			}
			System.out.println();
		}*/
	}
	
	public static int findClosest(int kindex)
	{
		int nindex = -1;
		double minDist = Double.MAX_VALUE;
		for (Node node:n)
			if (node.assigned==kindex)
				if ((double)node.dist.get(kindex) < minDist && !node.visited)
				{
					minDist = (double)node.dist.get(kindex);
					nindex = node.index;
				}
		n.get(nindex).visited=true;
		return nindex;
	}
	
	public static void addNextNode(int nindex, int kindex, Route route)
	{
		int nextNodeIndex = -1;
		double minDist = Double.MAX_VALUE;
		for (Node node:n)
			if (node.assigned==kindex && !node.visited)
				if (Math.sqrt((n.get(nindex).X-node.X)*(n.get(nindex).X-node.X)+(n.get(nindex).Y-node.Y)*(n.get(nindex).Y-node.Y)) < minDist)
				{
					nextNodeIndex = node.index;
					minDist = Math.sqrt((n.get(nindex).X-node.X)*(n.get(nindex).X-node.X)+(n.get(nindex).Y-node.Y)*(n.get(nindex).Y-node.Y));
				}
		if (nextNodeIndex != -1 && route.deliverables-n.get(nextNodeIndex).demand >= 0)
		{
			addNodeToRoute(nextNodeIndex, kindex, route);
			addNextNode(nextNodeIndex, kindex, route);
		}
	}
	
	public static boolean allNodesVisited(int kindex)
	{
		boolean visited = false;
		for (Node node:n)
		{
			if (!node.visited && node.assigned == kindex)
				visited = true;
		}
		
		return visited;
	}
	
	public static void addNodeToRoute(int nextNodeIndex,int kindex, Route route)
	{
		n.get(nextNodeIndex).visited=true;
		route.locationIndices.add(nextNodeIndex);
		route.deliverables-=n.get(nextNodeIndex).demand;
//		route.time+=Math.floor((double)(n.get(nextNodeIndex).dist.get(kindex)));
	}
	
	public static void unvisitNode(int nindex, Route route)
	{
		n.get(nindex).visited=false;
		route.locationIndices.remove(route.locationIndices.indexOf(nindex));
		route.deliverables+=n.get(nindex).demand;
//		route.time-=Math.floor((double)(n.get(nindex).dist.get(kindex)));
	}
}
