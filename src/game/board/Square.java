package game.board;

import game.pieces.Piece;

public class Square
{
	private final int	x;
	private final int	y;
	private Piece		piece;

	public Square( int x, int y )
	{
		this.x = x;
		this.y = y;
	}

	public int getX ()
	{
		return x;
	}

	public int getY ()
	{
		return y;
	}

	public Piece getPiece ()
	{
		return piece;
	}

	public void setPiece ( Piece piece )
	{
		this.piece = piece;
		if ( piece != null )
		{
			piece.sudoMove( x, y );
		}
	}
}
