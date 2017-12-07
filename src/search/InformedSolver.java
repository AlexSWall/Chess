package search;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class InformedSolver
{
	public final NodeFunction heuristicFunction;

	public InformedSolver( NodeFunction heuristicFunction )
	{
		this.heuristicFunction = heuristicFunction;
	}

	public void runAndPrintAllSearches ( Node startNode, GoalTest goalTest, int timeoutInMillis )
	{
		// === A* (Best First) Tree Search ===
		Frontier BFTS_Frontier = new BestFirstFrontier( new AStarFunction( heuristicFunction ) );
		Search BFTS_Searcher = new TreeSearch( BFTS_Frontier );
		Node BFTS_Solution = runSearch( BFTS_Searcher, startNode, goalTest, timeoutInMillis );

		// === A* (Best First) Graph Search ===
		Frontier BFGS_Frontier = new BestFirstFrontier( new AStarFunction( heuristicFunction ) );
		Search BFGS_Searcher = new GraphSearch( BFGS_Frontier );
		Node BFGS_Solution = runSearch( BFGS_Searcher, startNode, goalTest, timeoutInMillis );

		printSolutionDetails( "A* (Best First) Tree Search", BFTS_Searcher, BFTS_Frontier, BFTS_Solution );
		printSolutionDetails( "A* (Best First) Graph Search", BFGS_Searcher, BFGS_Frontier, BFGS_Solution );
	}

	private static Node runSearch ( Search searcher, Node startNode, GoalTest goalTest, int timeoutInMillis )
	{
		ExecutorService executor = Executors.newSingleThreadExecutor();
		Future<Node> future = executor.submit( new InformedSearchRunner( searcher, startNode, goalTest ) );

		Node solution = null;

		try
		{
			solution = future.get( timeoutInMillis, TimeUnit.MILLISECONDS );
		}
		catch ( TimeoutException | InterruptedException | ExecutionException e )
		{
		}

		future.cancel( true );
		executor.shutdownNow();

		return solution;
	}

	private static void printSolutionDetails ( String searchType, Search search, Frontier frontier, Node solution )
	{
		System.out.println( "==== " + searchType + " ====" );
		System.out.println( "Solution Found: " + ( solution != null ) );
		System.out.println( "Nodes Generated: " + search.nodeGenerationCount() + "\nMaximum Size of Frontier: " + frontier.maxFrontierSize() );
	}
}

class InformedSearchRunner
		implements Callable<Node>
{

	private Search		searcher;
	private Node		startNode;
	private GoalTest	goalTest;

	InformedSearchRunner( Search searcher, Node startNode, GoalTest goalTest )
	{
		this.searcher = searcher;
		this.startNode = startNode;
		this.goalTest = goalTest;
	}

	@Override
	public Node call () throws Exception
	{
		return searcher.search( startNode, goalTest );
	}
}
