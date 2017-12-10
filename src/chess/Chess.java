package chess;

import javax.swing.SwingUtilities;

/*
 * TODO:
 * -> Speed up canBeMoved, getPossibleMove, checking for possible moves/check, etc.
 *
 * -> Castling in Chess960
 *
 * -> Add functionality:
 * - -> New pieces (range-limited sliders, interesting placers, extradimensional...)
 * - -> Implement other chessboard types (cylinder...)
 * - -> Add new game versions (hoard, 4 player chess, etc.)
 */

public class Chess
{
	static final String	GAMENAME	= "Chess";
	static final String	VERSION		= "v0.1";
	static final int	WIDTH		= 666;
	static final int	HEIGHT		= 666;

	public static final AppSettings settings = new AppSettings( GAMENAME, VERSION, WIDTH, HEIGHT );

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
