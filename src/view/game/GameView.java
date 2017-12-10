package view.game;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import game.board.Board;
import game.board.Position;
import game.board.promotion.PromotionListener;
import game.pieces.Pawn;
import game.pieces.Piece;
import view.View;

public class GameView extends BasicGameState
{
	private final int	ID;
	private final Board	board;

	private PromotionView promotionView;

	private Piece		pieceBeingDragged	= null;
	private Position	mousePosition;

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

		if ( promotionView.isActive() )
		{
			promotionView.render( gc, sbg, g );
			return;
		}

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
			x = mousePosition.x - 40;
			y = mousePosition.y - 40;
			pieceBeingDragged.getImage().draw( x, y );
		}
	}

	@Override
	public void update ( GameContainer gc, StateBasedGame sbg, int delta ) throws SlickException
	{
		Input input = gc.getInput();

		mousePosition = new Position( input.getMouseX(), input.getMouseY() );

	}

	@Override
	public int getID ()
	{
		return ID;
	}

	public PromotionView getPromotionView ()
	{
		return promotionView;
	}

	public Board getBoard ()
	{
		return board;
	}

	public Position viewLocationToGrid ( int viewX, int viewY )
	{
		return new Position( (int) ( ( viewX - 1 ) / 83.33 ), (int) ( ( viewY - 1 ) / 83.33 ) );
	}

	public Position gridLocationToView ( int gridX, int gridY )
	{
		return new Position( (int) ( 1 + 83.33 * gridX ), (int) ( 1 + 83.33 * gridY ) );
	}

	public Piece getPieceAt ( int x, int y )
	{
		Position pos = viewLocationToGrid( x, y );
		return board.isASquare( pos.x, pos.y ) ? board.getPiece( pos.x, pos.y ) : null;
	}

	public void drawPieceAt ( Piece piece, int x, int y )
	{
		Position pos = gridLocationToView( x, y );
		piece.getImage().draw( pos.x, pos.y );
	}

	@Override
	public void mousePressed ( int button, int x, int y )
	{
		if ( promotionView.isActive() )
		{
			promotionView.mousePressed( button, x, y );
			return;
		}

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
			Position pos = viewLocationToGrid( x, y );

			board.getMovementLogic().tryToMovePiece( pieceBeingDragged, pos.x, pos.y );

			pieceBeingDragged = null;
		}
	}

	public void createPromotionView ( PromotionListener listener )
	{
		promotionView = new PromotionView( this, listener );
	}

	public void startPromotionChoiceMode ( Pawn pawn )
	{
		this.promotionView.activate( pawn );
	}
}
