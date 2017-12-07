package view.game;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import game.board.Board;
import game.pieces.Piece;
import view.View;

public class GameView extends BasicGameState
{
	private final int	ID;
	private final Board	board;

	private Piece	pieceBeingDragged	= null;
	private Input	mostRecentInput		= null;

	public GameView( View mainView, int ID, Board board )
	{
		this.ID = ID;
		this.board = board;
	}

	@Override
	public void init ( GameContainer gc, StateBasedGame sbg ) throws SlickException
	{
		board.init();
	}

	@Override
	public void render ( GameContainer gc, StateBasedGame sbg, Graphics g ) throws SlickException
	{
		board.boardImage.draw( 0, 0 );

		int x, y;

		for ( Piece piece : board.getPieces() )
		{
			if ( piece == pieceBeingDragged )
			{
				continue;
			}
			else
			{
				x = piece.getX();
				y = piece.getY();
				drawPieceAt( piece, x, y );
			}
		}

		if ( pieceBeingDragged != null )
		{
			x = mostRecentInput.getMouseX() - 40;
			y = mostRecentInput.getMouseY() - 40;
			pieceBeingDragged.getImage().draw( x, y );
		}
	}

	@Override
	public void update ( GameContainer gc, StateBasedGame sbg, int delta ) throws SlickException
	{
		mostRecentInput = gc.getInput();
	}

	@Override
	public int getID ()
	{
		return ID;
	}

	private int[] viewLocationToGrid ( int viewX, int viewY )
	{
		return new int[] { (int) ( ( viewX - 1 ) / 83.33 ), (int) ( ( viewY - 1 ) / 83.33 ) };
	}

	private int[] gridLocationToView ( int gridX, int gridY )
	{
		return new int[] { (int) ( 1 + 83.33 * gridX ), (int) ( 1 + 83.33 * gridY ) };
	}

	private Piece getPieceAt ( int x, int y )
	{
		int[] loc = viewLocationToGrid( x, y );
		return board.getPotentialPiece( loc[ 0 ], loc[ 1 ] );
	}

	private void drawPieceAt ( Piece piece, int x, int y )
	{
		int[] loc = gridLocationToView( x, y );
		piece.getImage().draw( loc[ 0 ], loc[ 1 ] );
	}

	@Override
	public void mousePressed ( int button, int x, int y )
	{
		if ( button == 0 )
		{
			pieceBeingDragged = getPieceAt( x, y );
		}
	}

	@Override
	public void mouseReleased ( int button, int x, int y )
	{
		if ( button == 0 && pieceBeingDragged != null )
		{
			int[] loc = viewLocationToGrid( x, y );

			board.tryToMovePiece( pieceBeingDragged, loc[ 0 ], loc[ 1 ] );

			pieceBeingDragged = null;
		}
	}
}
