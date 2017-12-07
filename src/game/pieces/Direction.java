package game.pieces;

public enum Direction
{
	UP ( 0, 1 ), UPRIGHT ( 1, 1 ), RIGHT ( 1, 0 ), DOWNRIGHT ( 1, -1 ), DOWN ( 0, -1 ), DOWNLEFT ( -1, -1 ), LEFT ( -1, 0 ), UPLEFT ( -1, 1 );

	public int	x;
	public int	y;

	Direction( int x, int y )
	{
		this.x = x;
		this.y = y;
	}
}
