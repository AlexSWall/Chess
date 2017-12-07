package game;

import javax.swing.event.EventListenerList;

public abstract class NewGameEventFirer
{
	static EventListenerList newGameListenerList = new EventListenerList();

	public static final void addNewGameListener ( NewGameListener listener )
	{
		newGameListenerList.add( NewGameListener.class, listener );
	}

	public static final void removeNewGameListener ( NewGameListener listener )
	{
		newGameListenerList.remove( NewGameListener.class, listener );
	}

	protected static final void fireNewGameEvent ( GameSettings settings )
	{
		Object[] listeners = newGameListenerList.getListenerList();

		for ( int i = 0; i < listeners.length; i += 2 )
		{
			if ( listeners[ i ] == NewGameListener.class )
			{
				( (NewGameListener) listeners[ i + 1 ] ).makeNewGame( settings );
			}
		}
	}
}
