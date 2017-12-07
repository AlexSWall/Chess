package game.pieces;

import game.board.Board;
import game.modes.TwoPlayerColour;

public class Queen extends SlidePiece
{
	private static final Direction[] slideDirections = { Direction.UP, Direction.UPRIGHT, Direction.RIGHT, Direction.DOWNRIGHT, Direction.DOWN, Direction.DOWNLEFT, Direction.LEFT, Direction.UPLEFT };

	public Queen( Board board, TwoPlayerColour colour, Integer x, Integer y )
	{
		super( board, "Queen", colour, x, y, slideDirections );
	}
}
