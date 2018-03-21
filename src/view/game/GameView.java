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
import game.modes.TwoPlayerColour;
import game.pieces.Pawn;
import game.pieces.Piece;
import view.View;

public class GameView extends BasicGameState
{
	private final View	mainView;
	private final int	ID;
	private final Board	board;

	private PromotionView promotionView;

	private Piece		pieceBeingDragged	= null;
	private Piece		pieceClicked		= null;
	private Position	mousePosition;

	public GameView( View mainView, int ID, Board board )
	{
		this.mainView = mainView;
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
		float xScale = mainView.getAppSettings().width / (float) mainView.getAppSettings().defaultWidth;
		float yScale = mainView.getAppSettings().height / (float) mainView.getAppSettings().defaultHeight;
		g.scale( xScale, yScale );

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
			x = (int) ( ( mousePosition.x ) / xScale - 40 );
			y = (int) ( ( mousePosition.y ) / yScale - 50 );
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

	public Position viewToGridCoordinates ( int viewX, int viewY )
	{
		return viewToGridCoordinates( getRotationAngle(), viewX, viewY );
	}

	public Position gridToViewCoordinates ( int gridX, int gridY )
	{
		return gridToViewCoordinates( getRotationAngle(), gridX, gridY );
	}

	private int getRotationAngle ()
	{
		switch ( board.getHumanPlayerColour() )
		{
			case WHITE:
				return 0;
			case BLACK:
				return 180;
			case BOTH:
				return ( board.getMovementLogic().getPlayerTurn() == TwoPlayerColour.BLACK ? 180 : 0 );
		}
		return 0;
	}

	private Position viewToGridCoordinates ( int theta, int x, int y )
	{
		int gridX = (int) ( ( x - 1 ) / ( mainView.getAppSettings().width / 8.0 ) );
		int gridY = (int) ( ( y - 1 ) / ( mainView.getAppSettings().height / 8.0 ) );

		switch ( theta )
		{
			case 0:
				return new Position( gridX, gridY );
			case 180:
				return new Position( board.getRowLength( gridY ) - gridX - 1, board.getColumnLength( gridX ) - gridY - 1 );
			default:
				return null;
		}
	}

	private Position gridToViewCoordinates ( int theta, int x, int y )
	{
		if ( theta == 180 )
		{
			int tmp = board.getRowLength( y ) - x - 1;
			y = board.getColumnLength( x ) - y - 1;
			x = tmp;
		}
		int viewX = (int) ( 1 + ( mainView.getAppSettings().defaultWidth / 8.0 ) * x );
		int viewY = (int) ( 1 + ( mainView.getAppSettings().defaultHeight / 8.0 ) * y );

		return new Position( viewX, viewY );
	}

	/**
	 * Gets piece at (x,y) as clicked in the view.
	 *
	 * @param x The x-coordinate in the view.
	 * @param y The y-coordinate in the view.
	 * @return The piece at this (x,y) coordinate.
	 */
	public Piece getPieceAt ( int x, int y )
	{
		Position pos = viewToGridCoordinates( x, y );
		return board.isASquare( pos.x, pos.y ) ? board.getPiece( pos.x, pos.y ) : null;
	}

	/**
	 * Draws the piece based upon its position in the grid.
	 *
	 * @param x The x-coordinate in the grid.
	 * @param y The y-coordinate in the grid.
	 */
	public void drawPieceAt ( Piece piece, int x, int y )
	{
		Position pos = gridToViewCoordinates( x, y );
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
			Position pos = viewToGridCoordinates( x, y );

			board.getMovementLogic().tryToMovePiece( pieceBeingDragged, pos.x, pos.y );

			pieceBeingDragged = null;
		}
	}

	@Override
	public void mouseClicked ( int button, int x, int y, int clickCount )
	{
		if ( button == 0 )
		{
			Position pos = viewToGridCoordinates( x, y );
			if ( pieceClicked != null && board.getMovementLogic().tryToMovePiece( pieceClicked, pos.x, pos.y ) )
			{
				pieceClicked = null;
			}
			else
			{
				pieceClicked = getPieceAt( x, y );
			}
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
