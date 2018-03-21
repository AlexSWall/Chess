package game.board;

import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.swing.event.EventListenerList;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import game.GameSettings;
import game.GameWonListener;
import game.board.promotion.PromotionChoice;
import game.board.promotion.PromotionListener;
import game.modes.TwoPlayerColour;
import game.pieces.King;
import game.pieces.Pawn;
import game.pieces.Piece;

public class Board
{
	public final GameWonFirer gameWonFirer = new GameWonFirer();

	private GameSettings	settings;
	private MovementLogic	movementLogic;
	private CastlingLogic	castlingLogic;

	private Square[][]						squares;
	private Set<Piece>						pieces;
	private Map<TwoPlayerColour, Set<King>>	playersKings;
	private TwoPlayerColour					humanPlayerColour;

	private PromotionListener listener;

	public Image boardImage;

	public Board()
	{
	}

	public void init () throws SlickException
	{
		String menuOptionsPath = Paths.get( "resources", "main" ).toString();
		boardImage = new Image( Paths.get( menuOptionsPath, "ChessBoard.PNG" ).toString() );
		System.out.println( "Initialising Board." );
		pieces = new HashSet<Piece>();
	}

	public void setup ( GameSettings settings )
	{
		System.out.println( "Setting Up Board." );
		this.settings = settings;

		BoardSetup setup = new BoardSetup( this, settings );
		setup.setupBoard();

		castlingLogic = new CastlingLogic( this, settings );
		movementLogic = new MovementLogic( this, setup, castlingLogic, listener );

		humanPlayerColour = settings.playerColour;
		squares = setup.getNewBoardSquares();
		pieces = setup.getPieces();
		playersKings = setup.getPlayerKingsMap();
	}

	public GameSettings getGameSettings ()
	{
		return settings;
	}

	public Set<Piece> getPieces ()
	{
		return pieces;
	}

	public TwoPlayerColour getHumanPlayerColour ()
	{
		return humanPlayerColour;
	}

	public void setPromotionListener ( PromotionListener listener )
	{
		this.listener = listener;
	}

	public boolean isASquare ( int x, int y )
	{
		return ( x >= 0 && y >= 0 && y < squares.length && x < squares[ y ].length );
	}

	public boolean isEmptySquare ( int x, int y )
	{
		return isASquare( x, y ) && getPiece( x, y ) == null;
	}

	public boolean hasPiece ( int x, int y )
	{
		return isASquare( x, y ) && getPiece( x, y ) != null;
	}

	public Piece getPiece ( int x, int y )
	{
		return squares[ y ][ x ].getPiece();
	}

	public void setPiece ( Piece piece, int x, int y )
	{
		squares[ y ][ x ].setPiece( piece );
	}

	public Square getSquare ( int x, int y )
	{
		return squares[ y ][ x ];
	}

	public void promote ( Pawn pawn, PromotionChoice choice )
	{
		movementLogic.promote( pawn, choice );
	}

	public int getRowLength ( int row )
	{
		return squares[ row ].length;
	}

	public int getColumnLength ( int column )
	{
		return squares.length;
	}

	public Map<TwoPlayerColour, Set<King>> getPlayersKings ()
	{
		return playersKings;
	}

	public MovementLogic getMovementLogic ()
	{
		return movementLogic;
	}

	public CastlingLogic getCastlingLogic ()
	{
		return castlingLogic;
	}

	public static class GameWonFirer
	{
		EventListenerList gameWonListenerList = new EventListenerList();

		public final void addGameWonListener ( GameWonListener listener )
		{
			gameWonListenerList.add( GameWonListener.class, listener );
		}

		public final void removeGameWonListener ( GameWonListener listener )
		{
			gameWonListenerList.remove( GameWonListener.class, listener );
		}

		final void fireGameWonEvent ( TwoPlayerColour winningColour )
		{
			Object[] listeners = gameWonListenerList.getListenerList();

			for ( int i = 0; i < listeners.length; i += 2 )
			{
				if ( listeners[ i ] == GameWonListener.class )
				{
					( (GameWonListener) listeners[ i + 1 ] ).gameWon( winningColour );
				}
			}
		}
	}
}
