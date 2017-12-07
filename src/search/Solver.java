package search;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.ExecutionException;

public class Solver
{
	public Solver( Node startNode, GoalTest goalTest, int timeoutInMillis )
	{
		runAndPrintAllSearches( startNode, goalTest, timeoutInMillis );
		IterativeDeepeningTreeSearch.endSearch = true;
	}

	public static void runAndPrintAllSearches( Node startNode, GoalTest goalTest, int timeoutInMillis )
	{
//		=== BFTS ===
		Frontier BFTS_Frontier = new BreadthFirstFrontier();
		Search BFTS_Searcher = new TreeSearch( BFTS_Frontier );
		Node BFTS_Solution = runSearch( BFTS_Searcher, startNode, goalTest, timeoutInMillis );

//		=== BFGS ===
		Frontier BFGS_Frontier = new BreadthFirstFrontier();
		Search BFGS_Searcher = new GraphSearch( BFGS_Frontier );
		Node BFGS_Solution = runSearch( BFGS_Searcher, startNode, goalTest, timeoutInMillis );

//		=== DFTS ===
		Frontier DFTS_Frontier = new DepthFirstFrontier();
		Search DFTS_Searcher = new TreeSearch( DFTS_Frontier );
		Node DFTS_Solution = runSearch( DFTS_Searcher, startNode, goalTest, timeoutInMillis );

//		=== DFGS ===
		Frontier DFGS_Frontier = new DepthFirstFrontier();
		Search DFGS_Searcher = new GraphSearch( DFGS_Frontier );
		Node DFGS_Solution = runSearch( DFGS_Searcher, startNode, goalTest, timeoutInMillis );

//		=== IDTS ===
		IterativeDeepeningTreeSearch IDTS_Searcher = new IterativeDeepeningTreeSearch();
		Node IDTS_Solution = runSearch( IDTS_Searcher, startNode, goalTest, timeoutInMillis );

		printSolutionDetails( "BFTS", BFTS_Searcher, BFTS_Frontier, BFTS_Solution );
		printSolutionDetails( "BFGS", BFGS_Searcher, BFGS_Frontier, BFGS_Solution );
		printSolutionDetails( "DFTS", DFTS_Searcher, DFTS_Frontier, DFTS_Solution );
		printSolutionDetails( "DFGS", DFGS_Searcher, DFGS_Frontier, DFGS_Solution );
		printIDTSSolutionDetails( "IDTS", IDTS_Searcher, IDTS_Solution );
	}

	private static Node runSearch( Search searcher, Node startNode, GoalTest goalTest, int timeoutInMillis )
	{
		ExecutorService executor = Executors.newSingleThreadExecutor();
		Future<Node> future = executor.submit( new SearchRunner( searcher, startNode, goalTest ) );

		Node solution = null;

		try
		{
			solution = future.get( timeoutInMillis, TimeUnit.MILLISECONDS );
		}
		catch (TimeoutException | InterruptedException | ExecutionException e ) 
		{
		}

		future.cancel(true);
		executor.shutdownNow();

		return solution;
	}

	private static void printIDTSSolutionDetails( String searchType, IterativeDeepeningTreeSearch search, Node solution )
	{
		printSolutionDetails( searchType, search, search.frontier, solution );
		System.out.println( "Maximum Depth: " + search.currentMaxDepth );
	}

	private static void printSolutionDetails( String searchType, Search search, Frontier frontier, Node solution )
	{
		System.out.println( "==== " + searchType + " ====" );
		System.out.println( "Solution Found: " + ( solution != null ) );
//		if ( solution != null )
//			new NPuzzlePrinting().printSolution( solution );
	
		System.out.println( "Nodes Generated: " + search.nodeGenerationCount() + "\nMaximum Size of Frontier: " + frontier.maxFrontierSize() );
	}
}

class SearchRunner implements Callable<Node> {

	private Search searcher;
	private Node startNode;
	private GoalTest goalTest;

	SearchRunner( Search searcher, Node startNode, GoalTest goalTest )
	{
		this.searcher = searcher;
		this.startNode = startNode;
		this.goalTest = goalTest;
	}

	@Override
	public Node call() throws Exception 
	{
		return searcher.search( startNode, goalTest );
	}
}
