package chess;

import javax.swing.SwingUtilities;

// A pug is probably edible, but I will treasure it forever, so you need not
// riot.

/*
 * TODO:
 * 0. Click to move.
 * 1. Pawn Promotion and En Passant
 * 2. Castling
 * 3. New pieces (range-limited sliders, interesting placers,
 * extradimensional...)
 * 4. Override canBeMoved methods for speed.
 * 5. Remove extra boundary checks for pawns by guaranteeing sensible
 * promotions.
 * 6. Make sure 'getPossibleMove' methods are as fast as possible.
 * 7. Implement other chessboard types (chess960, cylinder,...)
 * 8. StartCustomGame main menu button.
 */

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
