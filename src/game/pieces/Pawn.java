package game.pieces;

import java.util.HashSet;
import java.util.Set;

import game.board.Board;
import game.board.Position;
import game.modes.TwoPlayerColour;

public class Pawn extends Piece
{
	private final int	direction;
	private int			initialY;

	public Pawn( Board board, TwoPlayerColour colour, Integer x, Integer y )
	{
		super( board, "Pawn", colour, x, y );

		direction = colour.getPawnMovement() * ( board.getColourAtBottom() == TwoPlayerColour.WHITE ? 1 : -1 );
		initialY = y;
	}

	@Override
	public Set<Position> getPossibleMoves ()
	{
		Set<Position> possibleMoves = new HashSet<Position>();

		if ( board.isASquare( x, y + direction ) && !board.hasPiece( x, y + direction ) )
		{
			possibleMoves.add( new Position( x, y + direction ) );

			if ( y == initialY )
			{
				if ( board.isASquare( x, y + 2 * direction ) && !board.hasPiece( x, y + 2 * direction ) )
				{
					possibleMoves.add( new Position( x, y + 2 * direction ) );
				}
			}
		}

		for ( int dx = -1; dx <= 1; dx += 2 )
		{
			if ( board.hasPiece( x + dx, y + direction ) && board.getPotentialPiece( x + dx, y + direction ).colour != this.colour )
			{
				possibleMoves.add( new Position( x + dx, y + direction ) );
			}
		}

		return possibleMoves;
	}

	@Override
	public boolean isMove ( int dest_x, int dest_y )
	{
		if ( dest_y == this.y + direction )
		{
			if ( dest_x < this.x - 1 || this.x + 1 < dest_x )
				return false;

			if ( dest_x == this.x )
				return board.isASquare( dest_x, dest_y ) && !board.hasPiece( dest_x, dest_y );
			else
				return board.hasPiece( dest_x, dest_y ) && board.getPotentialPiece( dest_x, dest_y ).colour != this.colour;
		}
		else if ( dest_y == y + 2 * direction && y == initialY )
		{
			boolean canMoveOneForward = board.isASquare( x, y + direction ) && !board.hasPiece( x, y + direction );
			boolean canMoveTwoForward = canMoveOneForward && board.isASquare( x, y + 2 * direction ) && !board.hasPiece( x, y + 2 * direction );
			return canMoveTwoForward;
		}
		else
			return false;
	}
}
