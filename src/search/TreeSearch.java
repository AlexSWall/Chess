package search;

public class TreeSearch implements Search
{
	public Frontier frontier;
	private int nodeGenerationCount;

	public TreeSearch( Frontier frontier )
	{
		this.frontier = frontier;
	}

	public Node search( Node root, GoalTest goal )
	{
		Node node;
		State newState;

		nodeGenerationCount = 0;
		frontier.addNode( root );

		while ( !frontier.isEmpty() && !Thread.interrupted() )
		{
			node = frontier.removeNode();

			if ( goal.isGoal( node.state ) )
				return node;

			nodeGenerationCount++;

			if ( !goal.isFutureGoalPossible( node.state ) )
				continue;

			for ( Action action : node.state.getApplicableActions() )
			{
				newState = node.state.getActionResult( action );

				frontier.addNode( new Node( node, action, newState ) );
			}
		}

		return null;
	}

	public int nodeGenerationCount()
	{
		return nodeGenerationCount;
	}
}
