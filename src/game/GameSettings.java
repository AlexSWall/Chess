package game;

import game.board.BoardSetupType;
import game.modes.TwoPlayerColour;

public class GameSettings
{
	public final BoardSetupType		type;
	public final boolean			isWhite;
	public final TwoPlayerColour	playerColour;

	public GameSettings( BoardSetupType type, boolean isWhite )
	{
		this.type = type;
		this.isWhite = isWhite;
		this.playerColour = ( isWhite ? TwoPlayerColour.WHITE : TwoPlayerColour.BLACK );
	}
}
