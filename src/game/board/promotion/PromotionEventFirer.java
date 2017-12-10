package game.board.promotion;

import javax.swing.event.EventListenerList;

import game.pieces.Pawn;

public abstract class PromotionEventFirer
{
	static EventListenerList promotionListenerList = new EventListenerList();

	public static final void addPromotionListener ( PromotionListener listener )
	{
		promotionListenerList.add( PromotionListener.class, listener );
	}

	public static final void removePromotionListener ( PromotionListener listener )
	{
		promotionListenerList.remove( PromotionListener.class, listener );
	}

	protected static final void firePromotionEvent ( Pawn pawn, PromotionChoice choice )
	{
		Object[] listeners = promotionListenerList.getListenerList();

		for ( int i = 0; i < listeners.length; i += 2 )
		{
			if ( listeners[ i ] == PromotionListener.class )
			{
				( (PromotionListener) listeners[ i + 1 ] ).promote( pawn, choice );
			}
		}
	}
}
