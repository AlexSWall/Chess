package game.pieces;

import game.board.Board;
import game.modes.TwoPlayerColour;

public class Rook extends SlidePiece
{
	private static final Direction[] slideDirections = { Direction.UP, Direction.RIGHT, Direction.DOWN, Direction.LEFT };

	public Rook( Board board, TwoPlayerColour colour, Integer x, Integer y )
	{
		super( board, "Rook", colour, x, y, slideDirections );
	}
}
