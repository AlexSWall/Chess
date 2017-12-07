package game.pieces;

import java.util.HashSet;
import java.util.Set;

import game.board.Board;
import game.board.Position;
import game.modes.TwoPlayerColour;

public abstract class PopPiece extends Piece
{
	private final int[][] deltaPopPlaces;

	public PopPiece( Board board, String name, TwoPlayerColour colour, int x, int y, int[][] deltaPopPlaces )
	{
		super( board, name, colour, x, y );
		this.deltaPopPlaces = deltaPopPlaces;
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
		return possibleMoves;
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
