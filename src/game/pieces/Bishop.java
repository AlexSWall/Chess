package game.pieces;

import game.board.Board;
import game.modes.TwoPlayerColour;

public class Bishop extends SlidePiece
{
	private static final Direction[] slideDirections = { Direction.UPRIGHT, Direction.DOWNRIGHT, Direction.DOWNLEFT, Direction.UPLEFT };

	public Bishop( Board board, TwoPlayerColour colour, Integer x, Integer y )
	{
		super( board, "Bishop", colour, x, y, slideDirections );
	}
}
