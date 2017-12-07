package search;

public class IterativeDeepeningTreeSearch implements Search
{
	public static boolean endSearch = false;

	public Frontier frontier;
	private int nodeGenerationCount;
	public int currentMaxDepth;

	public IterativeDeepeningTreeSearch()
	{
		this.frontier = new DepthFirstFrontier();
	}

	public Node search( Node root, GoalTest goal )
	{
		Node node;
		State newState;

		nodeGenerationCount = 0;
		endSearch = false;

		for ( currentMaxDepth = 1; currentMaxDepth < Integer.MAX_VALUE && !Thread.interrupted() && !endSearch; currentMaxDepth++ )
		{
			frontier.addNode( root );

			while ( !frontier.isEmpty() && !Thread.interrupted() && !endSearch )
			{
				node = frontier.removeNode();

				if ( goal.isGoal( node.state ) )
					return node;

				if ( node.depth >= currentMaxDepth )
					continue;

				nodeGenerationCount++;

				for ( Action action : node.state.getApplicableActions() )
				{
					newState = node.state.getActionResult( action );

					frontier.addNode( new Node( node, action, newState, node.depth + 1 ) );
				}
			}
		}

		return null;
	}

	public int nodeGenerationCount()
	{
		return nodeGenerationCount;
	}
}
