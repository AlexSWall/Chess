package game;

import game.modes.TwoPlayerColour;

public class GameSettings
{
	public final boolean			useDefaultSetup;
	public final boolean			isWhite;
	public final TwoPlayerColour	playerColour;

	public GameSettings( boolean useDefaultSetup, boolean isWhite )
	{
		this.useDefaultSetup = useDefaultSetup;
		this.isWhite = isWhite;
		this.playerColour = ( isWhite ? TwoPlayerColour.WHITE : TwoPlayerColour.BLACK );
	}
}
