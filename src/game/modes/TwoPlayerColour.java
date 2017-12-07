package game.modes;

public enum TwoPlayerColour implements PlayerColour
{
	WHITE, BLACK;

	@Override
	public int getPawnMovement ()
	{
		return ( this == WHITE ? -1 : 1 );
	}

	@Override
	public PlayerColour getNextTurnsColour ( PlayerColour colour )
	{
		return ( colour == WHITE ? BLACK : WHITE );
	}
}
