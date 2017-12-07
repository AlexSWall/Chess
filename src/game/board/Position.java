package game.board;

public class Position
{
	public int	x;
	public int	y;

	public Position()
	{
		this( 0, 0 );
	}

	public Position( int x, int y )
	{
		this.x = x;
		this.y = y;
	}

	@Override
	public boolean equals ( Object obj )
	{
		if ( this == obj )
			return true;
		if ( obj == null )
			return false;
		if ( getClass() != obj.getClass() )
			return false;

		Position other = (Position) obj;
		if ( x != other.x || y != other.y )
			return false;

		return true;
	}

	@Override
	public int hashCode ()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + x;
		result = prime * result + y;
		return result;
	}

	@Override
	public String toString ()
	{
		return "( " + x + ", " + y + " )";
	}
}
