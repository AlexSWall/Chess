package search;

public class AStarFunction implements NodeFunction
{
	public final NodeFunction heuristicFunction;

	public AStarFunction( NodeFunction heuristicFunction )
	{
		this.heuristicFunction = heuristicFunction;
	}

	public int getValueOf( Node node )
	{
		return node.g + heuristicFunction.getValueOf( node );
	}
}
