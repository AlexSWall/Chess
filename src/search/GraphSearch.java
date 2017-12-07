package search;

import java.util.HashSet;
import java.util.Set;

public class GraphSearch implements Search
{
	public Frontier frontier;
	private int nodeGenerationCount;

	public GraphSearch( Frontier frontier )
	{
		this.frontier = frontier;
	}

	public Node search( Node root, GoalTest goal )
	{
		Node node;
		State newState;

		nodeGenerationCount = 0;
		frontier.addNode( root );
		Set<State> explored = new HashSet<State>();

		while ( !frontier.isEmpty() && !Thread.interrupted())
		{
			node = frontier.removeNode();

			if ( explored.contains( node.state ) )
				continue;

			explored.add( node.state );

			if ( goal.isGoal( node.state ) )
				return node;

			nodeGenerationCount++;

			if ( !goal.isFutureGoalPossible( node.state ) )
				continue;

			for ( Action action : node.state.getApplicableActions() )
			{
				newState = node.state.getActionResult( action );

				if ( !explored.contains( newState ) )
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
