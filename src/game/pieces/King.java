package game.pieces;

import java.util.HashSet;
import java.util.Set;

import game.board.Board;
import game.board.CastlingLogic;
import game.board.Position;
import game.modes.TwoPlayerColour;

public class King extends Piece
{
	private static final int[][] deltaPopPlaces = { { 0, 1 }, { 1, 1 }, { 1, 0 }, { 1, -1 }, { 0, -1 }, { -1, -1 }, { -1, 0 }, { -1, 1 } };

	private final CastlingLogic castlingLogic;

	public King( Board board, TwoPlayerColour colour, Integer x, Integer y )
	{
		super( board, "King", colour, x, y );
		castlingLogic = board.getCastlingLogic();
	}

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

		castlingLogic.addPossibleCastlingMoves( this, possibleMoves );

		return possibleMoves;
	}

	@Override
	public boolean isMove ( int x, int y )
	{
		return getPossibleMoves().contains( new Position( x, y ) );
	}

	public boolean isAdjacentTo ( int x, int y )
	{
		for ( int[] delta : deltaPopPlaces )
		{
			if ( this.x + delta[ 0 ] == x && this.y + delta[ 1 ] == y )
				return true;
		}
		return false;
	}

	private boolean canPopTo ( int x, int y )
	{
		if ( !board.isASquare( x, y ) )
			return false;

		if ( board.isEmptySquare( x, y ) || ( board.getPiece( x, y ).colour != this.colour ) )
			return true;

		return false;
	}
}
