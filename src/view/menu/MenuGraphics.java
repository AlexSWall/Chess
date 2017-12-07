package view.menu;

import java.nio.file.Paths;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class MenuGraphics
{
	final static String	mainPath	= Paths.get( "resources", "main" ).toString();
	final Image[]		options;

	MenuGraphics()
	{
		Image[] optionsTemp = new Image[ 4 ];
		String menuOptionsPath = Paths.get( mainPath, "menu options" ).toString();

		try
		{
			optionsTemp[ 0 ] = new Image( Paths.get( menuOptionsPath, "ChessBoardMenuStart.PNG" ).toString() );
			optionsTemp[ 1 ] = new Image( Paths.get( menuOptionsPath, "ChessBoardMenuCustom.PNG" ).toString() );
			optionsTemp[ 2 ] = new Image( Paths.get( menuOptionsPath, "ChessBoardMenuOptions.PNG" ).toString() );
			optionsTemp[ 3 ] = new Image( Paths.get( menuOptionsPath, "ChessBoardMenuQuit.PNG" ).toString() );
		}
		catch ( SlickException e )
		{
			e.printStackTrace();
		}

		options = optionsTemp;
	}

	public Image getBackground ( int i )
	{
		return options[ i ];
	}
}
