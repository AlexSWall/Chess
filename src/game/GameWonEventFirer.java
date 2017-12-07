package game;

import javax.swing.event.EventListenerList;

import game.modes.TwoPlayerColour;

public abstract class GameWonEventFirer
{
	static EventListenerList gameWonListenerList = new EventListenerList();

	public static final void addGameWonListener ( GameWonListener listener )
	{
		gameWonListenerList.add( GameWonListener.class, listener );
	}

	public static final void removeNewGameListener ( GameWonListener listener )
	{
		gameWonListenerList.remove( GameWonListener.class, listener );
	}

	protected static final void fireNewGameEvent ( TwoPlayerColour colourThatWon )
	{
		Object[] listeners = gameWonListenerList.getListenerList();

		for ( int i = 0; i < listeners.length; i += 2 )
		{
			if ( listeners[ i ] == GameWonListener.class )
			{
				( (GameWonListener) listeners[ i + 1 ] ).gameWon( colourThatWon );
			}
		}
	}
}
