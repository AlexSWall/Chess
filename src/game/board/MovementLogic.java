package game.board;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

import game.board.promotion.PromotionChoice;
import game.board.promotion.PromotionListener;
import game.modes.TwoPlayerColour;
import game.pieces.King;
import game.pieces.Pawn;
import game.pieces.Piece;
import game.pieces.Queen;

public class MovementLogic
{
	private final Board				board;
	private final CastlingLogic		castlingLogic;
	private final PromotionListener	listener;

	private ReversibleMover reversibleMover;

	private TwoPlayerColour								playerTurn;
	private Function<TwoPlayerColour, TwoPlayerColour>	nextTurnFunc;

	private Piece	pieceJustMoved	= null;
	private boolean	pawnJustJumped	= false;

	public MovementLogic( Board board, BoardSetup setup, CastlingLogic castlingLogic, PromotionListener listener )
	{
		this.board = board;
		this.castlingLogic = castlingLogic;
		this.listener = listener;

		reversibleMover = new ReversibleMover( board );

		playerTurn = setup.getPlayerTurn();
		nextTurnFunc = setup.getNextTurnFunction();
	}

	public TwoPlayerColour getPlayerTurn ()
	{
		return playerTurn;
	}

	/**
	 * This function is called by the GUI when it is informing of an attempt to move a piece to a square.
	 *
	 * @param piece The piece being moved.
	 * @param newX The x-coordinate of the destination.
	 * @param newY The y-coordinate of the destination.
	 */
	public void tryToMovePiece ( Piece piece, int newX, int newY )
	{
		int oldY = piece.getY();

		if ( piece.getColour() != playerTurn )
			return;

		if ( !piece.isMove( newX, newY ) )
			return;

		boolean isAttemptingCastling = castlingLogic.isAttemptedCastlingMove( piece, newX, newY );
		boolean isAttemptingEnPassant = piece instanceof Pawn && ( (Pawn) piece ).canEnPassant( newX, newY );
		reversibleMover.doMove( piece, newX, newY, isAttemptingCastling, isAttemptingEnPassant );

		if ( isKingInCheck( piece.getColour() ) )
		{
			reversibleMover.reverseLastMove();
			return;
		}

		if ( piece instanceof Pawn )
		{
			Pawn pawn = (Pawn) piece;
			if ( pawn.canPromote() )
			{
				listener.promote( pawn, PromotionChoice.CHOOSING );
			}
		}

		pieceJustMoved = piece;
		pawnJustJumped = pieceJustMoved instanceof Pawn && Math.abs( newY - oldY ) == 2;
		TwoPlayerColour nextPlayersTurn = nextTurnFunc.apply( piece.getColour() );

		if ( !playerCanMove( nextPlayersTurn ) )
		{
			board.gameWonFirer.fireGameWonEvent( isKingInCheck( playerTurn ) ? piece.getColour() : null );
		}
		else
		{
			playerTurn = nextPlayersTurn;
		}
	}

	public boolean canTakePassingPawn ( Piece piece )
	{
		return piece.equals( pieceJustMoved ) && pawnJustJumped;
	}

	public boolean isKingInCheck ( TwoPlayerColour colour )
	{
		for ( King king : board.getPlayersKings().get( colour ) )
		{
			if ( colourInSquareIsAttacked( king.getColour(), king.getX(), king.getY() ) )
				return true;
		}
		return false;
	}

	public boolean colourInSquareIsAttacked ( TwoPlayerColour colourToAttack, int x, int y )
	{
		for ( Piece piece : board.getPieces() )
		{
			if ( piece instanceof Queen )
			{
				piece.toString();
			}
			if ( piece.getColour() != colourToAttack )
			{
				if ( piece.getClass() == King.class )
				{
					if ( ( (King) piece ).isAdjacentTo( x, y ) )
						return true;
				}
				else if ( piece.isMove( x, y ) )
					return true;
			}
		}
		return false;
	}

	private boolean playerCanMove ( TwoPlayerColour playerColour )
	{
		Set<Piece> currentPieces = new HashSet<Piece>( board.getPieces() );
		boolean attemptingCastling;
		boolean attemptingEnPassant;

		for ( Piece piece : currentPieces )
		{
			if ( piece.getColour() == playerColour )
			{
				for ( Position movePos : piece.getPossibleMoves() )
				{
					attemptingCastling = castlingLogic.isAttemptedCastlingMove( piece, movePos.x, movePos.y );
					attemptingEnPassant = piece instanceof Pawn && ( (Pawn) piece ).canEnPassant( movePos.x, movePos.y );

					reversibleMover.doMove( piece, movePos.x, movePos.y, attemptingCastling, attemptingEnPassant );

					if ( !isKingInCheck( playerColour ) )
					{
						reversibleMover.reverseLastMove();
						return true;
					}

					reversibleMover.reverseLastMove();
				}
			}
		}

		return false;
	}

	public void promote ( Pawn pawn, PromotionChoice choice )
	{
		Piece newPiece = choice.getPromotionPiece( board, pawn );
		board.getSquare( pawn.getX(), pawn.getY() ).setPiece( newPiece );
		board.getPieces().remove( pawn );
		board.getPieces().add( newPiece );
	}
}
