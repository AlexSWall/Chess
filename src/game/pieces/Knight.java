package game.pieces;

import game.board.Board;
import game.modes.TwoPlayerColour;

public class Knight extends PopPiece
{
	private static final int[][] deltaPopPlaces = { { 1, 2 }, { 2, 1 }, { 2, -1 }, { 1, -2 }, { -1, -2 }, { -2, -1 }, { -2, 1 }, { -1, 2 } };

	public Knight( Board board, TwoPlayerColour colour, Integer x, Integer y )
	{
		super( board, "Knight", colour, x, y, deltaPopPlaces );
	}

}
