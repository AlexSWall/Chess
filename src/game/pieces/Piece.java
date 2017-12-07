package game.pieces;

import java.nio.file.Paths;
import java.util.Set;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import game.board.Board;
import game.board.Position;
import game.modes.TwoPlayerColour;

public abstract class Piece
{
	protected Board				board;
	protected String			name;
	protected int				x;
	protected int				y;
	protected TwoPlayerColour	colour;
	protected Image				image;

	protected boolean hasMoved;

	protected Set<Position> possibleMoves;

	public Piece( Board board, String name, TwoPlayerColour colour, int x, int y )
	{
		this.board = board;
		this.name = name;
		this.x = x;
		this.y = y;
		this.colour = colour;
		this.hasMoved = false;

		try
		{
			image = new Image( Paths.get( "resources", "pieces", ( colour == TwoPlayerColour.WHITE ? "White " : "Black " ) + name + ".png" ).toString() );
		}
		catch ( SlickException e )
		{
			e.printStackTrace();
		}
	}

	public abstract boolean isMove ( int x, int y );

	public final boolean canMoveTo ( int x, int y )
	{
		if ( isMove( x, y ) )
		{
			if ( !board.moveResultHasOwnKingInCheck( this, x, y ) )
				return true;
		}
		return false;
	}

	public boolean sudoMove ( int x, int y )
	{
		this.x = x;
		this.y = y;
		return true;
	}

	public abstract Set<Position> getPossibleMoves ();

	public String getName ()
	{
		return name;
	}

	public int getX ()
	{
		return x;
	}

	public int getY ()
	{
		return y;
	}

	public TwoPlayerColour getColour ()
	{
		return colour;
	}

	public Image getImage ()
	{
		return image;
	}

	public boolean hasMoved ()
	{
		return hasMoved;
	}

	public void setHasMoved ( boolean hasMoved )
	{
		this.hasMoved = hasMoved;
	}

	@Override
	public String toString ()
	{
		return colour + " " + name + " at ( " + x + ", " + y + " )";
	}

}
