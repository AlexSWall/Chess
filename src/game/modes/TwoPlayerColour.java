package game.modes;

public enum TwoPlayerColour implements PlayerColour
{
	WHITE, BLACK, BOTH;

	@Override
	public int getPawnMovement ()
	{
		return ( this == WHITE ? -1 : 1 );
	}

	@Override
	public TwoPlayerColour getNextTurnsColour ()
	{
		return ( this == WHITE ? BLACK : WHITE );
	}

	public static TwoPlayerColour[] getPlayerColours ()
	{
		return new TwoPlayerColour[] { WHITE, BLACK };
	}
}
