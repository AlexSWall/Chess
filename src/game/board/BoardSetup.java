package game.board;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.function.Function;

import game.GameSettings;
import game.modes.TwoPlayerColour;
import game.pieces.Bishop;
import game.pieces.King;
import game.pieces.Knight;
import game.pieces.Pawn;
import game.pieces.Piece;
import game.pieces.Queen;
import game.pieces.Rook;

public class BoardSetup
{
	private final Board			board;
	private final GameSettings	settings;

	private Square[][]						squares;
	private TwoPlayerColour					playerTurn;
	private TwoPlayerColour					colourAtBottom;
	private Set<Piece>						pieces;
	private Map<TwoPlayerColour, Set<King>>	playersKings;

	private Function<TwoPlayerColour, TwoPlayerColour> nextTurnFunc;

	public BoardSetup( Board board, GameSettings settings )
	{
		this.board = board;
		this.settings = settings;
		setupBoard();
	}

	public Square[][] getNewBoardSquares ()
	{
		if ( settings.type == BoardSetupType.STANDARD )
		{
			standardPieceSetup();
		}
		else if ( settings.type == BoardSetupType.CHESS960 )
		{
			chess960RandomPieceSetup();
		}
		else
		{
			standardPieceSetup();
		}
		createPieceSets();
		return squares;
	}

	public Square[][] getSquares ()
	{
		return squares;
	}

	public TwoPlayerColour getPlayerTurn ()
	{
		return playerTurn;
	}

	public Set<Piece> getPieces ()
	{
		return pieces;
	}

	public Map<TwoPlayerColour, Set<King>> getPlayerKingsMap ()
	{
		return playersKings;
	}

	public Function<TwoPlayerColour, TwoPlayerColour> getNextTurnFunction ()
	{
		return nextTurnFunc;
	}

	public TwoPlayerColour getColourAtBottom ()
	{
		return colourAtBottom;
	}

	protected void setupBoard ()
	{
		this.squares = new Square[ 8 ][ 8 ];

		for ( int i = 0; i < squares.length; i++ )
		{
			for ( int j = 0; j < squares[ i ].length; j++ )
			{
				squares[ i ][ j ] = new Square( j, i );
			}
		}

		colourAtBottom = settings.playerColour;

		playerTurn = TwoPlayerColour.WHITE;

		nextTurnFunc = new Function<TwoPlayerColour, TwoPlayerColour>()
		{
			@Override
			public TwoPlayerColour apply ( TwoPlayerColour t )
			{
				return ( t == TwoPlayerColour.WHITE ? TwoPlayerColour.BLACK : TwoPlayerColour.WHITE );
			}
		};
	}

	private void createPieceSets ()
	{
		Piece piece;

		pieces = new HashSet<Piece>();
		playersKings = new HashMap<TwoPlayerColour, Set<King>>();

		for ( TwoPlayerColour colour : TwoPlayerColour.values() )
		{
			playersKings.put( colour, new HashSet<King>() );
		}

		for ( Square[] squareRow : squares )
		{
			for ( Square square : squareRow )
			{
				piece = square.getPiece();
				if ( piece == null )
				{
					continue;
				}

				pieces.add( piece );
				if ( piece.getClass() == King.class )
				{
					playersKings.get( piece.getColour() ).add( (King) piece );
				}
			}
		}
	}

	private void standardPieceSetup ()
	{
		List<Class<? extends Piece>> piecesToPlace = Arrays.asList( Rook.class, Knight.class, Bishop.class, Queen.class, King.class, Bishop.class, Knight.class, Rook.class );
		tryAddingPieces( piecesToPlace );
	}

	/**
	 * This method first places the king, then the rooks (one on each side of the king), then the bishops (on
	 * different coloured squares), then the queen randomly, and finally fills the last two squares with knights.
	 */
	private void chess960RandomPieceSetup ()
	{
		List<Class<? extends Piece>> assignedPieceLocations = new ArrayList<Class<? extends Piece>>( Collections.nCopies( 8, null ) );
		Random rand = new Random();

		int kingLoc;
		int rookLocLeft;
		int rookLocRight;
		// The following three must be initialised due to the repeat-until-success approach to placement.
		int bishopLoc1 = -1;
		int bishopLoc2 = -1;
		int queenLoc = -1;

		// Assign the king's location.
		kingLoc = 1 + rand.nextInt( 6 ); // 1 <= k < 7
		assignedPieceLocations.set( kingLoc, King.class );

		// Assign the left rook's location.
		rookLocLeft = rand.nextInt( kingLoc ); // 0 <= r1 < k
		assignedPieceLocations.set( rookLocLeft, Rook.class );

		// Assign the right rook's location.
		rookLocRight = kingLoc + 1 + rand.nextInt( 7 - kingLoc ); // k+1 <= r2 <= 7
		assignedPieceLocations.set( rookLocRight, Rook.class );

		// Assign the first bishop's location.
		for ( boolean squareEmpty = false; squareEmpty == false; )
		{
			bishopLoc1 = rand.nextInt( 8 );
			squareEmpty = assignedPieceLocations.get( bishopLoc1 ) == null;
		}
		assignedPieceLocations.set( bishopLoc1, Bishop.class );

		// Assign the second bishop's location.
		for ( boolean squareEmpty = false; squareEmpty == false; )
		{
			bishopLoc2 = 2 * rand.nextInt( 4 ) + ( ( bishopLoc1 + 1 ) % 2 ); // Ensure the square is a different colour.
			squareEmpty = assignedPieceLocations.get( bishopLoc2 ) == null;
		}
		assignedPieceLocations.set( bishopLoc2, Bishop.class );

		// Assign the queen's location.
		for ( boolean squareEmpty = false; squareEmpty == false; )
		{
			queenLoc = rand.nextInt( 8 );
			squareEmpty = assignedPieceLocations.get( queenLoc ) == null;
		}
		assignedPieceLocations.set( queenLoc, Queen.class );

		// Assign the two knights in the remaining two free positions.
		for ( int i = 0; i < 8; i++ )
		{
			if ( assignedPieceLocations.get( i ) == null )
			{
				assignedPieceLocations.set( i, Knight.class );
			}
		}

		tryAddingPieces( assignedPieceLocations );
	}

	private void tryAddingPieces ( List<Class<? extends Piece>> piecesToPlace )
	{
		try
		{
			addPieces( piecesToPlace );
		}
		catch ( Exception e )
		{
			e.printStackTrace();
		}
	}

	private void addPieces ( List<Class<? extends Piece>> piecesToPlace )
			throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException
	{
		Constructor<? extends Piece> pieceConstructor;
		Piece piece;
		int xPos, yPos;

		for ( int j = 0; j < piecesToPlace.size(); j++ )
		{
			squares[ 1 ][ j ].setPiece( new Pawn( board, settings.isWhite ? TwoPlayerColour.BLACK : TwoPlayerColour.WHITE, j, 1 ) );
			squares[ 6 ][ j ].setPiece( new Pawn( board, !settings.isWhite ? TwoPlayerColour.BLACK : TwoPlayerColour.WHITE, j, 6 ) );
		}

		for ( TwoPlayerColour colour : TwoPlayerColour.values() )
		{
			yPos = ( colour == ( settings.isWhite ? TwoPlayerColour.BLACK : TwoPlayerColour.WHITE ) ? 0 : 7 );

			for ( xPos = 0; xPos < piecesToPlace.size(); xPos++ )
			{
				pieceConstructor = piecesToPlace.get( xPos ).getConstructor( Board.class, TwoPlayerColour.class, Integer.class, Integer.class );
				piece = pieceConstructor.newInstance( new Object[] { board, colour, Integer.valueOf( xPos ), Integer.valueOf( yPos ) } );

				squares[ yPos ][ xPos ].setPiece( piece );
			}
		}
	}
}
