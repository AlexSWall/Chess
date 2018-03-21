package view.game;

import java.nio.file.Paths;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import game.board.Position;
import game.board.promotion.PromotionChoice;
import game.board.promotion.PromotionListener;
import game.modes.TwoPlayerColour;
import game.pieces.Pawn;

public class PromotionView
{
	private final GameView			view;
	private final PromotionListener	listener;

	private boolean	active;
	private Pawn	pawn;
	private int		gridYValue;

	private Image	knight, bishop, rook, queen;
	Position		renderPosition;

	PromotionView( GameView view, PromotionListener listener )
	{
		this.view = view;
		this.listener = listener;
	}

	public void render ( GameContainer gc, StateBasedGame sbg, Graphics g ) throws SlickException
	{
		renderPosition = view.gridToViewCoordinates( 2, gridYValue );
		knight.draw( renderPosition.x, renderPosition.y );

		renderPosition = view.gridToViewCoordinates( 3, gridYValue );
		bishop.draw( renderPosition.x, renderPosition.y );

		renderPosition = view.gridToViewCoordinates( 4, gridYValue );
		rook.draw( renderPosition.x, renderPosition.y );

		renderPosition = view.gridToViewCoordinates( 5, gridYValue );
		queen.draw( renderPosition.x, renderPosition.y );
	}

	public void mousePressed ( int button, int x, int y )
	{
		Position gridPos = view.viewToGridCoordinates( x, y );

		if ( gridPos.y != gridYValue )
			return;
		if ( gridPos.x < 2 || gridPos.x > 5 )
			return;

		switch ( gridPos.x )
		{
			case 2:
				firePromotionEvent( pawn, PromotionChoice.KNIGHT );
				break;
			case 3:
				firePromotionEvent( pawn, PromotionChoice.BISHOP );
				break;
			case 4:
				firePromotionEvent( pawn, PromotionChoice.ROOK );
				break;
			case 5:
				firePromotionEvent( pawn, PromotionChoice.QUEEN );
				break;
		}
	}

	private Image getPieceImage ( TwoPlayerColour colour, String name ) throws SlickException
	{
		return new Image( Paths.get( "resources", "pieces", ( colour == TwoPlayerColour.WHITE ? "White " : "Black " ) + name + ".png" ).toString() );
	}

	private void firePromotionEvent ( Pawn pawn, PromotionChoice choice )
	{
		listener.promote( pawn, choice );
		setInactive();
	}

	public void setPawn ( Pawn pawn )
	{
		this.pawn = pawn;
	}

	public void setInactive ()
	{
		this.active = false;
	}

	public void activate ( Pawn pawn )
	{
		this.active = true;
		this.pawn = pawn;
		this.gridYValue = ( pawn.getColour() == TwoPlayerColour.WHITE ? 6 : 1 );

		try
		{
			knight = getPieceImage( pawn.getColour(), "Knight" );
			bishop = getPieceImage( pawn.getColour(), "Bishop" );
			rook = getPieceImage( pawn.getColour(), "Rook" );
			queen = getPieceImage( pawn.getColour(), "Queen" );
		}
		catch ( SlickException e )
		{
			e.printStackTrace();
		}
	}

	public boolean isActive ()
	{
		return active;
	}
}
