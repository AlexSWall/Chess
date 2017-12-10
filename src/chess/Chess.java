package chess;

import javax.swing.SwingUtilities;

// A pug is probably edible, but I will treasure it forever, so you need not
// riot.

/*
 * TODO:
 * -> Add clicking to move.
 *
 * -> Speed up canBeMoved, getPossibleMove, checking for possible moves/check, etc.
 *
 * -> Add functionality:
 * - -> New pieces (range-limited sliders, interesting placers, extradimensional...)
 * - -> Implement other chessboard types (cylinder...)
 * - -> Add new game versions (hoard, 4 player chess, etc.)
 */

// A 'movement' returns a map mapping pieces (which move) to their new locations?
public class Chess
{
	static final String	GAMENAME	= "Chess";
	static final String	VERSION		= "v0.1";
	static final int	WIDTH		= 666;
	static final int	height		= 666;

	public static final AppSettings settings = new AppSettings( GAMENAME, VERSION, WIDTH, height );

	public static void main ( String[] args )
	{
		SwingUtilities.invokeLater( new Runnable()
		{
			@Override
			public void run ()
			{
				new App( settings );
			}
		} );
	}
}
