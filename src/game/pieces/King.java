package game.pieces;

import java.util.HashSet;
import java.util.Set;

import game.board.Board;
import game.board.Position;
import game.modes.TwoPlayerColour;

public class King extends Piece
{
	private static final int[][]	deltaPopPlaces	= { { 0, 1 }, { 1, 1 }, { 1, 0 }, { 1, -1 }, { 0, -1 }, { -1, -1 }, { -1, 0 }, { -1, 1 } };
	private static final int[][]	castlingDeltas	= { { 2, 0 }, { -2, 0 } };

	public King( Board board, TwoPlayerColour colour, Integer x, Integer y )
	{
		super( board, "King", colour, x, y );
	}

	/**
	 * Gets final absolute position.
	 */
	@Override
	public Set<Position> getPossibleMoves ()
	{
		possibleMoves = new HashSet<Position>();

		for ( int[] deltaPopPlace : deltaPopPlaces )
		{
			if ( canPopTo( x + deltaPopPlace[ 0 ], y + deltaPopPlace[ 1 ] ) )
			{
				possibleMoves.add( new Position( x + deltaPopPlace[ 0 ], y + deltaPopPlace[ 1 ] ) );
			}
		}

		for ( int[] castlingDelta : castlingDeltas )
			if ( canCastleToDelta( castlingDelta[ 0 ], castlingDelta[ 1 ] ) )
			{
				possibleMoves.add( new Position( x + castlingDelta[ 0 ], y + castlingDelta[ 1 ] ) );
			}

		return possibleMoves;
	}

	private boolean canCastleToDelta ( int dx, int dy )
	{
		// King must not have moved.
		if ( hasMoved )
			return false;

		// A rook which hasn't moved must be the next piece in the direction.
		if ( !castlingRookExistsInDirection( dx, dy ) )
			return false;

		// The king cannot be in check, move through check, or end in check.
		if ( !castlingMovementAvoidsCheck( dx, dy ) )
			return false;

		return true;
	}

	private boolean castlingRookExistsInDirection ( int dx, int dy )
	{
		int middleX, middleY;
		boolean castleInX = ( dx != 0 );
		Piece nextPieceInPath;

		for ( int distance = 1;; distance++ ) // Increase distance until we find the rook.
		{
			middleX = this.x + ( castleInX ? distance : 0 );
			middleY = this.y + ( castleInX ? 0 : distance );

			if ( !board.isASquare( middleX, middleY ) ) // No rook found.
				return false;

			if ( board.hasPiece( middleX, middleY ) ) // This piece must be a rook of the same colour which has not moved.
			{
				nextPieceInPath = board.getPiece( middleX, middleY );

				if ( nextPieceInPath.getClass() != Rook.class )
					return false;
				else if ( nextPieceInPath.getColour() != this.getColour() )
					return false;
				else if ( board.getPiece( middleX, middleY ).hasMoved() )
					return false;
				else
				{
					break;
				}
			}
		}

		return true;
	}

	public boolean castlingMovementAvoidsCheck ( int dx, int dy )
	{

		return false;
	}

	@Override
	public boolean isMove ( int x, int y )
	{
		if ( !canPopTo( x, y ) )
			return false;

		if ( !getPossibleMoves().contains( new Position( x, y ) ) )
			return false;

		return true;
	}

	private boolean canPopTo ( int x, int y )
	{
		if ( !board.isASquare( x, y ) )
			return false;

		if ( !board.isEmptySquare( x, y ) && !( board.getPotentialPiece( x, y ).colour != this.colour ) )
			return false;

		return true;
	}
}
