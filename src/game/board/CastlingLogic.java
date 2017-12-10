package game.board;

import java.util.Set;

import game.GameSettings;
import game.pieces.King;
import game.pieces.Piece;
import game.pieces.Rook;

public class CastlingLogic
{
	private Board			board;
	@SuppressWarnings ( "unused" )
	private GameSettings	settings;

	private static final int[][] castlingDeltas = { { 2, 0 }, { -2, 0 } };

	public CastlingLogic( Board board, GameSettings settings )
	{
		this.board = board;
		this.settings = settings;
	}

	public boolean isAttemptedCastlingMove ( Piece piece, int newX, int newY )
	{
		if ( piece.getClass() == King.class )
		{
			if ( ( Math.abs( newX - piece.getX() ) == 1 && newY == piece.getY() ) )
			{
				if ( !board.hasPiece( newX, newY ) || !( board.getPiece( newX, newY ).getClass() == Rook.class ) )
					return false;
				if ( newX != 0 && newX != board.getRowLength( newY ) - 1 )
					return false;
				return true;
			}
			else if ( ( Math.abs( newX - piece.getX() ) == 2 || newY == piece.getY() ) )
				return true;
		}
		return false;
	}

	public void addPossibleCastlingMoves ( King king, Set<Position> possibleMoves )
	{
		for ( int[] castlingDelta : castlingDeltas )
			if ( canCastleToDelta( king, castlingDelta[ 0 ], castlingDelta[ 1 ] ) )
			{
				possibleMoves.add( new Position( king.getX() + castlingDelta[ 0 ], king.getY() + castlingDelta[ 1 ] ) );
			}
	}

	private boolean canCastleToDelta ( King king, int dx, int dy )
	{
		// King must not have moved.
		if ( king.hasMoved() )
			return false;
		// King cannot be in check.
		if ( board.getMovementLogic().colourInSquareIsAttacked( king.getColour(), king.getX(), king.getY() ) )
			return false;

		if ( board.getGameSettings().type == BoardSetupType.STANDARD )
			return canStandardChessCastle( king, dx, dy );
		else if ( board.getGameSettings().type == BoardSetupType.CHESS960 )
			return canChess960Castle( king, dx, dy );
		else
			return false;
	}

	private boolean canStandardChessCastle ( King king, int dx, int dy )
	{
		boolean kingsideCastle = ( dx == 2 );
		int middleX;
		Piece piece;

		if ( kingsideCastle )
		{
			if ( !board.hasPiece( 7, king.getY() ) )
				return false;

			piece = board.getPiece( 7, king.getY() );

			if ( piece.getClass() != Rook.class || piece.getColour() != king.getColour() || piece.hasMoved() )
				return false;

			for ( int distance = 1; distance <= 2; distance++ )
			{
				middleX = king.getX() + distance;
				if ( board.hasPiece( middleX, king.getY() ) )
					return false;
				if ( board.getMovementLogic().colourInSquareIsAttacked( king.getColour(), middleX, king.getY() ) )
					return false;
			}

			return true;
		}
		else // Queenside Castle
		{
			if ( !board.hasPiece( 0, king.getY() ) )
				return false;

			piece = board.getPiece( 0, king.getY() );

			if ( piece.getClass() != Rook.class || piece.getColour() != king.getColour() || piece.hasMoved() )
				return false;

			for ( int distance = 1; distance <= 2; distance++ )
			{
				middleX = king.getX() - distance;
				if ( board.hasPiece( middleX, king.getY() ) )
					return false;
				if ( board.getMovementLogic().colourInSquareIsAttacked( king.getColour(), middleX, king.getY() ) )
					return false;
			}

			if ( board.hasPiece( king.getX() - 3, king.getY() ) )
				return false;

			return true;
		}
	}

	protected boolean canChess960Castle ( King king, int dx, int dy )
	{
		// In the direction must be a rook of the same colour which hasn't moved.
		if ( getCastlingRookInDirection( king, dx, dy ) == null )
			return false;

		// The king cannot be in check, move through check, or end in check.
		if ( !castlingMovementAvoidsCheck( king, dx, dy ) )
			return false;

		return true;
	}

	public Rook getCastlingRookInDirection ( King king, int dx, int dy )
	{
		int middleX, middleY;
		Piece nextPieceInPath;

		for ( int distance = 1;; distance++ ) // Increase distance until we find the rook.
		{
			middleX = king.getX() + distance * Integer.signum( dx );
			middleY = king.getY() + distance * Integer.signum( dy );

			if ( !board.isASquare( middleX, middleY ) ) // No rook found.
				return null;

			if ( board.hasPiece( middleX, middleY ) ) // This piece must be a rook of the same colour which has not moved.
			{
				nextPieceInPath = board.getPiece( middleX, middleY );

				if ( nextPieceInPath.getClass() != Rook.class )
					return null;
				else if ( nextPieceInPath.getColour() != king.getColour() )
					return null;
				else if ( board.getPiece( middleX, middleY ).hasMoved() )
					return null;
				else
					return (Rook) nextPieceInPath;
			}
		}
	}

	public boolean castlingMovementAvoidsCheck ( King king, int dx, int dy )
	{
		@SuppressWarnings ( "unused" ) // TODO
		int middleX, middleY;

		for ( int distance = 0; distance <= 2; distance++ ) // Increase distance until we find the rook.
		{
			middleX = king.getX() + distance * Integer.signum( dx );
			middleY = king.getY() + distance * Integer.signum( dy );
		}
		return false;
	}
}
