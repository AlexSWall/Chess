package search;

// Note: This class has a natural ordering that is inconsistent with equals.
public class Node implements Comparable<Node> {
	public final Node parent;
	public final Action action;
	public final State state;
	public final int depth;
	public int f;
	public final int g;
	
	public Node( Node parent, Action action, State state ) {
		this.parent = parent;
		this.action = action;
		this.state = state;
		this.depth = 0;

		if ( parent == null )
			this.g = 0;
		else
			this.g = parent.g + action.getCost();
	}

	public Node( Node parent, Action action, State state, int depth ) {
		this.parent = parent;
		this.action = action;
		this.state = state;
		this.depth = depth;

		if ( parent == null )
			this.g = 0;
		else
			this.g = parent.g + action.getCost();
	}

	public int compareTo( Node o )
	{
		return this.f - o.f;
	}
}
