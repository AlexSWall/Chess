package game;

import game.board.BoardSetupType;
import game.modes.TwoPlayerColour;

public class GameSettings
{
	public final BoardSetupType		type;
	public final TwoPlayerColour	playerColour;
	public final boolean			boardRotates;

	public GameSettings( BoardSetupType type, TwoPlayerColour colour )
	{
		this.type = type;
		this.playerColour = colour;
		this.boardRotates = playerColour == TwoPlayerColour.BOTH;
	}

	public boolean boardRotates ()
	{
		return boardRotates;
	}
}
