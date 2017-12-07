package search;

import java.util.LinkedList;

public class DepthFirstFrontier implements Frontier
{
	private LinkedList<Node> frontier;
	private int maxFrontierSize;

	public DepthFirstFrontier()
	{
		frontier = new LinkedList<Node>();
		maxFrontierSize = 0;
	}

	public DepthFirstFrontier( LinkedList<? extends Node> frontier )
	{
		this.frontier = new LinkedList<Node>( frontier );
	}

	public void addNode( Node node )
	{
		frontier.addFirst( node );

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
