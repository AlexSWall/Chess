package game.board;

import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import javax.swing.event.EventListenerList;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import game.GameSettings;
import game.GameWonListener;
import game.modes.TwoPlayerColour;
import game.pieces.King;
import game.pieces.Piece;

public class Board
{
	private ReversibleMover		reversibleMover;
	public final GameWonFirer	gameWonFirer;

	private TwoPlayerColour	colourAtBottom;
	private Square[][]		squares;
	private Set<Piece>		pieces	= new HashSet<Piece>();
	private TwoPlayerColour	playerTurn;

	private Map<TwoPlayerColour, Set<King>> playersKings = new HashMap<TwoPlayerColour, Set<King>>();

	private Function<TwoPlayerColour, TwoPlayerColour> nextTurnFunc;

	public Image boardImage;

	public Board()
	{
		reversibleMover = new ReversibleMover();
		gameWonFirer = new GameWonFirer();
	}

	public void init () throws SlickException
	{
		String menuOptionsPath = Paths.get( "resources", "main" ).toString();
		boardImage = new Image( Paths.get( menuOptionsPath, "ChessBoard.PNG" ).toString() );
	}

	public void setup ( GameSettings settings )
	{
		BoardSetup setup = new BoardSetup( this, settings );

		setup.setupBoard();
		playerTurn = setup.getPlayerTurn();
		nextTurnFunc = setup.getNextTurnFunction();
		colourAtBottom = setup.getColourAtBottom();
		squares = setup.getNewBoardSquares();

		updatePiecesSet();
	}

	private void updatePiecesSet ()
	{
		pieces = new HashSet<Piece>();

		for ( Square[] squareRow : squares )
		{
			for ( Square square : squareRow )
			{
				if ( square.getPiece() != null )
				{
					pieces.add( square.getPiece() );
				}
			}
		}
	}

	public Set<Piece> getPieces ()
	{
		return pieces;
	}

	public TwoPlayerColour getColourAtBottom ()
	{
		return colourAtBottom;
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

	public Piece getPotentialPiece ( int x, int y )
	{
		return isASquare( x, y ) ? getPiece( x, y ) : null;
	}

	public Piece getPiece ( int x, int y )
	{
		return squares[ y ][ x ].getPiece();
	}

	private void setPiece ( Piece piece, int x, int y )
	{
		squares[ y ][ x ].setPiece( piece );
	}

	public void tryToMovePiece ( Piece piece, int newX, int newY )
	{
		if ( piece.getColour() != playerTurn )
			return;

		if ( !piece.canMoveTo( newX, newY ) )
			return;

		reversibleMover.doMove( piece, newX, newY );

		playerTurn = nextTurnFunc.apply( piece.getColour() );

		if ( !playerCanMove( playerTurn ) )
		{
			gameWonFirer.fireGameWonEvent( piece.getColour() );
		}
	}

	public boolean colourInSquareIsAttacked ( TwoPlayerColour colourToAttack, int x, int y )
	{
		for ( Piece piece : pieces )
		{
			if ( piece.getColour() != colourToAttack )
			{
				if ( piece.isMove( x, y ) )
					return true;
			}
		}
		return false;
	}

	public boolean moveResultHasOwnKingInCheck ( Piece pieceMoving, int newX, int newY )
	{
		reversibleMover.doMove( pieceMoving, newX, newY );

		TwoPlayerColour colourMoved = pieceMoving.getColour();

		King king = null;
		Position kingPosition = null;

		for ( Piece piece : pieces )
		{
			if ( piece.getClass() == King.class && colourMoved == piece.getColour() )
			{
				king = (King) piece;
				kingPosition = new Position( king.getX(), king.getY() );
				break;
			}
		}

		for ( Piece piece : pieces )
		{
			if ( piece.getColour() != king.getColour() )
			{
				if ( piece.getPossibleMoves().contains( kingPosition ) )
				{
					reversibleMover.reverseLastMove();
					return true;
				}
			}
		}

		reversibleMover.reverseLastMove();
		return false;
	}

	private boolean playerCanMove ( TwoPlayerColour playerColour )
	{
		Set<Piece> currentPieces = new HashSet<Piece>( pieces );
		for ( Piece piece : currentPieces )
		{
			if ( piece.getColour() == playerColour )
			{
				for ( Position movePos : piece.getPossibleMoves() )
				{
					if ( !moveResultHasOwnKingInCheck( piece, movePos.x, movePos.y ) )
						return true;
				}
			}
		}

		return false;
	}

	private class ReversibleMover
	{
		private ReversalData data;

		public void doMove ( Piece piece, int newX, int newY )
		{
			data = new ReversalData( piece, newX, newY );

			pieces.remove( data.pieceTaken );

			setPiece( null, data.oldX, data.oldY );
			setPiece( data.movedPiece, data.newX, data.newY );
		}

		public void reverseLastMove ()
		{
			if ( data.pieceTaken != null )
			{
				pieces.add( data.pieceTaken );
			}

			setPiece( data.movedPiece, data.oldX, data.oldY );
			setPiece( data.pieceTaken, data.newX, data.newY );
			data.movedPiece.setHasMoved( data.hadMoved );
		}

		private class ReversalData
		{
			public final Piece		movedPiece;
			public final int		oldX;
			public final int		oldY;
			public final int		newX;
			public final int		newY;
			public final boolean	hadMoved;
			public final Piece		pieceTaken;

			public ReversalData( Piece piece, int newX, int newY )
			{
				this.movedPiece = piece;
				this.oldX = piece.getX();
				this.oldY = piece.getY();
				this.newX = newX;
				this.newY = newY;
				this.hadMoved = piece.hasMoved();
				this.pieceTaken = getPiece( newX, newY );
			}
		}
	}

	public class GameWonFirer
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

		private final void fireGameWonEvent ( TwoPlayerColour colourThatWon )
		{
			Object[] listeners = gameWonListenerList.getListenerList();

			for ( int i = 0; i < listeners.length; i += 2 )
			{
				if ( listeners[ i ] == GameWonListener.class )
				{
					( (GameWonListener) listeners[ i + 1 ] ).gameWon( colourThatWon );
				}
			}
		}
	}

}
