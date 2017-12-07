package search;

public interface Search
{
	Node search( Node root, GoalTest goal );
	int nodeGenerationCount();
}
