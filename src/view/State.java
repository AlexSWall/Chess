package view;

public enum State
{

	MENU ( 0 ), GAME ( 1 );

	private int value;

	State( int value )
	{
		this.value = value;
	}

	public int getValue ()
	{
		return value;
	}
}
