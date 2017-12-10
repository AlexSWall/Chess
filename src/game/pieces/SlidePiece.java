package game.pieces;

import java.util.HashSet;
import java.util.Set;

import game.board.Board;
import game.board.Position;
import game.modes.TwoPlayerColour;

public abstract class SlidePiece extends Piece
{
	private final Direction[] slideDirections;

	public SlidePiece( Board board, String name, TwoPlayerColour colour, int x, int y, Direction[] slideDirections )
	{
		super( board, name, colour, x, y );
		this.slideDirections = slideDirections;
	}

	@Override
	public Set<Position> getPossibleMoves ()
	{
		possibleMoves = new HashSet<Position>();
		int dist;
		int xNew, yNew;

		outerloop: for ( Direction slideDirection : slideDirections )
		{
			dist = 1;
			for ( dist = 1;; dist++ )
			{
				xNew = x + slideDirection.x * dist;
				yNew = y + slideDirection.y * dist;

				if ( board.isEmptySquare( xNew, yNew ) )
				{
					possibleMoves.add( new Position( xNew, yNew ) );
				}
				else if ( board.hasPiece( xNew, yNew ) && board.getPiece( xNew, yNew ).colour != this.colour )
				{
					possibleMoves.add( new Position( xNew, yNew ) );
					continue outerloop;
				}
				else
				{
					continue outerloop;
				}
			}
		}
		return possibleMoves;
	}

	@Override
	public boolean isMove ( int x, int y )
	{
		return canSlideByDelta( x - this.x, y - this.y );
	}

	private boolean canSlideByDelta ( int xDelta, int yDelta )
	{
		int xDir = Integer.signum( xDelta );
		int yDir = Integer.signum( yDelta );

		if ( xDir * yDelta != yDir * xDelta )
			return false;

		int distanceToMove = Integer.max( xDir * xDelta, yDir * yDelta );

		for ( int dist = 1; dist < distanceToMove; dist++ )
		{
			if ( !board.isEmptySquare( x + xDir * dist, y + yDir * dist ) )
				return false;
		}
		if ( !board.isASquare( x + xDelta, y + yDelta ) )
			return false;

		return board.isEmptySquare( x + xDelta, y + yDelta ) || board.getPiece( x + xDelta, y + yDelta ).colour != this.colour;
	}
}
