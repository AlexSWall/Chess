package search;

import java.util.PriorityQueue;

public class BestFirstFrontier implements Frontier
{
	private NodeFunction nodeValueFunc;
	private PriorityQueue<Node> priQueue;
	private int maxFrontierSize;

	public BestFirstFrontier( NodeFunction nodeValueFunc )
	{
		this.nodeValueFunc = nodeValueFunc;
		this.priQueue = new PriorityQueue<Node>();
		this.maxFrontierSize = 0;
	}

	public void addNode( Node node )
	{
		node.f = nodeValueFunc.getValueOf( node );
		priQueue.add( node );

		if ( priQueue.size() > maxFrontierSize )
			maxFrontierSize = priQueue.size();
	}

	public void clearFrontier()
	{
		priQueue.clear();
	}

	public boolean isEmpty()
	{
		return priQueue.isEmpty();
	}

	public Node removeNode()
	{
		return priQueue.poll();
	}

	public int maxFrontierSize()
	{
		return maxFrontierSize;
	}
}
