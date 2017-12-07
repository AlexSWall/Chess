package search;

import java.util.LinkedList;

public class BreadthFirstFrontier implements Frontier
{
	private LinkedList<Node> frontier;
	private int maxFrontierSize;

	public BreadthFirstFrontier()
	{
		frontier = new LinkedList<Node>();
		maxFrontierSize = 0;
	}

	public BreadthFirstFrontier( LinkedList<? extends Node> frontier )
	{
		this.frontier = new LinkedList<Node>( frontier );
	}

	public void addNode( Node node )
	{
		frontier.addLast( node );

		if ( frontier.size() > maxFrontierSize )
			maxFrontierSize = frontier.size();
	}

	public void clearFrontier()
	{
		frontier.clear();
	}

	public boolean isEmpty()
	{
		return frontier.isEmpty();
	}

	public Node removeNode()
	{
		return frontier.remove();
	}

	public int maxFrontierSize()
	{
		return maxFrontierSize;
	}
}
