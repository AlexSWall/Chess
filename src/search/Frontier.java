package search;

public interface Frontier
{
	void addNode(Node node);
	void clearFrontier();
	boolean isEmpty();
	Node removeNode();
	int maxFrontierSize();
}
