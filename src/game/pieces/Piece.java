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
	protected final Board			board;
	protected final String			name;
	protected final TwoPlayerColour	colour;
	protected final Image			image;

	protected int	x;
	protected int	y;

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

		Image tempImage = null;
		try
		{
			tempImage = new Image( Paths.get( "resources", "pieces", ( colour == TwoPlayerColour.WHITE ? "White " : "Black " ) + name + ".png" ).toString() );
		}
		catch ( SlickException e )
		{
			e.printStackTrace();
		}
		this.image = tempImage;
	}

	public abstract boolean isMove ( int x, int y );

	public boolean setPosition ( int x, int y )
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
